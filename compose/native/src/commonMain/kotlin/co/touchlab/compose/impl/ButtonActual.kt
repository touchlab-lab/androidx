package co.touchlab.compose.impl

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun ButtonActual(
    modifier: Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit
)
