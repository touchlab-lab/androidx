package co.touchlab.compose

import androidx.compose.ui.graphics.Color
import platform.UIKit.UIColor

fun Color.toUIColor(): UIColor = UIColor(
    red = red.toDouble() / 255F,
    green = green.toDouble() / 255F,
    blue = blue.toDouble() / 255F,
    alpha = alpha.toDouble(),
)
