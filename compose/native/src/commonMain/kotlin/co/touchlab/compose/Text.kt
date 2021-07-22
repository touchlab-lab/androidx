package co.touchlab.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import co.touchlab.compose.impl.TextActual

@Composable
fun Text(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: TextUnit = TextUnit.Unspecified,
) {
    TextActual(text, modifier, color, size)
}