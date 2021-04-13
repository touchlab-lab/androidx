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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.web.elements.Div
import androidx.compose.web.renderComposable
import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.promise
import kotlinx.dom.clear
import org.w3c.dom.HTMLElement
import kotlin.test.Test
import kotlin.test.assertEquals

private fun String.asHtmlElement() = document.createElement("div") as HTMLElement

class DomSideEffectTests {

    @Test
    fun canCreateElementsInDomSideEffect() {
        val root = "div".asHtmlElement()
        renderComposable(
            root = root
        ) {
            Div {
                DomSideEffect {
                    it.appendChild(
                        document.createElement("p").also {
                            it.appendChild(document.createTextNode("Hello World!"))
                        }
                    )
                }
            }
        }
        assertEquals(
            expected = "<div><div style=\"\"><p>Hello World!</p></div></div>",
            actual = root.outerHTML
        )
    }

    private val testScope = MainScope()

    private fun runBlockingTest(
        block: suspend CoroutineScope.() -> Unit
    ): dynamic = testScope.promise { this.block() }

    @Test
    fun canUpdateElementsCreatedInDomSideEffect() = runBlockingTest {
        val root = "div".asHtmlElement()

        var i: Int by mutableStateOf(0)
        val disposeCalls = mutableListOf<Int>()

        @Composable
        fun CustomDiv(value: Int) {
            Div {
                DomSideEffect(value) {
                    it.appendChild(
                        it.appendChild(document.createTextNode("Value = $value"))
                    )
                    onDispose {
                        disposeCalls.add(value)
                        it.clear()
                    }
                }
            }
        }

        renderComposable(
            root = root
        ) {
            CustomDiv(i)
        }
        assertEquals(
            expected = "<div><div style=\"\">Value = 0</div></div>",
            actual = root.outerHTML
        )

        i = 1

        delay(1) // to let the composition recompose before we make assertions
        assertEquals(
            expected = 1,
            actual = disposeCalls.size,
            message = "Only one onDispose call expected"

        )
        assertEquals(
            expected = 0,
            actual = disposeCalls[0],
            message = "onDispose should be called with a previous value"
        )
        assertEquals(
            expected = "<div><div style=\"\">Value = 1</div></div>",
            actual = root.outerHTML
        )
    }

    @Test
    fun onDisposeIsCalledWhenComposableRemovedFromComposition() = runBlockingTest {
        val root = "div".asHtmlElement()

        var showDiv: Boolean by mutableStateOf(true)
        var onDisposeCalledTimes = 0

        renderComposable(
            root = root
        ) {
            if (showDiv) {
                Div {
                    DomSideEffect {
                        it.appendChild(document.createTextNode("Goedemorgen!"))
                        onDispose { onDisposeCalledTimes++ }
                    }
                }
            }
        }
        assertEquals(
            expected = "<div><div style=\"\">Goedemorgen!</div></div>",
            actual = root.outerHTML
        )

        showDiv = false

        delay(1) // to let the composition recompose before we make assertions
        assertEquals(1, onDisposeCalledTimes)
        assertEquals(
            expected = "<div></div>",
            actual = root.outerHTML
        )
    }
}