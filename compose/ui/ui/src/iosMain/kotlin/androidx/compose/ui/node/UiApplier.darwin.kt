package androidx.compose.ui.node

import androidx.compose.runtime.AbstractApplier
import androidx.compose.ui.platform.UIKitComposeOwner

internal class DarwinUiApplier(
    root: LayoutNode
) : AbstractApplier<LayoutNode>(root) {

    override fun insertTopDown(index: Int, instance: LayoutNode) {
        // Ignored. Insert is performed in [insertBottomUp] to build the tree bottom-up to avoid
        // duplicate notification when the child nodes enter the tree.
    }

    override fun insertBottomUp(index: Int, instance: LayoutNode) {
        current.insertAt(index, instance)
    }

    override fun remove(index: Int, count: Int) {
        current.removeAt(index, count)
    }

    override fun move(from: Int, to: Int, count: Int) {
        current.move(from, to, count)
    }

    override fun onClear() {
        root.removeAll()
    }

    override fun onEndChanges() {
        super.onEndChanges()
        (root.owner as? UIKitComposeOwner)?.clearInvalidObservations()
    }
}