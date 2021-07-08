package androidx.compose.ui.graphics

import platform.darwin.NSObject

internal actual typealias NativeColorFilter = NSObject

internal actual fun actualTintColorFilter(color: Color, blendMode: BlendMode): ColorFilter {
    TODO()
}

internal actual fun actualColorMatrixColorFilter(colorMatrix: ColorMatrix): ColorFilter =
    TODO()

internal actual fun actualLightingColorFilter(multiply: Color, add: Color): ColorFilter =
    TODO()
