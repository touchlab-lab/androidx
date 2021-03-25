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

package androidx.compose.web.sample

import androidx.compose.web.attributes.Draggable
import androidx.compose.web.elements.A
import androidx.compose.web.elements.Button
import androidx.compose.web.elements.Div
import androidx.compose.web.elements.Text
import androidx.compose.web.elements.TextArea
import androidx.compose.web.renderComposable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.browser.document
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class State {
    var isDarkTheme by mutableStateOf(false)
}

val globalState = State()
val globalInt = mutableStateOf(1)

@Composable
fun CounterApp(counter: MutableState<Int>) {
    Counter(counter.value)

    Button(
        style = {
            cssText = """
            color: ${if (counter.value % 2 == 0) "green" else "red"};
            width: ${counter.value + 200}px;
            font-size: ${if (counter.value % 2 == 0) "25px" else "30px"};
            margin: 15px;
        """.trimIndent().replace("\n", "")
        },
        attrs = {
            onClick { counter.value = counter.value + 1 }
        }
    ) {
        Text("Increment ${counter.value}")
    }
}


@Composable
fun Counter(value: Int) {
    Div(attrs = {
        classes("counter")
        id("counter")
        draggable(Draggable.True)
        attr("title", "This is a counter!")
        onDrag { println("DRAGGING NOW!!!!") }
    }, style = {
        cssText = "color: red;"
    }) {
        Text("Counter = $value")
    }
}

fun main() {
    val root = document.getElementById("root")!!

    renderComposable(
        root = root
    ) {
        println("renderComposable")
        val counter = remember { mutableStateOf(0) }

        CounterApp(counter)

        val inputValue = remember { mutableStateOf("") }

        smallColoredTextWithState(
            text = derivedStateOf {
                if (inputValue.value.isNotEmpty()) {
                    " ___ " + inputValue.value
                } else {
                    ""
                }
            }
        )

        A(href = "http://127.0.0.1") {
            Text("Click Me")
        }

        MyInputComponent(text = inputValue) {
            inputValue.value = it
        }
    }

    MainScope().launch {
        while (true) {
            delay(3000)
            globalState.isDarkTheme = !globalState.isDarkTheme
        }
    }
}

@Composable
fun MyInputComponent(text: State<String>, onChange: (String) -> Unit) {
    Div {
        TextArea(value = text.value, attrs = {
            onInput {
                onChange(it.nativeEvent.target.asDynamic().value)
            }
        })
    }
}

@Composable
fun smallColoredTextWithState(text: State<String>) {
    smallColoredText(text = text.value)
}

@Composable
fun smallColoredText(text: String) {
    if (globalInt.value < 5) {
        Div(
            attrs = {
                if (globalInt.value > 2) {
                    id("someId-${globalInt.value}")
                }

                classes("someClass")

                attr("customAttr", "customValue")

                onClick {
                    globalInt.value = globalInt.value + 1
                }

                ref { element ->
                    println("DIV CREATED ${element.id}")
                    onDispose { println("DIV REMOVED ${element.id}") }
                }
            },
            style = {
                cssText = if (globalState.isDarkTheme) {
                    "color: black;"
                } else {
                    "color: green;"
                }
            }
        ) {
            Text("Text = $text")
        }
    }
}
