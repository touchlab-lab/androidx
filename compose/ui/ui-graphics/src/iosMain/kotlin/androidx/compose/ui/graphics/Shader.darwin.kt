package androidx.compose.ui.graphics

import androidx.compose.ui.geometry.Offset
import platform.darwin.NSObject

actual typealias Shader = NSObject


internal actual fun ActualLinearGradientShader(
        from: Offset,
        to: Offset,
        colors: List<Color>,
        colorStops: List<Float>?,
        tileMode: TileMode
): Shader {
    TODO()
}

internal actual fun ActualRadialGradientShader(
        center: Offset,
        radius: Float,
        colors: List<Color>,
        colorStops: List<Float>?,
        tileMode: TileMode
): Shader {
    TODO()
}

internal actual fun ActualSweepGradientShader(
        center: Offset,
        colors: List<Color>,
        colorStops: List<Float>?
): Shader {
    TODO()
}

internal actual fun ActualImageShader(
        image: ImageBitmap,
        tileModeX: TileMode,
        tileModeY: TileMode
): Shader {
    TODO()
}
