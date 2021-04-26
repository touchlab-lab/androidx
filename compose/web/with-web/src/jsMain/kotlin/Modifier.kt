package org.jetbrains.ui.ww

import androidx.compose.ui.unit.ww.Dp
import androidx.core.graphics.ww.Color
import androidx.compose.web.css.backgroundColor
import androidx.compose.web.css.width
import androidx.compose.web.css.height
import androidx.compose.web.css.margin
import androidx.compose.web.css.px
import androidx.compose.web.css.Color.RGB
import org.jetbrains.compose.web.ww.internal.castOrCreate
import androidx.compose.web.css.StyleBuilder

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

fun Modifier.asStyleBuilderApplier(
    followingHandler: (StyleBuilder.() -> Unit)? = null
): StyleBuilder.() -> Unit = castOrCreate().let { modifier ->
    val st: StyleBuilder.() -> Unit = {
        modifier.styleHandlers.forEach { it.invoke(this) }
        followingHandler?.invoke(this)
    }

    st
}

actual fun Modifier.padding(all: Dp): Modifier = castOrCreate().apply {
    // yes, it's not a typo, what Modifier.padding does is actually adding margin
    styleHandlers.add({
        margin(all.value.px)
    })
}