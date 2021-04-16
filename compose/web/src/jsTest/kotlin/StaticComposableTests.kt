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

import androidx.compose.web.css.Color
import androidx.compose.web.css.StylePropertyValue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.browser.document
import androidx.compose.web.renderComposable
import org.w3c.dom.HTMLElement
import org.w3c.dom.get
import androidx.compose.web.elements.Text
import androidx.compose.web.elements.Div
import androidx.compose.web.css.opacity
import androidx.compose.web.css.color
import androidx.compose.web.css.border
import androidx.compose.web.css.px
import androidx.compose.web.css.width
import androidx.compose.web.css.height

private fun String.asHtmlElement() = document.createElement("div") as HTMLElement

class StaticComposableTests {
    @Test
    fun emptyComposable() {
        val root = "div".asHtmlElement()
        renderComposable(
            root = root
        ) {}
        assertEquals("<div></div>", root.outerHTML)
    }

    @Test
    fun textChild() {
        val root = "div".asHtmlElement()
        renderComposable(
            root = root
        ) {
            Text("inner text")
        }
        assertEquals("<div>inner text</div>", root.outerHTML)
    }

    @Test
    fun attrs() {
        val root = "div".asHtmlElement()
        renderComposable(
            root = root
        ) {
            Div(
                attrs = {
                    classes("some", "simple", "classes")
                    id("special")
                    attr("data-val", "some data")
                    attr("data-val", "some other data")
                    id("verySpecial")
                }
            ) {}
        }

        val el = root.firstChild
        assertTrue(el is HTMLElement, "element not found")

        assertEquals("verySpecial", el.getAttribute("id"))
        assertEquals("some simple classes", el.getAttribute("class"))
        assertEquals("some other data", el.getAttribute("data-val"))
    }

    @Test
    fun styles() {
        val root = "div".asHtmlElement()
        renderComposable(
            root = root
        ) {
            Div(
                style = {
                    opacity(0.3)
                    color("red")
                    opacity(0.2)
                    color("green")
                }
            ) {}
        }

        assertEquals("opacity: 0.2; color: green;", (root.children[0] as HTMLElement).style.cssText)
    }

    @Test
    fun stylesBorder() {
        val root = "div".asHtmlElement()
        renderComposable(
            root = root
        ) {
            Div(
                style = {
                    add("border", StylePropertyValue("1px solid red"))
                }
            ) {}
            Div(
                style = {
                    border(3.px, color = Color("green"))
                }
            ) {}
        }

        assertEquals("border: 1px solid red;", (root.children[0] as HTMLElement).style.cssText)
        root.children[1]?.let { el ->
            assertEquals(
                "green",
                el.asDynamic().attributeStyleMap.get("border-color").toString(),
            )
            assertEquals(
                "3px",
                el.asDynamic().attributeStyleMap.get("border-width").toString(),
            )
        }
    }

    @Test
    fun stylesWidth() {
        val root = "div".asHtmlElement()
        renderComposable(
            root = root
        ) {
            Div(
                style = {
                    width(100.px)
                }
            ) {}
        }

        assertEquals("width: 100px;", (root.children[0] as HTMLElement).style.cssText)
    }

    @Test
    fun stylesHeight() {
        val root = "div".asHtmlElement()
        renderComposable(
            root = root
        ) {
            Div(
                style = {
                    height(100.px)
                }
            ) {}
        }

        assertEquals("height: 100px;", (root.children[0] as HTMLElement).style.cssText)
    }
}