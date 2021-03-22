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

package androidx.compose.js.sample

import androidx.compose.js.MppModifier
import androidx.compose.js.Element
import androidx.compose.js.Text
import androidx.compose.js.attr
import androidx.compose.js.classes
import androidx.compose.js.compose
import androidx.compose.js.div
import androidx.compose.js.events.onClick
import androidx.compose.js.inlineStyles
import androidx.compose.js.renderComposable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.browser.document

@Composable
fun Button(modifier: MppModifier = MppModifier, content: @Composable () -> Unit) {
    Element("button", modifier = modifier, content = content)
}

@Composable
fun SimpleTextButton(text: String, modifier: MppModifier = MppModifier) {
    Button(modifier = modifier) {
        Text(text)
    }
}

@Composable
fun CounterApp(counter: MutableState<Int>) {

    Counter(counter.value)

    SimpleTextButton(
        text = "Increment ${counter.value}",
        modifier = MppModifier.onClick {
            counter.value = counter.value + 1
        }.inlineStyles(
            """
            color: ${if (counter.value % 2 == 0) "green" else "red"};
            width: ${counter.value + 200}px;
            font-size: ${if (counter.value % 2 == 0) "25px" else "30px"};
            margin: 15px;
            """.trimIndent()
        )
    )
}

@Composable
fun Counter(value: Int) {
    div(
        modifier = MppModifier
            .inlineStyles("color:red;")
            .classes("counter")
            .attr("id", "counter")
            .attr("title", "This a counter!")
    ) {
        Text("Counter = $value")
    }
}

fun main() {
    val root = document.getElementById("root")!!

    renderComposable(
        root = root
    ) {
        val counter = remember { mutableStateOf(0) }

        CounterApp(counter)

        if (counter.value % 2 == 0) {
            div(
                modifier = MppModifier.inlineStyles(
                    "background: red; width: 200px; height: 200px"
                )
            ) {
                Text("Text in a box")
            }
        }
    }
}
