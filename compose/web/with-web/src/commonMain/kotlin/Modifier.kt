package org.jetbrains.ui.ww

import androidx.compose.ui.unit.ww.Dp
import androidx.core.graphics.ww.Color

interface Modifier {
    open class Element : Modifier
    companion object : Element()
}

expect fun Modifier.size(size: Dp): Modifier
expect fun Modifier.background(color: Color): Modifier