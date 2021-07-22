package androidx.compose.ui.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.Composition
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.runtime.snapshots.SnapshotStateObserver
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.materialize
import androidx.compose.ui.node.DarwinUiApplier
import androidx.compose.ui.node.LayoutNode
import androidx.compose.ui.node.LayoutNodeWrapper
import androidx.compose.ui.node.Ref
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.util.fastForEachIndexed
import kotlinx.cinterop.CValue
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSize
import platform.UIKit.UIColor
import platform.UIKit.UIContentContainerProtocol
import platform.UIKit.UIView
import platform.UIKit.UIViewController
import platform.UIKit.addChildViewController
import platform.UIKit.addSubview
import platform.UIKit.backgroundColor
import platform.UIKit.childViewControllers
import platform.UIKit.didMoveToParentViewController
import platform.UIKit.removeFromParentViewController
import platform.UIKit.removeFromSuperview
import platform.UIKit.setFrame
import platform.UIKit.willMoveToParentViewController
import kotlin.math.max

internal val LocalParentController = staticCompositionLocalOf<ParentController> {
    error("No parent controller")
}

internal class ParentController(
    private val parent: UIViewController,
    private val willAddSubview: (UIView) -> Unit,
    private val didAddSubview: (UIView) -> Unit,
    private val layoutNode: LayoutNode,
) {
    fun addChild(controller: UIViewController) {
        parent.addChildViewController(controller)
        val view = controller.view
        willAddSubview(view)
        parent.view.addSubview(view)
        view.setFrame(parent.view.bounds)
        didAddSubview(view)
        controller.didMoveToParentViewController(parent)
    }

    fun removeChild(controller: UIViewController) {
        check(parent.childViewControllers.contains(controller)) {
            "Controller: $controller is not a child of $parent"
        }

        controller.willMoveToParentViewController(null)
        controller.view.removeFromSuperview()
        controller.removeFromParentViewController()
    }

    fun addSubview(view: UIView) {
        println("adding subview $view to $parent")
        willAddSubview(view)
        parent.view.addSubview(view)
        didAddSubview(view)
    }

    fun localPositionOf(layoutNode: LayoutNode): Offset {
        return this.layoutNode.coordinates.localPositionOf(layoutNode.coordinates, Offset.Zero)
    }
}

interface ParentViewScope {
    fun onWillAddSubview(block: (UIView) -> Unit)
    fun onDidAddSubview(block: (UIView) -> Unit)
    fun <T: UIView> createView(block: () -> T): ViewFactory<T>

    fun interface ViewFactory<T: UIView> {
        fun create(): T
    }
}

internal class ParentViewScopeImpl: ParentViewScope {
    var willAddSubview: (UIView) -> Unit = { }
    var didAddSubview: (UIView) -> Unit = { }

    override fun onWillAddSubview(block: (UIView) -> Unit) {
        willAddSubview = block
    }

    override fun onDidAddSubview(block: (UIView) -> Unit) {
        didAddSubview = block
    }

    override fun <T : UIView> createView(block: () -> T): ParentViewScope.ViewFactory<T> {
        return ParentViewScope.ViewFactory(block)
    }
}

@Composable
fun <VIEW: UIView> SubComposable(
    viewFactory: ParentViewScope.() -> ParentViewScope.ViewFactory<VIEW>,
    modifier: Modifier = Modifier,
    viewUpdate: UIViewUpdateScope.(VIEW) -> Unit,
    content: @Composable () -> Unit,
) {
    // Create a semantics node for accessibility. Semantics modifier is composed and need to be
    // materialized. So it can't be added in AndroidViewHolder when assigning modifier to layout
    // node, which is after the materialize call.
    val modifierWithSemantics = modifier.semantics(true) { }
    val materialized = currentComposer.materialize(modifierWithSemantics)
    val density = LocalDensity.current
    val parentController = LocalParentController.current
    val parentReference = rememberCompositionContext()
    val viewFactoryHolderRef = remember { Ref<SubComposableHolder<VIEW>>() }
    ComposeNode<LayoutNode, DarwinUiApplier>(
        factory = {
            val viewFactoryHolder = SubComposableHolder(
                parentReference,
                parentController,
                viewFactory,
                content,
            )
            viewFactoryHolderRef.value = viewFactoryHolder
            viewFactoryHolder.layoutNode
        },
        update = {
            set(materialized) { viewFactoryHolderRef.value!!.modifier = it }
            set(density) { viewFactoryHolderRef.value!!.density = it }
            set(viewUpdate) { viewFactoryHolderRef.value!!.updateBlock = it }
        }
    )
}

internal class SubComposableHolder<VIEW: UIView>(
    parentContext: CompositionContext,
    private val parentController: ParentController,
    viewFactoryBuilder: ParentViewScope.() -> ParentViewScope.ViewFactory<VIEW>,
    private val content: @Composable () -> Unit
) {
    private val parentViewScope = ParentViewScopeImpl()
    private val viewFactory = viewFactoryBuilder(parentViewScope)
    val controllerDelegateFunctions = ControllerDelegateFunctions(
        loadView = { view = viewFactory.create() }
    )

    val controller = Controller()

    val view: VIEW
        get() = controller.view as VIEW

    init {
        controller.compositionContext = parentContext
    }

    private val updateScope = object: UIViewUpdateScope {
        override fun requestRemeasure() {
            layoutNode.requestRemeasure()
        }
    }

    var updateBlock: UIViewUpdateScope.(VIEW) -> Unit = NoOpUpdate
        set(value) {
            field = value
            update = { updateScope.updateBlock(view) }
        }

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

    private val onCommitAffectingUpdate: (SubComposableHolder<VIEW>) -> Unit = {
        runUpdate()
    }

    private val runUpdate: () -> Unit = {
        if (hasUpdateBlock) {
            snapshotObserver.observeReads(this, onCommitAffectingUpdate, update)
        }
    }

    private fun layoutAccordingTo(layoutNode: LayoutNode) {
        val position = parentController.localPositionOf(layoutNode)
        val size = layoutNode.coordinates.size
        val x = position.x.toDouble()
        val y = position.y.toDouble()
        val width = size.width.toDouble()
        val height = size.height.toDouble()

        view.setFrame(
            CGRectMake(x, y, width, height)
        )
        println("SUB: -layoutAccordingTo($layoutNode): ($x, $y, ${size})")
    }

    private val sizingView: UIView
        get() = controller.view

    val layoutNode: LayoutNode = run {
        // Prepare layout node that proxies measure and layout passes to the View.
        val layoutNode = LayoutNode()

        val coreModifier = Modifier
            .drawBehind {
                drawIntoCanvas { canvas ->
                    (layoutNode.owner as? UIKitComposeOwner)
                        ?.drawUIView(controller.view, canvas.nativeCanvas)
                }
            }.onGloballyPositioned {
                println("SUB onGloballyPositioned: $this, $layoutNode, $it")
                // The global position of this LayoutNode can change with it being replaced. For
                // these cases, we need to inform the View.
                layoutAccordingTo(layoutNode)
            }
        layoutNode.modifier = modifier.then(coreModifier)
        onModifierChanged = { layoutNode.modifier = it.then(coreModifier) }

        layoutNode.density = density
        onDensityChanged = { layoutNode.density = it }

        layoutNode.onAttach = { owner ->
            println("Debug attached")
            parentController.addChild(controller)

            layoutNode.requestRemeasure()

            runUpdate()
        }
        layoutNode.onDetach = { owner ->
            parentController.removeChild(controller)
        }

        layoutNode.measurePolicy = object: MeasurePolicy {
            override fun MeasureScope.measure(
                measurables: List<Measurable>,
                constraints: Constraints
            ): MeasureResult {
                if (measurables.isEmpty()) {
                    return layout(
                        constraints.minWidth,
                        constraints.minHeight
                    ) {
                        layoutAccordingTo(layoutNode)
                    }
                }

                if (measurables.size == 1) {
                    val measurable = measurables[0]
                    val placeable: Placeable = measurable.measure(constraints)
                    val boxWidth: Int = max(constraints.minWidth, placeable.width)
                    val boxHeight: Int = max(constraints.minHeight, placeable.height)
                    println("SUB placeable: $placeable - ${placeable.width}, ${placeable.height}")
                    println("SUB measurable: $measurable - ${measurable.parentData}")
                    println("SUB layout: $boxWidth, $boxHeight - ${layoutNode.coordinates.size}")
                    return layout(boxWidth, boxHeight) {
                        layoutAccordingTo(layoutNode/*, boxWidth, boxHeight*/)
                        println("SUB placing $placeable to 0, 0")
                        placeable.place(0, 0)
                    }
                }

                val placeables = arrayOfNulls<Placeable>(measurables.size)
                var boxWidth = constraints.minWidth
                var boxHeight = constraints.minHeight
                measurables.fastForEachIndexed { index, measurable ->
                    val placeable = measurable.measure(constraints)
                    placeables[index] = placeable
                    boxWidth = max(boxWidth, placeable.width)
                    boxHeight = max(boxHeight, placeable.height)
                }

                // Specify the size of the Box and position its children.
                return layout(boxWidth, boxHeight) {
                    layoutAccordingTo(layoutNode)
                    placeables.forEachIndexed { index, placeable ->
                        println("SUB placing $placeable to 0, 0")
                        (placeable as Placeable).place(0, 0)
                    }
                }
            }
        }
        layoutNode
    }

    val composition = Composition(DarwinUiApplier(layoutNode), parentContext).apply {
        setContent {
            CompositionLocalProvider(
                LocalParentController provides ParentController(
                    controller,
                    willAddSubview = parentViewScope.willAddSubview,
                    didAddSubview = parentViewScope.didAddSubview,
                    layoutNode,
                )
            ) {
                content()
            }
        }
    }

    private val onCommitAffectingMeasure: (LayoutNode) -> Unit = { layoutNode ->
        if (layoutNode.isValid) {
            layoutNode.requestRemeasure()
        }
    }

    private val onCommitAffectingLayout: (LayoutNode) -> Unit = { layoutNode ->
        if (layoutNode.isValid) {
            layoutNode.requestRelayout()
        }
    }

    inner class Controller: UIViewController(null, null) {

        override fun viewWillLayoutSubviews() {
            super.viewWillLayoutSubviews()
//            willLayoutSubviews()
            println("SUB viewWillLayoutSubviews()")
        }

        override fun viewDidLayoutSubviews() {
            super.viewDidLayoutSubviews()
            println("SUB viewDidLayoutSubviews()")
        }

        override fun viewDidLoad() {
            super.viewDidLoad()
            println("SUB viewDidLoad()")
        }

        override fun viewWillAppear(animated: Boolean) {
            super.viewWillAppear(animated)
            println("SUB viewWillAppear(animated: $animated)")
        }

        override fun loadView() {
//            super.loadView()
            controllerDelegateFunctions.loadView(this)
        }

        override fun isMovingToParentViewController(): Boolean {
            return super.isMovingToParentViewController().also {
                println("SUB isMovingToParentViewController() = $it")
            }
        }

        override fun isMovingFromParentViewController(): Boolean {
            return super.isMovingFromParentViewController().also {
                println("SUB isMovingFromParentViewController() = $it")
            }
        }

        override fun preferredContentSizeDidChangeForChildContentContainer(container: UIContentContainerProtocol) {
            super.preferredContentSizeDidChangeForChildContentContainer(container)
            println("SUB preferredContentSizeDidChangeForChildContentContainer($container)")
        }

        override fun sizeForChildContentContainer(
            container: UIContentContainerProtocol,
            withParentContainerSize: CValue<CGSize>
        ): CValue<CGSize> {
            return super.sizeForChildContentContainer(container, withParentContainerSize).also {
                println("SUB sizeForChildContentContainer($container, ${withParentContainerSize
                    .useContents { "($width, $height)" }}) = ${it.useContents { "($width, $height)"
                }}")
            }
        }

        override fun systemLayoutFittingSizeDidChangeForChildContentContainer(container: UIContentContainerProtocol) {
            super.systemLayoutFittingSizeDidChangeForChildContentContainer(container)
            println("SUB systemLayoutFittingSizeDidChangeForChildContentContainer($container)")
        }
    }
}
