package co.touchlab.compose.impl

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.UIKitView
import androidx.compose.ui.unit.TextUnit
import co.touchlab.compose.toUIColor
import platform.UIKit.UILabel
import platform.UIKit.setNeedsLayout

@Composable
actual fun TextActual(
    text: String,
    modifier: Modifier,
    color: Color,
    size: TextUnit
) {
    UIKitView(
        factory = { UILabel() },
        modifier = modifier,
        update = { label ->
            if (label.text != text) {
                label.text = text
                requestRemeasure()
            }
            label.textColor = color.toUIColor()
            if (label.font.pointSize != size.value.toDouble()) {
                label.font = label.font.fontWithSize(size.value.toDouble())
                requestRemeasure()
            }
//            modifier.castOrCreate().modHandlers.forEach { block -> block.invoke(label) }
        }
    )
}