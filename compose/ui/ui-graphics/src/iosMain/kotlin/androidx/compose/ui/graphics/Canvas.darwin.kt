package androidx.compose.ui.graphics

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGAffineTransform
import platform.CoreGraphics.CGAffineTransformMake
import platform.CoreGraphics.CGAffineTransformScale
import platform.CoreGraphics.CGBitmapContextCreate
import platform.CoreGraphics.CGBitmapContextCreateWithData
import platform.CoreGraphics.CGContextAddEllipseInRect
import platform.CoreGraphics.CGContextAddLineToPoint
import platform.CoreGraphics.CGContextAddPath
import platform.CoreGraphics.CGContextAddRect
import platform.CoreGraphics.CGContextBeginTransparencyLayerWithRect
import platform.CoreGraphics.CGContextClearRect
import platform.CoreGraphics.CGContextClipToRect
import platform.CoreGraphics.CGContextConcatCTM
import platform.CoreGraphics.CGContextDrawPath
import platform.CoreGraphics.CGContextEndTransparencyLayer
import platform.CoreGraphics.CGContextMoveToPoint
import platform.CoreGraphics.CGContextRef
import platform.CoreGraphics.CGContextRestoreGState
import platform.CoreGraphics.CGContextRotateCTM
import platform.CoreGraphics.CGContextSaveGState
import platform.CoreGraphics.CGContextScaleCTM
import platform.CoreGraphics.CGContextTranslateCTM
import platform.CoreGraphics.CGPathDrawingMode
import platform.CoreGraphics.CGRectMake
import platform.QuartzCore.CATransform3D
import platform.QuartzCore.CATransform3DGetAffineTransform
import platform.QuartzCore.CATransform3DIdentity
import platform.posix.setpassent

actual class NativeCanvas(val context: CGContextRef?)

internal actual fun ActualCanvas(image: ImageBitmap): Canvas =
    DarwinCanvas().apply {
        internalCanvas = NativeCanvas(
            TODO()
        )
    }

fun Canvas(contextRef: CGContextRef?): Canvas =
    DarwinCanvas().apply { internalCanvas = NativeCanvas(contextRef) }

actual val Canvas.nativeCanvas: NativeCanvas
    get() = (this as DarwinCanvas).internalCanvas

private val EmptyCanvas = NativeCanvas(null)

class CanvasHolder {
    @PublishedApi internal val darwinCanvas = DarwinCanvas()

    inline fun drawInto(canvas: NativeCanvas, block: Canvas.() -> Unit) {
        val previousCanvas = darwinCanvas.internalCanvas
        darwinCanvas.internalCanvas = canvas
        darwinCanvas.block()
        darwinCanvas.internalCanvas = previousCanvas
    }
}

@PublishedApi internal class DarwinCanvas: Canvas {
    @PublishedApi
    internal var internalCanvas: NativeCanvas = EmptyCanvas

    private val context: CGContextRef?
        get() = internalCanvas.context

    override fun save() {
        CGContextSaveGState(context)
    }

    override fun restore() {
        CGContextEndTransparencyLayer(context)
        CGContextRestoreGState(context)
    }

    override fun saveLayer(bounds: Rect, paint: Paint) {
        CGContextBeginTransparencyLayerWithRect(context,
            CGRectMake(
                x = bounds.left.toDouble(),
                y = bounds.top.toDouble(),
                width = bounds.width.toDouble(),
                height = bounds.height.toDouble(),
            ),
            null
        )
    }

    override fun translate(dx: Float, dy: Float) {
        CGContextTranslateCTM(context, dx.toDouble(), dy.toDouble())
    }

    override fun scale(sx: Float, sy: Float) {
        CGContextScaleCTM(context, sx.toDouble(), sy.toDouble())
    }

    override fun rotate(degrees: Float) {
        CGContextRotateCTM(context, degrees.toDouble())
    }

    override fun skew(sx: Float, sy: Float) {
        TODO("Not yet implemented")
    }

    override fun concat(matrix: Matrix) {
        val transform = CATransform3DIdentity
        transform.m11 = matrix[0, 0].toDouble()
        transform.m12 = matrix[0, 1].toDouble()
        transform.m13 = matrix[0, 2].toDouble()
        transform.m14 = matrix[0, 3].toDouble()

        transform.m21 = matrix[1, 0].toDouble()
        transform.m22 = matrix[1, 1].toDouble()
        transform.m23 = matrix[1, 2].toDouble()
        transform.m24 = matrix[1, 3].toDouble()

        transform.m31 = matrix[2, 0].toDouble()
        transform.m32 = matrix[2, 1].toDouble()
        transform.m33 = matrix[2, 2].toDouble()
        transform.m34 = matrix[2, 3].toDouble()

        transform.m41 = matrix[3, 0].toDouble()
        transform.m42 = matrix[3, 1].toDouble()
        transform.m43 = matrix[3, 2].toDouble()
        transform.m44 = matrix[3, 3].toDouble()

        CGContextConcatCTM(context, CATransform3DGetAffineTransform(transform.readValue()))
    }

    override fun clipRect(left: Float, top: Float, right: Float, bottom: Float, clipOp: ClipOp) {
        val rect = CGRectMake(
            left.toDouble(),
            top.toDouble(),
            (right - left).toDouble(),
            (bottom - top).toDouble(),
        )
        when (clipOp) {
            ClipOp.Difference -> CGContextClearRect(context, rect)
            ClipOp.Intersect -> CGContextClipToRect(context, rect)
            else -> { }
        }
    }

    override fun clipPath(path: Path, clipOp: ClipOp) {
        TODO("Not yet implemented")
    }

    override fun drawLine(p1: Offset, p2: Offset, paint: Paint) {
        CGContextMoveToPoint(context, p1.x.toDouble(), p2.x.toDouble())
        CGContextAddLineToPoint(context, p2.x.toDouble(), p2.y.toDouble())
        drawCurrentPath(paint)
    }

    override fun drawRect(left: Float, top: Float, right: Float, bottom: Float, paint: Paint) {
        val rect = CGRectMake(
            left.toDouble(),
            top.toDouble(),
            (right - left).toDouble(),
            (bottom - top).toDouble(),
        )
        CGContextAddRect(context, rect)
        drawCurrentPath(paint)
    }

    override fun drawRoundRect(left: Float, top: Float, right: Float, bottom: Float, radiusX: Float, radiusY: Float, paint: Paint) {
        TODO("Not yet implemented")
    }

    override fun drawOval(left: Float, top: Float, right: Float, bottom: Float, paint: Paint) {
        TODO("Not yet implemented")
    }

    override fun drawCircle(center: Offset, radius: Float, paint: Paint) {
        TODO("Not yet implemented")
    }

    override fun drawArc(left: Float, top: Float, right: Float, bottom: Float, startAngle: Float, sweepAngle: Float, useCenter: Boolean, paint: Paint) {
        TODO("Not yet implemented")
    }

    override fun drawPath(path: Path, paint: Paint) {
        TODO("Not yet implemented")
    }

    override fun drawImage(image: ImageBitmap, topLeftOffset: Offset, paint: Paint) {
        TODO("Not yet implemented")
    }

    override fun drawImageRect(image: ImageBitmap, srcOffset: IntOffset, srcSize: IntSize, dstOffset: IntOffset, dstSize: IntSize, paint: Paint) {
        TODO("Not yet implemented")
    }

    override fun drawPoints(pointMode: PointMode, points: List<Offset>, paint: Paint) {
        TODO("Not yet implemented")
    }

    override fun drawRawPoints(pointMode: PointMode, points: FloatArray, paint: Paint) {
        TODO("Not yet implemented")
    }

    override fun drawVertices(vertices: Vertices, blendMode: BlendMode, paint: Paint) {
        TODO("Not yet implemented")
    }

    override fun enableZ() {
        TODO("Not yet implemented")
    }

    override fun disableZ() {
        TODO("Not yet implemented")
    }

    private fun drawCurrentPath(paint: Paint) {
        paint.asFrameworkPaint().apply(context)

        val mode: CGPathDrawingMode = CGPathDrawingMode.kCGPathFill
        CGContextDrawPath(context, mode)
    }
}