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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.materialize
import androidx.compose.ui.node.DarwinUiApplier
import androidx.compose.ui.node.LayoutNode
import androidx.compose.ui.node.Ref
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import kotlinx.cinterop.CPointed
import kotlinx.cinterop.ObjCAction
import kotlinx.cinterop.interpretCPointer
import kotlinx.cinterop.objcPtr
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGRectZero
import platform.CoreGraphics.CGSizeMake
import platform.UIKit.NSLayoutConstraint
import platform.UIKit.UILayoutFittingCompressedSize
import platform.UIKit.UILayoutFittingExpandedSize
import platform.UIKit.UIView
import platform.UIKit.UIViewAutoresizingNone
import platform.UIKit.UIViewController
import platform.UIKit.addSubview
import platform.UIKit.autoresizingMask
import platform.UIKit.bottomAnchor
import platform.UIKit.heightAnchor
import platform.UIKit.layoutIfNeeded
import platform.UIKit.leadingAnchor
import platform.UIKit.removeFromSuperview
import platform.UIKit.setFrame
import platform.UIKit.subviews
import platform.UIKit.superview
import platform.UIKit.systemLayoutSizeFittingSize
import platform.UIKit.topAnchor
import platform.UIKit.trailingAnchor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints
import platform.UIKit.widthAnchor
import platform.UIKit.window
import platform.darwin.UInt8
import platform.objc.OBJC_ASSOCIATION_RETAIN
import platform.objc.objc_getAssociatedObject
import platform.objc.objc_setAssociatedObject
import kotlin.native.concurrent.ensureNeverFrozen

internal class UIKitViewHolder<T: UIView>(
    parentContext: CompositionContext,
    private val parentController: ParentController,
): UIView(CGRectZero.readValue()) {
    init {
        // Any [Abstract]ComposeViews that are descendants of this view will host
        // subcompositions of the host composition.
        // UiApplier doesn't supply this, only AndroidView.
        compositionContext = parentContext

        autoresizingMask = UIViewAutoresizingNone
        translatesAutoresizingMaskIntoConstraints = true
    }

    private var viewConstraints: ViewConstraints? = null

    private class ViewConstraints(
        val edges: List<NSLayoutConstraint>,
        val minWidth: NSLayoutConstraint,
        val fixedWidth: NSLayoutConstraint,
        val maxWidth: NSLayoutConstraint,
        val minHeight: NSLayoutConstraint,
        val fixedHeight: NSLayoutConstraint,
        val maxHeight: NSLayoutConstraint,
    )

    internal var typedView: T? = null

    var factory: (() -> T)? = null
        set(value) {
            field = value
            if (value != null) {
                typedView = value()
                view = typedView
            }
        }

    private val updateScope = object: UIViewUpdateScope {
        override fun requestRemeasure() {
            layoutNode.requestRemeasure()
        }
    }

    var updateBlock: UIViewUpdateScope.(T) -> Unit = NoOpUpdate
        set(value) {
            field = value
            update = {
                typedView?.let {
                    updateScope.updateBlock(it)
                }
            }
        }

    /**
     * The view hosted by this holder.
     */
    var view: UIView? = null
        internal set(value) {
            if (value !== field) {
                field = value
                subviews.forEach { (it as? UIView)?.removeFromSuperview() }
                viewConstraints = null
                if (value != null) {
                    addSubview(value)
                    value.translatesAutoresizingMaskIntoConstraints = false
                    viewConstraints = ViewConstraints(
                        edges = listOf(
                            value.leadingAnchor.constraintEqualToAnchor(leadingAnchor),
                            value.topAnchor.constraintEqualToAnchor(topAnchor),
                            value.trailingAnchor.constraintEqualToAnchor(trailingAnchor),
                            value.bottomAnchor.constraintEqualToAnchor(bottomAnchor),
                        ),
                        minWidth = value.widthAnchor.constraintGreaterThanOrEqualToConstant(0.0),
                        fixedWidth = value.widthAnchor.constraintEqualToConstant(100.0),
                        maxWidth = value.widthAnchor.constraintLessThanOrEqualToConstant(10000.0),
                        minHeight = value.heightAnchor.constraintGreaterThanOrEqualToConstant(0.0),
                        fixedHeight = value.heightAnchor.constraintEqualToConstant(100.0),
                        maxHeight = value.heightAnchor.constraintLessThanOrEqualToConstant(10000.0),
                    ).apply {
                        edges.forEach { it.active = true }
                    }
                    layoutNode.requestRemeasure()

                    runUpdate()
                }
            }
        }

    private val sizingView: UIView
        get() = (view ?: this).also { println("Sizing view: $it") }

    var update: () -> Unit = {}
        protected set(value) {
            field = value
            hasUpdateBlock = true
            runUpdate()
        }
    private var hasUpdateBlock = false

    var modifier: Modifier = Modifier
        set(value) {
            if (value !== field) {
                field = value
                onModifierChanged?.invoke(value)
            }
        }

    internal var onModifierChanged: ((Modifier) -> Unit)? = null

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
    }.also { it.ensureNeverFrozen() }

    private val onCommitAffectingUpdate: (UIKitViewHolder<T>) -> Unit = {
        runUpdate()
    }

    private val runUpdate: () -> Unit = {
        if (hasUpdateBlock) {
            snapshotObserver.observeReads(this, onCommitAffectingUpdate, update)
        }
    }

    internal var onRequestDisallowInterceptTouchEvent: ((Boolean) -> Unit)? = null

    @ObjCAction
    fun didMoveToWindow() {
        if (window != null) {
            snapshotObserver.start()
        } else {
            snapshotObserver.stop()
            // remove all observations:
            snapshotObserver.clear()
        }
    }

    private fun UIView.layoutAccordingTo(layoutNode: LayoutNode) {
        val position = parentController.localPositionOf(layoutNode)
        val size = layoutNode.coordinates.size
        val x = position.x.toDouble()
        val y = position.y.toDouble()
        val width = size.width.toDouble()
        val height = size.height.toDouble()
        setFrame(
            frame.useContents {
                CGRectMake(x, y, width, height)
            }
        )
        println("layoutAccordingTo($layoutNode): ($x, $y), ($width x $height)")
    }

    val layoutNode: LayoutNode = run {
        // Prepare layout node that proxies measure and layout passes to the View.
        val layoutNode = LayoutNode()

        val coreModifier = Modifier
            .drawBehind {
                drawIntoCanvas { canvas ->
                    (layoutNode.owner as? UIKitComposeOwner)
                        ?.drawUIView(this@UIKitViewHolder, canvas.nativeCanvas)
                }
            }.onGloballyPositioned {
                // The global position of this LayoutNode can change with it being replaced. For
                // these cases, we need to inform the View.
                println("onGloballyPositioned: $this, $layoutNode, $it")
                layoutAccordingTo(layoutNode)
            }
        layoutNode.modifier = modifier.then(coreModifier)
        onModifierChanged = { layoutNode.modifier = it.then(coreModifier) }

        layoutNode.density = density
        onDensityChanged = { layoutNode.density = it }

        var viewRemovedOnDetach: UIView? = null
        layoutNode.onAttach = { owner ->
            println("Debug attached")
            if (viewRemovedOnDetach != null) view = viewRemovedOnDetach
            parentController.addSubview(this)
            owner.root.requestRemeasure()
        }
        layoutNode.onDetach = { owner ->
            removeFromSuperview()
            viewRemovedOnDetach = view
            view = null
        }

        layoutNode.measurePolicy = object : MeasurePolicy {
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
                            viewConstraints.maxHeight.constant = constraints.maxHeight.toDouble()
                            true
                        } else {
                            false
                        }

                        viewConstraints.minHeight.active = if (constraints.minHeight > 0) {
                            viewConstraints.minHeight.constant = constraints.minHeight.toDouble()
                            true
                        } else {
                            false
                        }
                    }
                }

                translatesAutoresizingMaskIntoConstraints = false
                layoutIfNeeded()
                val (width, height) = bounds.useContents { size.width to size.height }
                println("W: $width, H: $height")
                return layout(width.roundUpToInt(), height.roundUpToInt()) {
                    layoutAccordingTo(layoutNode)
                    translatesAutoresizingMaskIntoConstraints = true
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
        layoutNode
    }

}

@Composable
fun <T : UIView> UIKitView(
    factory: () -> T,
    modifier: Modifier = Modifier,
    update: UIViewUpdateScope.(T) -> Unit = NoOpUpdate
) {
    // Create a semantics node for accessibility. Semantics modifier is composed and need to be
    // materialized. So it can't be added in AndroidViewHolder when assigning modifier to layout
    // node, which is after the materialize call.
    val modifierWithSemantics = modifier.semantics(true) { }
    val materialized = currentComposer.materialize(modifierWithSemantics)
    val density = LocalDensity.current
//    val layoutDirection = LocalLayoutDirection.current
    val parentReference = rememberCompositionContext()
    val parentController = LocalParentController.current
//    val stateRegistry = LocalSaveableStateRegistry.current
//    val stateKey = currentCompositeKeyHash.toString()
    val viewFactoryHolderRef = remember { Ref<UIKitViewHolder<T>>() }
    ComposeNode<LayoutNode, DarwinUiApplier>(
        factory = {
            val viewFactoryHolder = UIKitViewHolder<T>(parentReference, parentController)
            viewFactoryHolder.factory = factory
            @Suppress("UNCHECKED_CAST")
//            val savedState = stateRegistry?.consumeRestored(stateKey) as? SparseArray<Parcelable>
//            if (savedState != null) viewFactoryHolder.typedView?.restoreHierarchyState(savedState)
            viewFactoryHolderRef.value = viewFactoryHolder
            viewFactoryHolder.layoutNode
        },
        update = {
            set(materialized) { viewFactoryHolderRef.value!!.modifier = it }
            set(density) { viewFactoryHolderRef.value!!.density = it }
            set(update) { viewFactoryHolderRef.value!!.updateBlock = it }
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

interface UIViewUpdateScope {
    fun requestRemeasure()
}

val NoOpUpdate: UIViewUpdateScope.(UIView) -> Unit = {}

/**
 * The [CompositionContext] that should be used as a parent for compositions at or below
 * this view in the hierarchy. Set to non-`null` to provide a [CompositionContext]
 * for compositions created by child views, or `null` to fall back to any [CompositionContext]
 * provided by ancestor views.
 *
 * See [findViewTreeCompositionContext].
 */
private var compositionContextKey: UInt8 = 0u
private val compositionContextKeyPtr = interpretCPointer<CPointed>(compositionContextKey.objcPtr())
var UIView.compositionContext: CompositionContext?
    get() = objc_getAssociatedObject(this, compositionContextKeyPtr) as? CompositionContext
    set(value) {
        objc_setAssociatedObject(this, compositionContextKeyPtr, value, OBJC_ASSOCIATION_RETAIN)
    }


//private var compositionContextKey: UInt8 = 0u
//private val compositionContextKeyPtr = interpretCPointer<CPointed>(compositionContextKey.objcPtr())
var UIViewController.compositionContext: CompositionContext?
    get() = objc_getAssociatedObject(this, compositionContextKeyPtr) as? CompositionContext
    set(value) {
        objc_setAssociatedObject(this, compositionContextKeyPtr, value, OBJC_ASSOCIATION_RETAIN)
    }

/**
 * Returns the parent [CompositionContext] for this point in the view hierarchy, or `null`
 * if none can be found.
 *
 * See [compositionContext] to get or set the parent [CompositionContext] for
 * a specific view.
 */
fun UIView.findViewTreeCompositionContext(): CompositionContext? {
    var found: CompositionContext? = compositionContext
    if (found != null) return found
    var parent: UIView? = superview
    while (found == null && parent != null) {
        found = parent.compositionContext
        parent = parent.superview
    }
    return found
}
