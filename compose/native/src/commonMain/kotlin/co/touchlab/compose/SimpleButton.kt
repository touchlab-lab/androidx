package co.touchlab.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import co.touchlab.compose.impl.SimpleButtonActual

@Composable
fun SimpleButton(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit,
) {
    SimpleButtonActual(modifier, title, onClick)
}
