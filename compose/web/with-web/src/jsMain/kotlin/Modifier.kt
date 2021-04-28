package org.jetbrains.ui.ww

import androidx.compose.ui.unit.ww.Dp
import androidx.core.graphics.ww.Color
import androidx.compose.web.css.backgroundColor
import androidx.compose.web.css.margin
import androidx.compose.web.css.px
import androidx.compose.web.css.Color.RGB
import org.jetbrains.compose.web.ww.internal.castOrCreate
import androidx.compose.web.css.StyleBuilder
import androidx.compose.web.attributes.AttrsBuilder

actual fun Modifier.background(color: Color): Modifier = castOrCreate().apply {
    add {
        backgroundColor(RGB(color.red, color.green, color.blue))
    }
}

fun Modifier.asStyleBuilderApplier(
    passThroughHandler: (StyleBuilder.() -> Unit)? = null
): StyleBuilder.() -> Unit = castOrCreate().let { modifier ->
    val st: StyleBuilder.() -> Unit = {
        modifier.styleHandlers.forEach { it.invoke(this) }
        passThroughHandler?.invoke(this)
    }

    st
}

fun Modifier.asAttributeBuilderApplier(
    passThroughHandler: (AttrsBuilder<*>.() -> Unit)? = null
): AttrsBuilder<*>.() -> Unit =
    castOrCreate().let { modifier ->
        val st: AttrsBuilder<*>.() -> Unit = {
            modifier.attrHandlers.forEach { it.invoke(this) }
            passThroughHandler?.invoke(this)
        }

        st
    }

actual fun Modifier.padding(all: Dp): Modifier = castOrCreate().apply {
    // yes, it's not a typo, what Modifier.padding does is actually adding marginEe
    add {
        margin(all.value.px)
    }
}