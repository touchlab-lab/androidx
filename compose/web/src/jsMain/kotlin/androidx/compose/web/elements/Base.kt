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

package androidx.compose.web.elements

import androidx.compose.web.DomApplier
import androidx.compose.web.DomNodeWrapper
import androidx.compose.web.attributes.AttrsBuilder
import androidx.compose.web.attributes.StyleBuilder
import androidx.compose.runtime.Applier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.DisposableEffectResult
import androidx.compose.runtime.DisposableEffectScope
import androidx.compose.runtime.ExplicitGroupsComposable
import androidx.compose.runtime.SkippableUpdater
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.remember
import kotlinx.browser.document
import org.w3c.dom.HTMLElement

@OptIn(ComposeCompilerApi::class)
@Composable
@ExplicitGroupsComposable
inline fun <T, reified E : Applier<*>> ComposeDomNode(
    noinline factory: () -> T,
    elementScope: ElementScope,
    noinline attrsSkippableUpdate: @Composable SkippableUpdater<T>.() -> Unit,
    noinline styleSkippableUpdate: @Composable SkippableUpdater<T>.() -> Unit,
    content: @Composable ElementScope.() -> Unit
) {
    if (currentComposer.applier !is E) error("Invalid applier")
    currentComposer.startNode()
    if (currentComposer.inserting) {
        currentComposer.createNode(factory)
    } else {
        currentComposer.useNode()
    }
//    Updater<T>(currentComposer).update()
    SkippableUpdater<T>(currentComposer).apply {
        attrsSkippableUpdate()
        styleSkippableUpdate()
    }
    currentComposer.startReplaceableGroup(0x7ab4aae9)
    content(elementScope)
    currentComposer.endReplaceableGroup()
    currentComposer.endNode()
}

class DisposableEffectHolder(
    var effect: (DisposableEffectScope.(HTMLElement) -> DisposableEffectResult)? = null
)

@Composable
inline fun <T : Any> TagElement(
    tagName: String,
    crossinline applyAttrs: AttrsBuilder<T>.() -> Unit = {},
    crossinline applyStyle: StyleBuilder.() -> Unit = {},
    content: @Composable ElementScope.() -> Unit = {}
) {

    val scope = remember { ElementScopeImpl() }
    val refEffect = remember { DisposableEffectHolder() }

    ComposeDomNode<DomNodeWrapper, DomApplier>(
        factory = {
            DomNodeWrapper(document.createElement(tagName)).also {
                scope.element = it.node as HTMLElement
            }
        },
        attrsSkippableUpdate = {
            val attrsApplied = AttrsBuilder<T>().also { it.applyAttrs() }
            refEffect.effect = attrsApplied.refEffect
            val attrsCollected = attrsApplied.collect()
            val events = attrsApplied.asList()

            update {
                set(attrsCollected, DomNodeWrapper.UpdateAttrs)
                set(events, DomNodeWrapper.UpdateListeners)
                set(attrsApplied.propertyUpdates, DomNodeWrapper.UpdateProperties)
            }
        },
        styleSkippableUpdate = {
            val style = StyleBuilder().apply(applyStyle).cssText
            update {
                set(style, DomNodeWrapper.UpdateStyle)
            }
        },
        elementScope = scope,
        content = content
    )

    DisposableEffect(null) {
        refEffect.effect?.invoke(this, scope.element) ?: onDispose {}
    }
}
