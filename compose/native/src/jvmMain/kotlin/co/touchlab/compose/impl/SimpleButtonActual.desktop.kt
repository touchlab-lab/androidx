package co.touchlab.compose.impl

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import co.touchlab.compose.Button
import androidx.compose.material.Text as JText

@Composable
actual fun SimpleButtonActual(
    modifier: Modifier,
    title: String,
    onClick: () -> Unit,
) {
    Button(modifier, onClick) {
        JText(title, modifier)
    }
}