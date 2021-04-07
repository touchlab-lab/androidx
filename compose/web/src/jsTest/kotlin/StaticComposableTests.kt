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

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.browser.document
import androidx.compose.web.renderComposable
import org.w3c.dom.HTMLElement
import androidx.compose.web.elements.Text

private fun String.asHtmlElement() = document.createElement("div") as HTMLElement

class StaticComposableTests {
    @Test
    fun emptyComposable() {
        val root = "div".asHtmlElement()
        renderComposable(
            root = root
        ) {}
        assertEquals(root.outerHTML, "<div></div>")
    }

    @Test
    fun testText() {
        val root = "div".asHtmlElement()
        renderComposable(
            root = root
        ) {
            Text("inner text")
        }
        assertEquals(root.outerHTML, "<div>inner text</div>")
    }
}