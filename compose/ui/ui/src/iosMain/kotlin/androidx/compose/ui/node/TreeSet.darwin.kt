package androidx.compose.ui.node

import platform.Foundation.NSMutableOrderedSet
import platform.Foundation.addObject
import platform.Foundation.containsObject
import platform.Foundation.firstObject
import platform.Foundation.removeObject

internal actual class TreeSet<E> actual constructor(comparator: Comparator<in E>) {
    private val backing = NSMutableOrderedSet()

    actual fun add(element: E): Boolean {
        return if (contains(element)) {
            false
        } else {
            backing.addObject(element)
            true
        }
    }

    actual fun remove(element: E): Boolean {
        return if (contains(element)) {
            backing.removeObject(element)
            true
        } else {
            false
        }
    }

    actual fun first(): E {
        return backing.firstObject as E
    }

    actual fun contains(element: E): Boolean = backing.containsObject(element)

    actual fun isEmpty(): Boolean = backing.count == 0UL
}
