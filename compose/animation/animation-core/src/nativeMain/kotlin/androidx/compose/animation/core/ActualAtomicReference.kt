// ktlint-disable filename

/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:Suppress("UNUSED_PARAMETER")
package androidx.compose.animation.core

class NativeAtomicReference<V>(value: V) {
    private var v: V = value

    fun get(): V = v

    fun set(value: V) {
        v = value
    }

    fun getAndSet(value: V): V {
        val returnV = v
        v = value
        return returnV!!
    }

    fun compareAndSet(expect: V, newValue: V): Boolean {
        if (v == expect) {
            v = newValue
            return true
        }
        return false
    }
}

internal actual typealias AtomicReference<V> = androidx.compose.runtime.AtomicReference<V>

