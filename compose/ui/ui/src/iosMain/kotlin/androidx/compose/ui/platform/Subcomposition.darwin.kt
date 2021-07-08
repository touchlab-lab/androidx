package androidx.compose.ui.platform

import androidx.compose.runtime.Composition
import androidx.compose.runtime.CompositionContext
import androidx.compose.ui.node.LayoutNode
import androidx.compose.ui.node.DarwinUiApplier

internal actual fun createSubcomposition(
    container: LayoutNode,
    parent: CompositionContext
): Composition = Composition(
    DarwinUiApplier(container),
    parent
)