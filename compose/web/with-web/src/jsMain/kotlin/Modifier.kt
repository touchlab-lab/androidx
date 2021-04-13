package org.jetbrains.ui.ww

import androidx.compose.ui.unit.ww.Dp
import androidx.core.graphics.ww.Color
import androidx.compose.web.css.StyleBuilder
import androidx.compose.web.css.backgroundColor
import androidx.compose.web.css.width
import androidx.compose.web.css.height
import androidx.compose.web.css.px
import androidx.compose.web.css.Color.RGB

private class ActualModifier : Modifier {
    val styleHandlers = mutableListOf<StyleBuilder.() -> Unit>()
}

actual fun Modifier.size(size: Dp) = when (this) {
    is ActualModifier -> size(size)
    else -> ActualModifier().size(size)
}

private fun ActualModifier.size(size: Dp): Modifier {
    styleHandlers.add({
        width(size.value.px)
        height(size.value.px)
    })
    return this
}

actual fun Modifier.background(color: Color) = when (this) {
    is ActualModifier -> background(color)
    else -> ActualModifier().background(color)
}

private fun ActualModifier.background(color: Color): Modifier {
    styleHandlers.add({
        backgroundColor(RGB(color.red, color.green, color.blue))
    })
    return this
}

fun Modifier.asStyleBuilderApplier(): StyleBuilder.() -> Unit = when (this) {
    is ActualModifier -> asStyleBuilderApplier()
    else -> ActualModifier().asStyleBuilderApplier()
}

private fun ActualModifier.asStyleBuilderApplier(): StyleBuilder.() -> Unit {
    val st: StyleBuilder.() -> Unit = {
        styleHandlers.forEach { it.invoke(this) }
    }

    return st
}