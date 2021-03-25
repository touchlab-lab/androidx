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

package androidx.compose.web.events

import androidx.compose.web.MppModifier
import org.w3c.dom.events.EventListener

/**
 * Adds an event listener for the target node which is applied on node recomposition
 */
internal class EventModifier(
    val eventName: String,
    val listener: EventListener
) : MppModifier.Element

fun MppModifier.event(eventName: String, listener: EventListener): MppModifier =
    this.then(EventModifier(eventName, listener))

fun MppModifier.onClick(action: (WrappedEvent) -> Unit): MppModifier =
    this.then(
        EventModifier(
            "click",
            EventListener {
                action(
                    WrappedEventImpl(it)
                )
            }
        )
    )
