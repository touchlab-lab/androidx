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
import kotlin.test.assertTrue
import kotlinx.browser.document
import androidx.compose.web.renderComposable
import org.w3c.dom.HTMLElement
import androidx.compose.foundation.layout.ww.Box
import org.jetbrains.ui.ww.Modifier
import org.jetbrains.ui.ww.size
import org.jetbrains.ui.ww.background
import androidx.core.graphics.ww.Color
import androidx.compose.ui.unit.ww.dp

private fun String.asHtmlElement() = document.createElement("div") as HTMLElement

class ModifierTests {
    @Test
    fun backgroundModifier() {
        val root = "div".asHtmlElement()
        renderComposable(
            root = root
        ) {
            Box(
                Modifier.background(Color(255, 0, 0))
            ) { }
        }

        val el = root.firstChild
        assertTrue(el is HTMLElement, "element not found")

        assertEquals("background-color: rgb(255, 0, 0);", el.style.cssText)
    }

    @Test
    fun size() {
        val root = "div".asHtmlElement()
        renderComposable(
            root = root
        ) {
            Box(
                Modifier.size(40.dp)
            ) { }
        }

        val el = root.firstChild
        assertTrue(el is HTMLElement, "element not found")

        assertEquals("width: 40px; height: 40px;", el.style.cssText)
    }
}