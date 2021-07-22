package co.touchlab.compose.impl

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun SimpleButtonActual(
    modifier: Modifier,
    title: String,
    onClick: () -> Unit,
)
