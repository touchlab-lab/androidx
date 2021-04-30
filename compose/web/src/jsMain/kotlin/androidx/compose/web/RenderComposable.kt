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

package androidx.compose.web

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composition
import androidx.compose.runtime.ControlledComposition
import androidx.compose.runtime.DefaultMonotonicFrameClock
import androidx.compose.runtime.Recomposer
import androidx.compose.web.elements.DOMScope
import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.HTMLBodyElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.get

/**
 * Use this method to mount the composition at the certain [root]
 *
 * @param root - the [HTMLElement] that will be the root of the DOM tree managed by Compose
 * @param content - the Composable lambda that defines the composition content
 *
 * @return the instance of the [Composition]
 */
fun <THTMLElement : HTMLElement> renderComposable(
    root: THTMLElement,
    content: @Composable DOMScope<THTMLElement>.() -> Unit
): Composition {
    GlobalSnapshotManager.ensureStarted()

    val context = DefaultMonotonicFrameClock + Dispatchers.Main
    val recomposer = Recomposer(context)
    val composition = ControlledComposition(
        applier = DomApplier(DomNodeWrapper(root)),
        parent = recomposer
    )
    val scope = object : DOMScope<THTMLElement> {}
    composition.setContent @Composable {
        content(scope)
    }

    CoroutineScope(context).launch(start = CoroutineStart.UNDISPATCHED) {
        recomposer.runRecomposeAndApplyChanges()
    }
    return composition
}

/**
 * Use this method to mount the composition at the element with id - [rootElementId].
 *
 * @param rootElementId - the id of the [HTMLElement] that will be the root of the DOM tree managed
 * by Compose
 * @param content - the Composable lambda that defines the composition content
 *
 * @return the instance of the [Composition]
 */
@Suppress("UNCHECKED_CAST")
fun renderComposable(
    rootElementId: String,
    content: @Composable DOMScope<HTMLElement>.() -> Unit
): Composition = renderComposable(
    root = document.getElementById(rootElementId) as HTMLElement,
    content = content
)

/**
 * Use this method to mount the composition at the [HTMLBodyElement] of the current document
 *
 * @param content - the Composable lambda that defines the composition content
 *
 * @return the instance of the [Composition]
 */
fun renderComposableInBody(
    content: @Composable DOMScope<HTMLBodyElement>.() -> Unit
): Composition = renderComposable(
    root = document.getElementsByTagName("body")[0] as HTMLBodyElement,
    content = content
)