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

private fun Modifier.castOrCreate(): ActualModifier = (this as? ActualModifier) ?: ActualModifier()

actual fun Modifier.size(size: Dp): Modifier = castOrCreate().apply {
    styleHandlers.add({
        width(size.value.px)
        height(size.value.px)
    })
}

actual fun Modifier.background(color: Color): Modifier = castOrCreate().apply {
    styleHandlers.add({
        backgroundColor(RGB(color.red, color.green, color.blue))
    })
}

fun Modifier.asStyleBuilderApplier(): StyleBuilder.() -> Unit = castOrCreate().let { modifier ->
    val st: StyleBuilder.() -> Unit = {
        modifier.styleHandlers.forEach { it.invoke(this) }
    }

    st
}