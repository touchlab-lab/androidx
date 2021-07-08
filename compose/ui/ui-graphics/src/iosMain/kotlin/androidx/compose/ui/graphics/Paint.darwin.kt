package androidx.compose.ui.graphics

import platform.CoreGraphics.CGContextRef
import platform.CoreGraphics.CGContextSetStrokeColorWithColor
import platform.darwin.NSObject

actual typealias NativePaint = DarwinPaint

actual fun Paint(): Paint = DarwinPaint()

class DarwinPaint(
    override var alpha: Float = DefaultAlpha,
    override var isAntiAlias: Boolean = true,
    override var color: Color = Color.Black,
    override var blendMode: BlendMode = BlendMode.SrcOver,
    override var style: PaintingStyle = PaintingStyle.Fill,
    override var strokeWidth: Float = 0f,
    override var strokeCap: StrokeCap = StrokeCap.Square,
    override var strokeJoin: StrokeJoin = StrokeJoin.Round,
    override var strokeMiterLimit: Float = 0f,
    override var filterQuality: FilterQuality = FilterQuality.None,
    override var shader: Shader? = null,
    override var colorFilter: ColorFilter? = null,
    override var pathEffect: PathEffect? = null,
) : Paint {
    override fun asFrameworkPaint(): NativePaint = this

    fun apply(context: CGContextRef?) {

    }
}