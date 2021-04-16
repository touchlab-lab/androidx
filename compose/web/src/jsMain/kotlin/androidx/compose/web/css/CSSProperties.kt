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

package androidx.compose.web.css

fun StyleBuilder.opacity(value: Number) {
    add("opacity", StylePropertyValue(value))
}

fun StyleBuilder.opacity(value: CSSpercentValue) {
    add("opacity", StylePropertyValue(value.value as Double / 100))
}

fun StyleBuilder.color(value: String) {
    add("color", StylePropertyValue(value))
}

fun StyleBuilder.color(value: Color) {
    // color hasn't Typed OM yet
    add("color", StylePropertyValue(value.toString()))
}

fun StyleBuilder.backgroundColor(value: Color) {
    add("background-color", StylePropertyValue(value.toString()))
}

fun StyleBuilder.backgroundColor(value: String) {
    add("background-color", StylePropertyValue(value))
}

fun StyleBuilder.border(value: String) {
    add("border", StylePropertyValue(value))
}

fun StyleBuilder.border(width: CSSSizeValue, color: Color) {
    add("border-width", StylePropertyValue(width.toString()))
    add("border-color", StylePropertyValue(color.toString()))
    add("border-color", StylePropertyValue(color.toString()))
    add("border-style", StylePropertyValue("solid"))
}

fun StyleBuilder.border(width: CSSSizeValue, color: String) {
    border(width, Color.Named(color))
}

fun StyleBuilder.width(value: CSSSizeOrAutoValue) {
    add("width", value)
}

fun StyleBuilder.width(value: CSSSizeValue) {
    width(CSSSizeOrAutoValue(value))
}

fun StyleBuilder.width(value: CSSAutoValue) {
    width(CSSSizeOrAutoValue(value))
}

fun StyleBuilder.height(value: CSSSizeOrAutoValue) {
    add("height", value)
}

fun StyleBuilder.height(value: CSSSizeValue) {
    height(CSSSizeOrAutoValue(value))
}

fun StyleBuilder.height(value: CSSAutoValue) {
    height(CSSSizeOrAutoValue(value))
}

fun StyleBuilder.fontSize(value: CSSSizeValue) {
    add("font-size", StylePropertyValue(value))
}

fun StyleBuilder.margin(value: CSSSizeValue) {
    // marign hasn't Typed OM yet
    add("margin", StylePropertyValue(value.toString()))
}

fun StyleBuilder.padding(value: CSSSizeValue) {
    // padding hasn't Typed OM yet
    add("padding", StylePropertyValue(value.toString()))
}
