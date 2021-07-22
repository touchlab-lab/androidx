package androidx.compose.ui.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.runtime.snapshots.SnapshotStateObserver
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.IntrinsicMeasurable
import androidx.compose.ui.layout.IntrinsicMeasureScope
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.RootMeasurePolicy
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.materialize
import androidx.compose.ui.node.DarwinUiApplier
import androidx.compose.ui.node.LayoutNode
import androidx.compose.ui.node.Ref
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGSizeMake
import platform.UIKit.NSLayoutConstraint
import platform.UIKit.UILayoutFittingCompressedSize
import platform.UIKit.UILayoutFittingExpandedSize
import platform.UIKit.UIView
import platform.UIKit.UIViewController
import platform.UIKit.heightAnchor
import platform.UIKit.layoutIfNeeded
import platform.UIKit.leadingAnchor
import platform.UIKit.systemLayoutSizeFittingSize
import platform.UIKit.topAnchor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints
import platform.UIKit.widthAnchor
import kotlin.math.ceil

val NoOpControllerUpdate: (UIViewController) -> Unit = {}
val NoOpBaseComposableUpdate: (UIViewControllerConvertible<*>) -> Unit = {}

fun Double.roundUpToInt(): Int {
    return ceil(this).toInt()
}

internal class UIKitControllerHolder<T: UIViewControllerConvertible<*>>(
    parentContext: CompositionContext?,
    factory: () -> T,
) {
    val backingConvertible: T = factory()
    val controller: UIViewController
        get() = backingConvertible.viewController

    init {
        parentContext?.let {
            controller.compositionContext = it
        }
    }

    var updateBlock: (T) -> Unit = NoOpBaseComposableUpdate
        set(value) {
            field = value
            update = { backingConvertible.apply(updateBlock) }
        }

    /**
     * The update logic of the [View].
     */
    var update: () -> Unit = {}
        protected set(value) {
            field = value
            hasUpdateBlock = true
            runUpdate()
        }
    private var hasUpdateBlock = false

    /**
     * The modifier of the `LayoutNode` corresponding to this [View].
     */
    var modifier: Modifier = Modifier
        set(value) {
            if (value !== field) {
                field = value
                onModifierChanged?.invoke(value)
            }
        }

    internal var onModifierChanged: ((Modifier) -> Unit)? = null

    /**
     * The screen density of the layout.
     */
    var density: Density = Density(1f)
        set(value) {
            if (value !== field) {
                field = value
                onDensityChanged?.invoke(value)
            }
        }

    internal var onDensityChanged: ((Density) -> Unit)? = null

    private val snapshotObserver = SnapshotStateObserver { command ->
        command()
    }

    private val onCommitAffectingUpdate: (UIKitControllerHolder<T>) -> Unit = {
        runUpdate()
    }

    private val runUpdate: () -> Unit = {
        if (hasUpdateBlock) {
            snapshotObserver.observeReads(this, onCommitAffectingUpdate, update)
        }
    }

    private var viewConstraints: ViewConstraints? = null

    private class ViewConstraints(
        val leading: NSLayoutConstraint,
        val top: NSLayoutConstraint,
        val minWidth: NSLayoutConstraint,
        val fixedWidth: NSLayoutConstraint,
        val maxWidth: NSLayoutConstraint,
        val minHeight: NSLayoutConstraint,
        val fixedHeight: NSLayoutConstraint,
        val maxHeight: NSLayoutConstraint,
    )

    private fun layoutAccordingTo(layoutNode: LayoutNode) {
        val position = layoutNode.coordinates.positionInRoot()
        val x = position.x.toDouble()
        val y = position.y.toDouble()
        viewConstraints?.leading?.constant = x
        viewConstraints?.top?.constant = y
//        setFrame(
//            frame.useContents {
//                CGRectMake(x, y, size.width, size.height)
//            }
//        )
        println("layoutAccordingTo($layoutNode): ($x, $y)")
    }

    private val sizingView: UIView
        get() = controller.view

    /**
     * A [LayoutNode] tree representation for this Android [View] holder.
     * The [LayoutNode] will proxy the Compose core calls to the [View].
     */
    val layoutNode: LayoutNode = run {
        // Prepare layout node that proxies measure and layout passes to the View.
        val layoutNode = LayoutNode()

        val coreModifier = Modifier
            .drawBehind {
                drawIntoCanvas { canvas ->
                    (layoutNode.owner as? UIKitComposeOwner)
                        ?.drawUIViewController(this@UIKitControllerHolder, canvas.nativeCanvas)
                }
            }.onGloballyPositioned {
                // The global position of this LayoutNode can change with it being replaced. For
                // these cases, we need to inform the View.
                layoutAccordingTo(layoutNode)
            }
        layoutNode.modifier = modifier.then(coreModifier)
        onModifierChanged = { layoutNode.modifier = it.then(coreModifier) }

        layoutNode.density = density
        onDensityChanged = { layoutNode.density = it }

//        var viewRemovedOnDetach: UIView? = null
        layoutNode.onAttach = { owner ->
            println("Debug attached")
//            if (viewRemovedOnDetach != null) view = viewRemovedOnDetach
            (owner as? UIKitComposeOwner)?.let {
                it.addUIViewController(this, layoutNode)
                controller.view.translatesAutoresizingMaskIntoConstraints = false
                viewConstraints = ViewConstraints(
                    leading = controller.view.leadingAnchor.constraintEqualToAnchor(it.view.leadingAnchor),
                    top = controller.view.topAnchor.constraintEqualToAnchor(it.view.topAnchor),
                    minWidth = controller.view.widthAnchor.constraintGreaterThanOrEqualToConstant(
                        0.0
                    ),
                    fixedWidth = controller.view.widthAnchor.constraintEqualToConstant(100.0),
                    maxWidth = controller.view.widthAnchor.constraintLessThanOrEqualToConstant(
                        10000.0
                    ),
                    minHeight = controller.view.heightAnchor.constraintGreaterThanOrEqualToConstant(
                        0.0
                    ),
                    fixedHeight = controller.view.heightAnchor.constraintEqualToConstant(100.0),
                    maxHeight = controller.view.heightAnchor.constraintLessThanOrEqualToConstant(
                        10000.0
                    ),
                ).apply {
                    listOf(leading, top).forEach { it.active = true }
                }
                layoutNode.requestRemeasure()

                runUpdate()
            }
        }
        layoutNode.onDetach = { owner ->
            (owner as? UIKitComposeOwner)?.removeUIViewController(this)
            viewConstraints = null
//            viewRemovedOnDetach = view
//            view = null
        }

        layoutNode.measurePolicy = if (controller is UIKitComposeOwner.Controller) {
            RootMeasurePolicy
        } else {
            object : MeasurePolicy {
                override fun MeasureScope.measure(
                    measurables: List<Measurable>,
                    constraints: Constraints
                ): MeasureResult {
                    println("Measuring: $constraints")
                    viewConstraints?.let { viewConstraints ->
                        if (constraints.hasFixedWidth) {
                            viewConstraints.fixedWidth.constant = constraints.maxWidth.toDouble()
                            viewConstraints.fixedWidth.active = true
                            viewConstraints.minWidth.active = false
                            viewConstraints.maxWidth.active = false
                        } else {
                            viewConstraints.fixedWidth.active = false

                            viewConstraints.maxWidth.active = if (constraints.hasBoundedWidth) {
                                viewConstraints.maxWidth.constant = constraints.maxWidth.toDouble()
                                true
                            } else {
                                false
                            }

                            viewConstraints.minWidth.active = if (constraints.minWidth > 0) {
                                viewConstraints.minWidth.constant = constraints.minWidth.toDouble()
                                true
                            } else {
                                false
                            }
                        }

                        if (constraints.hasFixedHeight) {
                            viewConstraints.fixedHeight.constant = constraints.maxHeight.toDouble()
                            viewConstraints.fixedHeight.active = true
                            viewConstraints.minHeight.active = false
                            viewConstraints.maxHeight.active = false
                        } else {
                            viewConstraints.fixedHeight.active = false

                            viewConstraints.maxHeight.active = if (constraints.hasBoundedHeight) {
                                viewConstraints.maxHeight.constant =
                                    constraints.maxHeight.toDouble()
                                true
                            } else {
                                false
                            }

                            viewConstraints.minHeight.active = if (constraints.minHeight > 0) {
                                viewConstraints.minHeight.constant =
                                    constraints.minHeight.toDouble()
                                true
                            } else {
                                false
                            }
                        }
                    }

    //                translatesAutoresizingMaskIntoConstraints = false
                    controller.view.layoutIfNeeded()
                    val (width, height) = controller.view.bounds.useContents {
                        size.width to size
                            .height
                    }
                    println("W: $width, H: $height")
                    return layout(width.roundUpToInt(), height.roundUpToInt())
                    {
                        layoutAccordingTo(layoutNode)
    //                    translatesAutoresizingMaskIntoConstraints = true
                    }
                }

                override fun IntrinsicMeasureScope.minIntrinsicWidth(
                    measurables: List<IntrinsicMeasurable>,
                    height: Int
                ) = sizingView.systemLayoutSizeFittingSize(
                    CGSizeMake(UILayoutFittingCompressedSize.width, height.toDouble())
                ).useContents { width.roundUpToInt() }

                override fun IntrinsicMeasureScope.maxIntrinsicWidth(
                    measurables: List<IntrinsicMeasurable>,
                    height: Int
                ) = sizingView.systemLayoutSizeFittingSize(
                    CGSizeMake(UILayoutFittingExpandedSize.width, height.toDouble())
                ).useContents { width.roundUpToInt() }

                override fun IntrinsicMeasureScope.minIntrinsicHeight(
                    measurables: List<IntrinsicMeasurable>,
                    width: Int
                ) = sizingView.systemLayoutSizeFittingSize(
                    CGSizeMake(width.toDouble(), UILayoutFittingCompressedSize.height)
                ).useContents { height.roundUpToInt() }

                override fun IntrinsicMeasureScope.maxIntrinsicHeight(
                    measurables: List<IntrinsicMeasurable>,
                    width: Int
                ) = sizingView.systemLayoutSizeFittingSize(
                    CGSizeMake(width.toDouble(), UILayoutFittingExpandedSize.height)
                ).useContents { height.roundUpToInt() }
            }
        }
        layoutNode
    }
}

@Composable
fun <T: UIViewControllerConvertible<*>> UIKitConvertible(
    factory: () -> T,
    modifier: Modifier = Modifier,
    update: (T) -> Unit = NoOpBaseComposableUpdate,
) {
    // Create a semantics node for accessibility. Semantics modifier is composed and need to be
    // materialized. So it can't be added in AndroidViewHolder when assigning modifier to layout
    // node, which is after the materialize call.
    val modifierWithSemantics = modifier.semantics(true) { }
    val materialized = currentComposer.materialize(modifierWithSemantics)
    val density = LocalDensity.current
    val parentReference = rememberCompositionContext()
    val viewFactoryHolderRef = remember { Ref<UIKitControllerHolder<T>>() }
    ComposeNode<LayoutNode, DarwinUiApplier>(
        factory = {
            val viewFactoryHolder = UIKitControllerHolder(parentReference, factory)
            viewFactoryHolderRef.value = viewFactoryHolder
            viewFactoryHolder.layoutNode
        },
        update = {
            set(materialized) { viewFactoryHolderRef.value!!.modifier = it }
            set(density) { viewFactoryHolderRef.value!!.density = it }
            set(update) { viewFactoryHolderRef.value!!.updateBlock = it }
        }
    )
}

@Composable
fun <T : UIViewController> UIKitController(
    factory: () -> T,
    modifier: Modifier = Modifier,
    update: (T) -> Unit = NoOpControllerUpdate
) {
    // Create a semantics node for accessibility. Semantics modifier is composed and need to be
    // materialized. So it can't be added in AndroidViewHolder when assigning modifier to layout
    // node, which is after the materialize call.
    val modifierWithSemantics = modifier.semantics(true) { }
    val materialized = currentComposer.materialize(modifierWithSemantics)
    val density = LocalDensity.current
//    val layoutDirection = LocalLayoutDirection.current
    val parentReference = rememberCompositionContext()
//    val stateRegistry = LocalSaveableStateRegistry.current
//    val stateKey = currentCompositeKeyHash.toString()
    val viewFactoryHolderRef = remember {
        Ref<UIKitControllerHolder<UIViewControllerConvertible<T>>>() }
    ComposeNode<LayoutNode, DarwinUiApplier>(
        factory = {
            val viewFactoryHolder = UIKitControllerHolder<UIViewControllerConvertible<T>>(parentReference) {
                object: UIViewControllerConvertible<T> {
                    override val viewController: T by lazy(factory)
                }
            }
//            @Suppress("UNCHECKED_CAST")
//            val savedState = stateRegistry?.consumeRestored(stateKey) as? SparseArray<Parcelable>
//            if (savedState != null) viewFactoryHolder.typedView?.restoreHierarchyState(savedState)
            viewFactoryHolderRef.value = viewFactoryHolder
            viewFactoryHolder.layoutNode
        },
        update = {
            set(materialized) { viewFactoryHolderRef.value!!.modifier = it }
            set(density) { viewFactoryHolderRef.value!!.density = it }
            set(update) { capturedUpdate ->
                viewFactoryHolderRef.value!!.updateBlock = { capturedUpdate(it.viewController) }
            }
//            set(layoutDirection) {
//                viewFactoryHolderRef.value!!.layoutDirection = when (it) {
//                    LayoutDirection.Ltr -> android.util.LayoutDirection.LTR
//                    LayoutDirection.Rtl -> android.util.LayoutDirection.RTL
//                }
//            }
        }
    )

//    if (stateRegistry != null) {
//        DisposableEffect(stateRegistry, stateKey) {
//            val valueProvider = {
//                val hierarchyState = SparseArray<Parcelable>()
//                viewFactoryHolderRef.value!!.typedView?.saveHierarchyState(hierarchyState)
//                hierarchyState
//            }
//            val entry = stateRegistry.registerProvider(stateKey, valueProvider)
//            onDispose {
//                entry.unregister()
//            }
//        }
//    }
}
