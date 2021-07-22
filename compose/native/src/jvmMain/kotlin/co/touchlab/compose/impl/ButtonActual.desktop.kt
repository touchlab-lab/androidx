package co.touchlab.compose.impl

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material.Button as JButton

@Composable
actual fun ButtonActual(
    modifier: Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    JButton(onClick, modifier) {
        content()
    }
}