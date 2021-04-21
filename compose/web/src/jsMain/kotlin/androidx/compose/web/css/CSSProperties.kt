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
    property("opacity", value(value))
}

fun StyleBuilder.opacity(value: CSSpercentValue) {
    property("opacity", value(value.value as Double / 100))
}

fun StyleBuilder.color(value: String) {
    property("color", value(value))
}

fun StyleBuilder.color(value: Color) {
    // color hasn't Typed OM yet
    property("color", value.styleValue())
}

fun StyleBuilder.backgroundColor(value: CSSVariableValue<Color>) {
    property("background-color", value)
}

fun StyleBuilder.backgroundColor(value: Color) {
    property("background-color", value.styleValue())
}

fun StyleBuilder.backgroundColor(value: String) {
    property("background-color", value(value))
}

enum class LineStyle {
    None,
    Hidden,
    Dotted,
    Dashed,
    Solid,
    Double,
    Groove,
    Ridge,
    Inset,
    Outset
}

enum class DisplayStyle(val value: String) {
    Block("block"),
    Inline("inline"),
    InlineBlock("inline-block"),
    Flex("flex"),
    LegacyInlineFlex("inline-flex"),
    Grid("grid"),
    LegacyInlineGrid("inline-grid"),
    FlowRoot("flow-root"),

    None("none"),
    Contents("contents"),

// TODO(shabunc): This properties behave them iconsistenly in both Chrome and Firefox so I turned the off so far
//    BlockFlow("block flow"),
//    InlineFlow("inline flow"),
//    InlineFlowRoot("inline flow-root"),
//    BlocklFlex("block flex"),
//    InlineFlex("inline flex"),
//    BlockGrid("block grid"),
//    InlineGrid("inline grid"),
//    BlockFlowRoot("block flow-root"),

    Table("table"),
    TableRow("table-row"),
    ListItem("list-item"),

    Inherit("inherit"),
    Initial("initial"),
    Unset("unset")
}

fun StyleBuilder.border(
    width: CSSSizeValue? = null,
    style: LineStyle? = null,
    color: Color? = null
) {
    val values = listOfNotNull(width, style?.name, color?.styleValue())
    property("border", value(values.joinToString(" ")))
}

fun StyleBuilder.display(displayStyle: DisplayStyle) {
    add("display", StylePropertyValue(displayStyle.value))
}

fun StyleBuilder.width(value: CSSSizeOrAutoValue) {
    property("width", value)
}

fun StyleBuilder.width(value: CSSSizeValue) {
    width(CSSSizeOrAutoValue(value))
}

fun StyleBuilder.width(value: CSSAutoValue) {
    width(CSSSizeOrAutoValue(value))
}

fun StyleBuilder.height(value: CSSSizeOrAutoValue) {
    property("height", value)
}

fun StyleBuilder.height(value: CSSSizeValue) {
    height(CSSSizeOrAutoValue(value))
}

fun StyleBuilder.height(value: CSSAutoValue) {
    height(CSSSizeOrAutoValue(value))
}

fun StyleBuilder.fontSize(value: CSSSizeValue) {
    property("font-size", value(value))
}

fun StyleBuilder.margin(value: CSSSizeValue) {
    // marign hasn't Typed OM yet
    property("margin", value(value.toString()))
}

fun StyleBuilder.padding(value: CSSSizeValue) {
    // padding hasn't Typed OM yet
    property("padding", value(value.toString()))
}
