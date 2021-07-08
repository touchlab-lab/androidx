package androidx.compose.runtime

import androidx.compose.runtime.snapshots.SnapshotMutableState
import kotlinx.coroutines.delay
import platform.QuartzCore.CACurrentMediaTime
import kotlin.native.concurrent.AtomicReference as KAtomicReference
import platform.Foundation.NSThread
import platform.Foundation.NSUUID
import kotlin.native.identityHashCode
import kotlin.native.concurrent.freeze

internal actual object Trace {
    actual fun beginSection(name: String): Any? {
        // Do nothing.
        return null
    }

    actual fun endSection(token: Any?) {
        // Do nothing.
    }
}

// TODO(igotti): do we need actual processing for those?
actual annotation class CheckResult(actual val suggest: String)

/**
 * Clock with fixed delay between frames (16ms), independent from any display/window.
 *
 * It is used by [withFrameNanos] and [withFrameMillis] if one is not present
 * in the calling [kotlin.coroutines.CoroutineContext].
 *
 * Use it only where you don't need to show animation in a window.
 *
 * If you need a frame clock for changing the state of an animation that should be displayed to
 * user, use [MonotonicFrameClock] that is bound to the current window. You can access it using
 * [LaunchedEffect]:
 * ```
 * LaunchedEffect {
 *   val frameClock = coroutineContext[MonotonicFrameClock]
 * }
 * ```
 *
 * Or using [rememberCoroutineScope]:
 * ```
 * val scope = rememberCoroutineScope()
 * val frameClock = scope.coroutineContext[MonotonicFrameClock]
 * ```
 *
 * If [withFrameNanos] / [withFrameMillis] runs inside the coroutine scope
 * obtained using [LaunchedEffect] or [rememberCoroutineScope] they also use
 * [MonotonicFrameClock] which is bound to the current window.
 */
@Deprecated(
        "MonotonicFrameClocks are not globally applicable across platforms. " +
                "Use an appropriate local clock."
)
actual val DefaultMonotonicFrameClock: MonotonicFrameClock get() = SixtyFpsMonotonicFrameClock

private object SixtyFpsMonotonicFrameClock : MonotonicFrameClock {
    private const val fps = 60

    override suspend fun <R> withFrameNanos(
            onFrame: (Long) -> R
    ): R {
        delay(1000L / fps)
        return onFrame((CACurrentMediaTime() / 1_000_000_000.0).toLong())
    }
}

internal actual fun <T> createSnapshotMutableState(
        value: T,
        policy: SnapshotMutationPolicy<T>
): SnapshotMutableState<T> = SnapshotMutableStateImpl(value, policy)

actual class AtomicReference<V> actual constructor(value: V) {
    private var value = value

    actual fun get(): V = value

    actual fun set(value: V) {
        this.value = value
    }

    actual fun getAndSet(value: V): V {
        val oldValue = this.value
        this.value = value
        return oldValue
    }

    actual fun compareAndSet(expect: V, newValue: V): Boolean = if (value == expect) {
        value = newValue
        true
    } else {
        false
    }
}

//internal actual typealias AtomicReference<V> = kotlin.native.concurrent.AtomicReference<V>

internal actual open class ThreadLocal<T> actual constructor(
        private val initialValue: () -> T
) {
    private val key = NSUUID.UUID()

    @Suppress("UNCHECKED_CAST")
    actual fun get(): T {
        val threadDictionary = NSThread.currentThread.threadDictionary
        val existingObject = threadDictionary.objectForKey(key) as? T
        return if (existingObject != null) {
            existingObject
        } else {
            val newObject = initialValue()
            set(newObject)
            newObject
        }
    }

    actual fun set(value: T) {
        if (value != null) {
            NSThread.currentThread.threadDictionary.setObject(value, key)
        } else {
            NSThread.currentThread.threadDictionary.removeObjectForKey(key)
        }
    }
}

//internal actual class SnapshotThreadLocal<T> {
//    private val map = AtomicReference<ThreadMap>(emptyThreadMap)
//    private val writeMutex = Any()
//
//    @Suppress("UNCHECKED_CAST")
//    actual fun get(): T? = map.get().get(Thread.currentThread().id) as T?
//
//    actual fun set(value: T?) {
//        val key = Thread.currentThread().id
//        synchronized(writeMutex) {
//            val current = map.get()
//            if (current.trySet(key, value)) return
//            map.set(current.newWith(key, value))
//        }
//    }
//}


internal actual class SnapshotThreadLocal<T> {
    private val impl = ThreadLocal<T?> { null }

    @Suppress("UNCHECKED_CAST")
    actual fun get(): T? = impl.get()

    actual fun set(value: T?) = impl.set(value)
}

internal actual fun identityHashCode(instance: Any?): Int = instance.identityHashCode()

actual inline fun <R> synchronized(lock: Any, block: () -> R): R {
    return block()
}

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@Target(
        AnnotationTarget.FUNCTION,
        AnnotationTarget.CONSTRUCTOR,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER
)
actual annotation class TestOnly