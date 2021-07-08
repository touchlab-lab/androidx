package androidx.compose.ui.platform

import kotlin.native.concurrent.AtomicInt as KAtomicInt

actual class AtomicInt actual constructor(value_: Int) {
    private val reference = KAtomicInt(value_)

    actual fun addAndGet(delta: Int): Int {
        return reference.addAndGet(delta)
    }

    actual fun compareAndSet(expected: Int, new: Int): Boolean {
        return reference.compareAndSet(expected, new)
    }
}