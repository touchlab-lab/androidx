package co.touchlab.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import co.touchlab.compose.impl.ButtonActual

@Composable
fun Button(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    ButtonActual(modifier, onClick, content)
}