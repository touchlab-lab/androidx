package androidx.compose.ui.input.pointer

/**
 * Describes a pointer input change event that has occurred at a particular point in time.
 */
actual data class PointerEvent actual constructor(
    /**
     * The changes.
     */
    actual val changes: List<PointerInputChange>
) {
    internal actual constructor(
        changes: List<PointerInputChange>,
        internalPointerEvent: InternalPointerEvent?
    ) : this(changes)
}