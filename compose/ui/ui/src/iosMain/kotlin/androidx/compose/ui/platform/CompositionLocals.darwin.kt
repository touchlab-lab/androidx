package androidx.compose.ui.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composition
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DefaultMonotonicFrameClock
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.ObserverHandle
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.Autofill
import androidx.compose.ui.autofill.AutofillTree
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusDirection.Companion.Down
import androidx.compose.ui.focus.FocusDirection.Companion.In
import androidx.compose.ui.focus.FocusDirection.Companion.Left
import androidx.compose.ui.focus.FocusDirection.Companion.Next
import androidx.compose.ui.focus.FocusDirection.Companion.Out
import androidx.compose.ui.focus.FocusDirection.Companion.Previous
import androidx.compose.ui.focus.FocusDirection.Companion.Right
import androidx.compose.ui.focus.FocusDirection.Companion.Up
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.geometry.MutableRect
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.pointer.PositionCalculator
import androidx.compose.ui.layout.RootMeasurePolicy
import androidx.compose.ui.node.InternalCoreApi
import androidx.compose.ui.node.LayoutNode
import androidx.compose.ui.node.MeasureAndLayoutDelegate
import androidx.compose.ui.node.OwnedLayer
import androidx.compose.ui.node.Owner
import androidx.compose.ui.node.OwnerSnapshotObserver
import androidx.compose.ui.node.RootForTest
import androidx.compose.ui.semantics.SemanticsModifierCore
import androidx.compose.ui.semantics.SemanticsOwner
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.input.TextInputService
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.graphics.CanvasHolder
import androidx.compose.ui.graphics.NativeCanvas
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.node.DarwinUiApplier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.EditCommand
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.ImeOptions
import androidx.compose.ui.text.input.PlatformTextInputService
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Constraints
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ObjCAction
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import platform.CoreGraphics.CGContextRef
import platform.CoreGraphics.CGPoint
import platform.CoreGraphics.CGPointMake
import platform.CoreGraphics.CGRect
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGRectZero
import platform.CoreGraphics.CGSize
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSURL
import platform.QuartzCore.CALayer
import platform.QuartzCore.CATransform3DConcat
import platform.QuartzCore.CATransform3DMakeRotation
import platform.QuartzCore.CATransform3DMakeScale
import platform.QuartzCore.CATransform3DMakeTranslation
import platform.UIKit.UIApplication
import platform.UIKit.UIColor
import platform.UIKit.UIContentContainerProtocol
import platform.UIKit.UIFont
import platform.UIKit.UIGraphicsPopContext
import platform.UIKit.UIGraphicsPushContext
import platform.UIKit.UIView
import platform.UIKit.UIViewAutoresizingFlexibleHeight
import platform.UIKit.UIViewAutoresizingFlexibleWidth
import platform.UIKit.UIViewController
import platform.UIKit.addChildViewController
import platform.UIKit.addSubview
import platform.UIKit.autoresizingMask
import platform.UIKit.backgroundColor
import platform.UIKit.bottomAnchor
import platform.UIKit.childViewControllers
import platform.UIKit.clipsToBounds
import platform.UIKit.convertPoint
import platform.UIKit.didMoveToParentViewController
import platform.UIKit.drawViewHierarchyInRect
import platform.UIKit.leadingAnchor
import platform.UIKit.removeFromParentViewController
import platform.UIKit.removeFromSuperview
import platform.UIKit.setFrame
import platform.UIKit.setNeedsDisplay
import platform.UIKit.setNeedsLayout
import platform.UIKit.subviews
import platform.UIKit.systemFontSize
import platform.UIKit.topAnchor
import platform.UIKit.trailingAnchor
import platform.UIKit.translatesAutoresizingMaskIntoConstraints
import platform.UIKit.willMoveToParentViewController
import platform.UIKit.window
import kotlin.math.roundToInt


internal class CALayerLayer(
    val drawBlock: (Canvas) -> Unit,
): OwnedLayer {
    val layer: CALayer = InnerLayer { ctx ->
        canvasHolder.drawInto(NativeCanvas(ctx)) {
            drawBlock(this)
        }
    }

    override val layerId: Long
        get() = TODO("Not yet implemented")

    private val canvasHolder = CanvasHolder()

    override fun updateLayerProperties(
        scaleX: Float,
        scaleY: Float,
        alpha: Float,
        translationX: Float,
        translationY: Float,
        shadowElevation: Float,
        rotationX: Float,
        rotationY: Float,
        rotationZ: Float,
        cameraDistance: Float,
        transformOrigin: TransformOrigin,
        shape: Shape,
        clip: Boolean,
        layoutDirection: LayoutDirection,
        density: Density
    ) {
        layer.transform =
            listOf(
                CATransform3DMakeScale(scaleX.toDouble(), scaleY.toDouble(), 0.0),
                CATransform3DMakeTranslation(translationX.toDouble(), translationY.toDouble(), 0.0),
                CATransform3DMakeRotation(
                    rotationX.toDouble(), 1.0, 0.0, 0.0,
                ),
                CATransform3DMakeRotation(
                    rotationY.toDouble(), 0.0, 1.0, 0.0,
                ),
                CATransform3DMakeRotation(
                    rotationZ.toDouble(), 0.0, 0.0, 1.0,
                )
            ).reduce(::CATransform3DConcat)

        layer.anchorPoint = CGPointMake(
            transformOrigin.pivotFractionX.toDouble(),
            transformOrigin.pivotFractionY.toDouble(),
        )
        layer.opacity = alpha

        layer.masksToBounds = clip
        layer.contentsScale = density.density.toDouble()
    }

    override fun isInLayer(position: Offset): Boolean {
        val x = position.x
        val y = position.y

        return if (layer.masksToBounds) {
            layer.bounds.useContents {
                position.x <= x && x < size.width && position.y <= y && y < size.height
            }
        } else {
            true
        }
    }

    override fun move(position: IntOffset) {
        layer.frame = layer.frame.useContents {
            CGRectMake(
                position.x.toDouble(),
                position.y.toDouble(),
                size.width,
                size.height,
            )
        }
    }

    override fun resize(size: IntSize) {
        layer.frame = layer.frame.useContents {
            CGRectMake(
                origin.x,
                origin.y,
                size.width.toDouble(),
                size.height.toDouble(),
            )
        }
    }

    override fun drawLayer(canvas: Canvas) {
        TODO("Not yet implemented")
    }

    override fun updateDisplayList() {
        TODO("Not yet implemented")
    }

    override fun invalidate() {
        layer.setNeedsLayout()
        layer.setNeedsDisplay()
    }

    override fun destroy() {
        TODO("Not yet implemented")
    }

    private val CValue<CGRect>.desc: String
        get() = useContents { "(${origin.x}, ${origin.y}, ${size.width}, ${size.height})" }

    override fun mapOffset(point: Offset, inverse: Boolean): Offset {
        return layer.frame.useContents {
            point - Offset(origin.x.toFloat(), origin.y.toFloat())
        }.also {
            println("mapOffset: $point, $it - ${layer.frame.desc}")
        }
    }

    override fun mapBounds(rect: MutableRect, inverse: Boolean) {
        println("mapBounds: $rect, $inverse - ${layer.frame.desc}")
    }

    private class InnerLayer(private val doDrawInContext: (CGContextRef?) -> Unit): CALayer() {
        override fun drawInContext(ctx: CGContextRef?) {
            doDrawInContext(ctx)
        }
    }
}

class ControllerDelegateFunctions(
    val loadView: UIViewController.() -> Unit,
)

@OptIn(ExperimentalComposeUiApi::class)
internal class UIKitComposeOwner(private val delegate: ControllerDelegateFunctions): Owner, PositionCalculator,
    RootForTest {
    val controller = Controller(this)
    val view: UIView
        get() = controller.view

    var composition: Composition? = null

    override val density: Density
        get() = Density(
            controller.traitCollection.displayScale.toFloat(),
            controller.traitCollection.displayScale.toFloat(),
        )

    private val semanticsModifier = SemanticsModifierCore(
        id = SemanticsModifierCore.generateSemanticsId(),
        mergeDescendants = false,
        clearAndSetSemantics = false,
        properties = { }
    )

    override val focusManager: FocusManager = object: FocusManager {
        override fun clearFocus(force: Boolean) {
            controller.resignFirstResponder()
        }

        override fun moveFocus(focusDirection: FocusDirection): Boolean {
            // TODO: Implement focus move
            return false
        }
    }

    override val windowInfo: WindowInfo = object: WindowInfo {
        override val isWindowFocused: Boolean
            get() = true
    }

    override val root = LayoutNode().also {
        it.measurePolicy = RootMeasurePolicy
        it.modifier = Modifier
            .then(semanticsModifier)
//            .then(_focusManager.modifier)
//            .then(keyInputModifier)
    }

    override val rootForTest: RootForTest = this

    override val semanticsOwner: SemanticsOwner = SemanticsOwner(root)

    override val autofillTree = AutofillTree()

    //    private val _autofill = if (autofillSupported()) Da
    override val autofill: Autofill?
        get() = null

    private var observationClearRequested = false

    override val clipboardManager: ClipboardManager = object: ClipboardManager {
        override fun setText(annotatedString: AnnotatedString) {

        }

        override fun getText(): AnnotatedString? {
            return null
        }
    }

    override val accessibilityManager: AccessibilityManager = object: AccessibilityManager {
        override fun calculateRecommendedTimeoutMillis(
            originalTimeoutMillis: Long,
            containsIcons: Boolean,
            containsText: Boolean,
            containsControls: Boolean
        ): Long {
            return originalTimeoutMillis
        }
    }

    override val snapshotObserver = OwnerSnapshotObserver { command ->
        command()
    }

    @OptIn(InternalCoreApi::class)
    override var showLayoutBounds = false

    // Will be set to true when we were measured twice with different constraints during the last
    // measure pass.
    private var wasMeasuredWithMultipleConstraints = false

    private val measureAndLayoutDelegate = MeasureAndLayoutDelegate(root)
    override val measureIteration: Long get() = measureAndLayoutDelegate.measureIteration

    override val viewConfiguration: ViewConfiguration = object: ViewConfiguration {
        override val longPressTimeoutMillis: Long
            get() = 300L
        override val doubleTapTimeoutMillis: Long
            get() = 200L
        override val doubleTapMinTimeMillis: Long
            get() = 100L
        override val touchSlop: Float
            get() = 1f
    }

    override val textInputService: TextInputService = TextInputService(
        object: PlatformTextInputService {
            override fun hideSoftwareKeyboard() {
                TODO("Not yet implemented")
            }

            override fun notifyFocusedRect(rect: Rect) {
                TODO("Not yet implemented")
            }

            override fun showSoftwareKeyboard() {
                TODO("Not yet implemented")
            }

            override fun startInput(
                value: TextFieldValue,
                imeOptions: ImeOptions,
                onEditCommand: (List<EditCommand>) -> Unit,
                onImeActionPerformed: (ImeAction) -> Unit
            ) {
                TODO("Not yet implemented")
            }

            override fun stopInput() {
                TODO("Not yet implemented")
            }

            override fun updateState(oldValue: TextFieldValue?, newValue: TextFieldValue) {
                TODO("Not yet implemented")
            }
        }
    )

    override val fontLoader: Font.ResourceLoader = object: Font.ResourceLoader {
        override fun load(font: Font): Any {
            return UIFont.systemFontOfSize(UIFont.systemFontSize)
        }
    }

    override var layoutDirection: LayoutDirection by mutableStateOf(LayoutDirection.Ltr)
        private set

    override val hapticFeedBack: HapticFeedback = object: HapticFeedback {
        override fun performHapticFeedback(hapticFeedbackType: HapticFeedbackType) {

        }
    }


    override val textToolbar: TextToolbar = object: TextToolbar {
        override fun showMenu(
            rect: Rect,
            onCopyRequested: ActionCallback?,
            onPasteRequested: ActionCallback?,
            onCutRequested: ActionCallback?,
            onSelectAllRequested: ActionCallback?
        ) {

        }

        override fun hide() {

        }

        override val status: TextToolbarStatus = TextToolbarStatus.Hidden
    }

    init {
        root.attach(this)
    }

    override fun screenToLocal(positionOnScreen: Offset): Offset {
        println("screenToLocal: $positionOnScreen")
        return positionOnScreen
    }

    override fun localToScreen(localPosition: Offset): Offset {
        println("localToScreen: $localPosition")
        return localPosition
    }

    private fun scheduleMeasureAndLayout(nodeToRemeasure: LayoutNode? = null) {
        if (/*!isLayoutRequested && */view.window != null) {
            if (wasMeasuredWithMultipleConstraints && nodeToRemeasure != null) {
                var node = nodeToRemeasure
                while (
                    node != null &&
                    node.measuredByParent == LayoutNode.UsageByParent.InMeasureBlock
                ) {
                    node = node.parent
                }
                if (node === root) {
                    view.setNeedsLayout()
                    return
                }
            }

            if (view.frame.useContents { size.width == 0.0 || size.height == 0.0 }) {
                // If the view has no size calling invalidate() will be skipped
                view.setNeedsLayout()
            } else {
                view.setNeedsDisplay()
            }
        }
    }

    override fun onAttach(node: LayoutNode) {
    }

    override fun onDetach(node: LayoutNode) {
        measureAndLayoutDelegate.onNodeDetached(node)
        requestClearInvalidObservations()
    }

    fun requestClearInvalidObservations() {
        observationClearRequested = true
    }

    fun clearInvalidObservations() {
        if (observationClearRequested) {
            snapshotObserver.clearInvalidObservations()
            observationClearRequested = false
        }
        clearChildInvalidObservations(controller)
    }

    private fun clearChildInvalidObservations(controller: UIViewController) {
        for (childViewController in controller.childViewControllers) {
            if (childViewController is UIKitComposeOwner) {
                childViewController.clearInvalidObservations()
            } else if (childViewController is UIViewController) {
                clearChildInvalidObservations(childViewController)
            }
        }
    }

    fun Offset.toCGPoint(): CValue<CGPoint> = CGPointMake(x.toDouble(), y.toDouble())

    fun CValue<CGPoint>.toOffset(): Offset = useContents { Offset(x.toFloat(), y.toFloat()) }

    override fun calculatePositionInWindow(localPosition: Offset): Offset =
        view.convertPoint(localPosition.toCGPoint(), toView = view.window).toOffset()

    override fun calculateLocalPosition(positionInWindow: Offset): Offset =
        view.convertPoint(positionInWindow.toCGPoint(), fromView = view.window).toOffset()

    override fun requestFocus(): Boolean {
        return view.becomeFirstResponder()
    }

    override fun measureAndLayout() {
        val rootNodeResized = measureAndLayoutDelegate.measureAndLayout()
        if (rootNodeResized) {
            view.setNeedsLayout()
        }
        measureAndLayoutDelegate.dispatchOnPositionedCallbacks()
    }

    override fun onRequestMeasure(layoutNode: LayoutNode) {
        if (measureAndLayoutDelegate.requestRemeasure(layoutNode)) {
            scheduleMeasureAndLayout()
        }
    }

    override fun onRequestRelayout(layoutNode: LayoutNode) {
        if (measureAndLayoutDelegate.requestRelayout(layoutNode)) {
            scheduleMeasureAndLayout()
        }
    }

    override fun createLayer(
        drawBlock: (Canvas) -> Unit,
        invalidateParentLayer: () -> Unit
    ): OwnedLayer {
        return CALayerLayer(
            drawBlock
        )
    }

    override fun onSemanticsChange() {
//        accessibilityDelegate.onSemanticsChange()
    }

    override fun onLayoutChange(layoutNode: LayoutNode) {
//        accessibilityDelegate.onLayoutChange(layoutNode)

        println("onLayoutChange: $layoutNode - ${layoutNode.coordinates.size}")
    }

    override fun getFocusDirection(keyEvent: KeyEvent): FocusDirection? {
        return when (keyEvent.key) {
            Key.Tab -> if (keyEvent.isShiftPressed) Previous else Next
            Key.DirectionRight -> Right
            Key.DirectionLeft -> Left
            Key.DirectionUp -> Up
            Key.DirectionDown -> Down
            Key.DirectionCenter -> In
            Key.Back -> Out
            else -> null
        }
    }

    override fun requestRectangleOnScreen(rect: Rect) {
        TODO("Not yet implemented")
    }

    override fun sendKeyEvent(keyEvent: KeyEvent): Boolean {
        TODO()
//        return keyInputModifier.processKeyInput()
    }

    fun drawUIViewController(holder: UIKitControllerHolder<*>, nativeCanvas: NativeCanvas) {
        UIGraphicsPushContext(nativeCanvas.context)
        holder.controller.view.drawViewHierarchyInRect(view.bounds, true)
        UIGraphicsPopContext()
    }

    fun drawUIView(view: UIView, nativeCanvas: NativeCanvas) {
        UIGraphicsPushContext(nativeCanvas.context)
        view.drawViewHierarchyInRect(controller.view.bounds, true)
        UIGraphicsPopContext()
    }

    private val uiViewNodes = mutableMapOf<UIKitViewHolder<*>, LayoutNode>()
    private val controllerNodes = mutableMapOf<UIKitControllerHolder<*>, LayoutNode>()

    fun addUIViewController(holder: UIKitControllerHolder<*>, layoutNode: LayoutNode) {
        controllerNodes[holder] = layoutNode

        controller.addChildViewController(holder.controller)
        controller.view.addSubview(holder.controller.view)
        view.setFrame(controller.view.bounds)
//        view.apply {
//            translatesAutoresizingMaskIntoConstraints = false
//            listOf(
//                leadingAnchor.constraintEqualToAnchor(controller.view.leadingAnchor),
//                topAnchor.constraintEqualToAnchor(controller.view.topAnchor),
//                trailingAnchor.constraintEqualToAnchor(controller.view.trailingAnchor),
//                bottomAnchor.constraintEqualToAnchor(controller.view.bottomAnchor),
//            ).forEach {
//                it.active = true
//            }
//        }
        holder.controller.didMoveToParentViewController(controller)
    }

    fun removeUIViewController(holder: UIKitControllerHolder<*>) {
        holder.controller.willMoveToParentViewController(null)
        holder.controller.view.removeFromSuperview()
        holder.controller.removeFromParentViewController()

        controllerNodes.remove(holder)
    }

    fun addUIView(viewHolder: UIKitViewHolder<*>, layoutNode: LayoutNode) {
        uiViewNodes[viewHolder] = layoutNode
        view.addSubview(viewHolder)
        layoutNode.requestRemeasure()
    }

    fun removeUIView(viewHolder: UIKitViewHolder<*>) {
        viewHolder.removeFromSuperview()
        uiViewNodes.remove(viewHolder)
    }

    private var constraints: Constraints? = null

    private fun layoutUiViewNodes() {
        val constraints = constraints ?: view.bounds.useContents {
            Constraints(0, size.width.roundUpToInt(), 0, size.height.roundUpToInt())
        }

        measureAndLayoutDelegate.updateRootConstraints(constraints)
        measureAndLayoutDelegate.measureAndLayout()

        for (subview in view.subviews) {
            println("Subview frame: ${(subview as? UIView)?.frame?.useContents { "(${origin.x}, " +
                "${origin.y}, ${size.width}, ${size.height})" }}")
        }
    }

    private fun willLayoutSubviews() {
        for (childViewController in controller.childViewControllers) {
            if (childViewController is Controller) {
                childViewController.owner.constraints = constraints ?: view.bounds.useContents {
                    Constraints(0, size.width.roundUpToInt(), 0, size.height.roundUpToInt())
                }
            }
        }

        layoutUiViewNodes()
    }

    inner class Controller(internal val owner: UIKitComposeOwner): UIViewController(null, null) {

        override fun viewWillLayoutSubviews() {
            super.viewWillLayoutSubviews()
            willLayoutSubviews()
            println("viewWillLayoutSubviews()")
        }

        override fun viewDidLayoutSubviews() {
            super.viewDidLayoutSubviews()
            println("viewDidLayoutSubviews()")
        }

        override fun viewDidLoad() {
            super.viewDidLoad()
            println("viewDidLoad()")
        }

        override fun viewWillAppear(animated: Boolean) {
            super.viewWillAppear(animated)
            println("viewWillAppear(animated: $animated)")
        }

        override fun loadView() {
//            super.loadView()
            this@UIKitComposeOwner.delegate.loadView(this)
        }

        override fun isMovingToParentViewController(): Boolean {
            return super.isMovingToParentViewController().also {
                println("isMovingToParentViewController() = $it")
            }
        }

        override fun isMovingFromParentViewController(): Boolean {
            return super.isMovingFromParentViewController().also {
                println("isMovingFromParentViewController() = $it")
            }
        }

        override fun preferredContentSizeDidChangeForChildContentContainer(container: UIContentContainerProtocol) {
            super.preferredContentSizeDidChangeForChildContentContainer(container)
            println("preferredContentSizeDidChangeForChildContentContainer($container)")
        }

        override fun sizeForChildContentContainer(
            container: UIContentContainerProtocol,
            withParentContainerSize: CValue<CGSize>
        ): CValue<CGSize> {
            val knownContainer = container as? Controller ?:
                return super.sizeForChildContentContainer(container, withParentContainerSize)

            val constraints = withParentContainerSize.useContents {
                Constraints(
                    minWidth = 0,
                    maxWidth = width.roundUpToInt(),
                    minHeight = 0,
                    maxHeight = height.roundUpToInt(),
                )
            }
            val measureDelegate = knownContainer.owner.measureAndLayoutDelegate
            measureDelegate.updateRootConstraints(constraints)
            measureDelegate.measureAndLayout()
            val measured = knownContainer.owner.root
            return CGSizeMake(measured.width.toDouble(), measured.height.toDouble()).also {
                println("x sizeForChildContentContainer($container, ${withParentContainerSize
                    .useContents { "($width, $height)" }}) = ${it.useContents { "($width, $height)"
                }}")
            }

//            return super.sizeForChildContentContainer(container, withParentContainerSize).also {
//
//            }
        }

        override fun systemLayoutFittingSizeDidChangeForChildContentContainer(container: UIContentContainerProtocol) {
            super.systemLayoutFittingSizeDidChangeForChildContentContainer(container)
            println("systemLayoutFittingSizeDidChangeForChildContentContainer($container)")
        }
    }
}

internal class UIViewsHandler : UIView(CGRectZero.readValue()) {
    init {
        clipsToBounds = false
    }

    val holderToLayoutNode = hashMapOf<UIKitViewHolder<*>, LayoutNode>()
    val layoutNodeToHolder = hashMapOf<LayoutNode, UIKitViewHolder<*>>()

//    @ObjCAction
//    fun didMoveToSuperview() {
//        originalDidMoveToSuperview()
//    }

    @ObjCAction
    fun setNeedsLayout() {
        layoutNodeToHolder.keys.forEach { it.requestRemeasure() }
    }
//
//    @ObjCAction
//    fun setNeedsLayout()
//
//    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
//        // Layout was already handled by component nodes, but replace here because
//        // the View system has forced relayout on children. This method will only be called
//        // when forceLayout is called on the Views hierarchy.
//        holderToLayoutNode.keys.forEach { it.layout(it.left, it.top, it.right, it.bottom) }
//    }
//
//    // No call to super to avoid invalidating the AndroidComposeView and rely on
//    // component nodes logic.
//    @SuppressLint("MissingSuperCall")
//    override fun requestLayout() {
//        // Hack to cleanup the dirty layout flag on ourselves, such that this method continues
//        // to be called for further children requestLayout().
//        cleanupLayoutState(this)
//        // requestLayout() was called by a child, so we have to request remeasurement for
//        // their corresponding layout node.
//        for (i in 0 until childCount) {
//            val child = getChildAt(i)
//            val node = holderToLayoutNode[child]
//            if (child.isLayoutRequested && node != null) {
//                node.requestRemeasure()
//            }
//        }
//    }
}

class IosUriHandler(): UriHandler {
    override fun openUri(uri: String) {
        UIApplication.sharedApplication.openURL(NSURL(string = uri))
    }
}

interface UIViewControllerConvertible<T: UIViewController> {
    val viewController: T
}

abstract class BaseUIKitComposable(private val parent: CompositionContext? = null):
    UIViewControllerConvertible<UIViewController> {

    override val viewController: UIViewController by lazy {
        val owner = UIKitComposeOwner(delegateFunctions)
        owner.setContent(parent) {
            Content()
        }
        owner.controller
    }

    private val delegateFunctions: ControllerDelegateFunctions
        get() = ControllerDelegateFunctions(
            loadView = ::loadView,
        )

    @Composable
    protected abstract fun Content()

    protected open fun loadView(controller: UIViewController) {
        controller.view = UIView().apply {
            autoresizingMask = UIViewAutoresizingFlexibleWidth or UIViewAutoresizingFlexibleHeight
        }
    }
}

internal fun UIKitComposeOwner.setContent(
    parent: CompositionContext? = null,
    content: @Composable () -> Unit,
): Composition {
    GlobalSnapshotManager.ensureStarted()

    val parentContext = parent ?: run {
        val context = DefaultMonotonicFrameClock + Dispatchers.Main
        val recomposer = Recomposer(context)
        CoroutineScope(context).launch(start = CoroutineStart.UNDISPATCHED) {
            recomposer.runRecomposeAndApplyChanges()
        }
        recomposer
    }

    val owner = this
    val composition = Composition(DarwinUiApplier(owner.root), parentContext).also {
        composition = it
    }
    composition.setContent {
        ProvideUIKitCompositionLocals(owner) {
            content()
        }
    }
    return composition
}

//@Composable
//fun SubComposable(
//    subcontent: @Composable () -> Unit,
//) {
//    UIKitControllerHolder(
//
//    )
//}

internal val LocalUIKitComposeOwner = staticCompositionLocalOf<UIKitComposeOwner> {
    error("No owner set!")
}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
internal fun ProvideUIKitCompositionLocals(
    owner: UIKitComposeOwner,
    content: @Composable () -> Unit
) {
    val view = owner
//    var configuration by remember {
//        mutableStateOf(
//            context.resources.configuration,
//            neverEqualPolicy()
//        )
//    }

//    owner.configurationChangeObserver = { configuration = it }

    val uriHandler = remember { IosUriHandler() }


    CompositionLocalProvider(
        LocalUIKitComposeOwner provides owner,
        LocalParentController provides ParentController(
            owner.controller,
            willAddSubview = { },
            didAddSubview = { },
            owner.root,
        ),
    ) {
        ProvideCommonCompositionLocals(
            owner = owner,
            uriHandler = uriHandler,
            content = content
        )
    }
}

/**
 * Platform-specific mechanism for starting a monitor of global snapshot state writes
 * in order to schedule the periodic dispatch of snapshot apply notifications.
 * This process should remain platform-specific; it is tied to the threading and update model of
 * a particular platform and framework target.
 *
 * Composition bootstrapping mechanisms for a particular platform/framework should call
 * [ensureStarted] during setup to initialize periodic global snapshot notifications.
 */
@ThreadLocal
internal object GlobalSnapshotManager {
    private var started = false
    private var commitPending = false
    private var removeWriteObserver: (ObserverHandle)? = null

    private val scheduleScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    fun ensureStarted() {
        if (!started) {
            started = true
            removeWriteObserver = Snapshot.registerGlobalWriteObserver(globalWriteObserver)
        }
    }

    private val globalWriteObserver: (Any) -> Unit = {
        // Race, but we don't care too much if we end up with multiple calls scheduled.
        if (!commitPending) {
            commitPending = true
            schedule {
                commitPending = false
                Snapshot.sendApplyNotifications()
            }
        }
    }

    /**
     * List of deferred callbacks to run serially. Guarded by its own monitor lock.
     */
    private val scheduledCallbacks = mutableListOf<() -> Unit>()

    /**
     * Guarded by [scheduledCallbacks]'s monitor lock.
     */
    private var isSynchronizeScheduled = false

    /**
     * Synchronously executes any outstanding callbacks and brings snapshots into a
     * consistent, updated state.
     */
    private fun synchronize() {
        scheduledCallbacks.forEach { it.invoke() }
        scheduledCallbacks.clear()
        isSynchronizeScheduled = false
    }

    private fun schedule(block: () -> Unit) {
        scheduledCallbacks.add(block)
        if (!isSynchronizeScheduled) {
            isSynchronizeScheduled = true
            scheduleScope.launch { synchronize() }
        }
    }
}