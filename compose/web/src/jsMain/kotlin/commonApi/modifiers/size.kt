package org.jetbrains.ui.ww

import androidx.compose.ui.unit.ww.Dp
import androidx.compose.web.css.width
import androidx.compose.web.css.height
import androidx.compose.web.css.px
import org.jetbrains.compose.web.ww.internal.castOrCreate

actual fun Modifier.size(width: Dp, height: Dp): Modifier = castOrCreate().apply {
    add {
        width(width.value.px)
        height(height.value.px)
    }
}