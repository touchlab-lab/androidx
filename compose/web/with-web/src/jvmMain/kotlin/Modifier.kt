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

actual fun Modifier.size(size: Dp) = when (this) {
    is ActualModifier -> size(size)
    else -> ActualModifier().size(size)
}

private fun ActualModifier.size(size: Dp): Modifier {
    modifier = modifier.size(size.implementation)
    return this
}

actual fun Modifier.background(color: Color) = when (this) {
    is ActualModifier -> background(color)
    else -> ActualModifier().background(color)
}

private fun ActualModifier.background(color: Color): Modifier {
    modifier = modifier.background(color.implementation)
    return this
}

val Modifier.implementation
    get() = ((this as? ActualModifier) ?: ActualModifier()).modifier