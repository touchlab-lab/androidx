package org.jetbrains.ui.ww

import androidx.compose.ui.unit.ww.Dp
import androidx.compose.ui.unit.ww.implementation
import androidx.compose.ui.Modifier as JModifier
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.background
import androidx.core.graphics.ww.Color
import androidx.core.graphics.ww.implementation

private class ModifierElement : JModifier.Element

private class ActualModifier : Modifier {
    var modifier: JModifier = ModifierElement()
}

private fun Modifier.castOrCreate(): ActualModifier = (this as? ActualModifier) ?: ActualModifier()

actual fun Modifier.size(size: Dp): Modifier = castOrCreate().apply {
    modifier = modifier.size(size.implementation)
}

actual fun Modifier.background(color: Color): Modifier = castOrCreate().apply {
    modifier = modifier.background(color.implementation)
}

val Modifier.implementation
    get() = castOrCreate().modifier