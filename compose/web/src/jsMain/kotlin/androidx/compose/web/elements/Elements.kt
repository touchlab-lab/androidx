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
import androidx.compose.web.attributes.Attrs
import androidx.compose.web.attributes.AttrsBuilder
import androidx.compose.web.attributes.InputType
import androidx.compose.web.attributes.StyleBuilder
import androidx.compose.web.attributes.action
import androidx.compose.web.attributes.alt
import androidx.compose.web.attributes.href
import androidx.compose.web.attributes.label
import androidx.compose.web.attributes.src
import androidx.compose.web.attributes.type
import androidx.compose.web.attributes.value
import androidx.compose.web.attributes.valueProp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import kotlinx.browser.document
import org.w3c.dom.Text

@Composable
fun Text(value: String) {
    ComposeNode<DomNodeWrapper, DomApplier>(
        factory = { DomNodeWrapper(document.createTextNode("")) },
        update = {
            set(value) { value -> (node as Text).data = value }
        },
    )
}

@Composable
inline fun Div(
    crossinline attrs: (AttrsBuilder<Attrs.Div>.() -> Unit) = {},
    crossinline style: (StyleBuilder.() -> Unit) = {},
    content: @Composable ElementScope.() -> Unit
) {
    TagElement(
        tagName = "div",
        applyAttrs = attrs,
        applyStyle = style,
        content = content
    )
}

@Composable
inline fun A(
    href: String? = null,
    crossinline attrs: (AttrsBuilder<Attrs.A>.() -> Unit) = {},
    content: @Composable ElementScope.() -> Unit
) {
    TagElement<Attrs.A>(
        tagName = "a",
        applyAttrs = {
            href(href)
            attrs()
        },
        content = content
    )
}

@Composable
inline fun Input(
    type: InputType = InputType.Text,
    value: String = "",
    crossinline attrs: (AttrsBuilder<Attrs.Input>.() -> Unit) = {},
    crossinline style: (StyleBuilder.() -> Unit) = {},
    content: @Composable ElementScope.() -> Unit = {}
) {
    TagElement<Attrs.Input>(
        tagName = "input",
        applyAttrs = {
            type(type)
            valueProp(value)
            attrs()
        },
        applyStyle = style,
        content = content
    )
}

@Composable
inline fun Button(
    crossinline attrs: AttrsBuilder<Attrs.Button>.() -> Unit = {},
    crossinline style: (StyleBuilder.() -> Unit) = {},
    content: @Composable ElementScope.() -> Unit
) = TagElement("button", applyAttrs = attrs, applyStyle = style, content = content)

@Composable
inline fun H1(
    crossinline attrs: AttrsBuilder<Attrs.H>.() -> Unit = {},
    crossinline style: (StyleBuilder.() -> Unit) = {},
    content: @Composable ElementScope.() -> Unit
) = TagElement("h1", applyAttrs = attrs, applyStyle = style, content = content)

@Composable
inline fun H2(
    crossinline attrs: AttrsBuilder<Attrs.H>.() -> Unit = {},
    crossinline style: (StyleBuilder.() -> Unit) = {},
    content: @Composable ElementScope.() -> Unit
) = TagElement("h2", applyAttrs = attrs, applyStyle = style, content = content)

@Composable
inline fun H3(
    crossinline attrs: AttrsBuilder<Attrs.H>.() -> Unit = {},
    crossinline style: (StyleBuilder.() -> Unit) = {},
    content: @Composable ElementScope.() -> Unit
) = TagElement("h3", applyAttrs = attrs, applyStyle = style, content = content)

@Composable
inline fun H4(
    crossinline attrs: AttrsBuilder<Attrs.H>.() -> Unit = {},
    crossinline style: (StyleBuilder.() -> Unit) = {},
    content: @Composable ElementScope.() -> Unit
) = TagElement("h4", applyAttrs = attrs, applyStyle = style, content = content)

@Composable
inline fun H5(
    crossinline attrs: AttrsBuilder<Attrs.H>.() -> Unit = {},
    crossinline style: (StyleBuilder.() -> Unit) = {},
    content: @Composable ElementScope.() -> Unit
) = TagElement("h5", applyAttrs = attrs, applyStyle = style, content = content)

@Composable
inline fun H6(
    crossinline attrs: AttrsBuilder<Attrs.H>.() -> Unit = {},
    crossinline style: (StyleBuilder.() -> Unit) = {},
    content: @Composable ElementScope.() -> Unit
) = TagElement("h6", applyAttrs = attrs, applyStyle = style, content = content)

@Composable
inline fun P(
    crossinline attrs: AttrsBuilder<Attrs>.() -> Unit = {},
    crossinline style: (StyleBuilder.() -> Unit) = {},
    content: @Composable ElementScope.() -> Unit
) = TagElement("p", applyAttrs = attrs, applyStyle = style, content = content)

@Composable
inline fun Em(
    crossinline attrs: AttrsBuilder<Attrs>.() -> Unit = {},
    crossinline style: (StyleBuilder.() -> Unit) = {},
    content: @Composable ElementScope.() -> Unit
) = TagElement("em", applyAttrs = attrs, applyStyle = style, content = content)

@Composable
inline fun I(
    crossinline attrs: AttrsBuilder<Attrs>.() -> Unit = {},
    crossinline style: (StyleBuilder.() -> Unit) = {},
    content: @Composable ElementScope.() -> Unit
) = TagElement("i", applyAttrs = attrs, applyStyle = style, content = content)

@Composable
inline fun B(
    crossinline attrs: AttrsBuilder<Attrs>.() -> Unit = {},
    crossinline style: (StyleBuilder.() -> Unit) = {},
    content: @Composable ElementScope.() -> Unit
) = TagElement("b", applyAttrs = attrs, applyStyle = style, content = content)

@Composable
inline fun Small(
    crossinline attrs: AttrsBuilder<Attrs>.() -> Unit = {},
    crossinline style: (StyleBuilder.() -> Unit) = {},
    content: @Composable ElementScope.() -> Unit
) = TagElement("small", applyAttrs = attrs, applyStyle = style, content = content)

@Composable
inline fun Span(
    crossinline attrs: AttrsBuilder<Attrs>.() -> Unit = {},
    crossinline style: (StyleBuilder.() -> Unit) = {},
    content: @Composable ElementScope.() -> Unit
) = TagElement("span", applyAttrs = attrs, applyStyle = style, content = content)

@Composable
inline fun Br(
    crossinline attrs: AttrsBuilder<Attrs>.() -> Unit = {},
    crossinline style: (StyleBuilder.() -> Unit) = {},
    content: @Composable ElementScope.() -> Unit
) = TagElement("br", applyAttrs = attrs, applyStyle = style, content = content)

@Composable
inline fun Ul(
    crossinline attrs: AttrsBuilder<Attrs.Ul>.() -> Unit = {},
    crossinline style: (StyleBuilder.() -> Unit) = {},
    content: @Composable ElementScope.() -> Unit,
) = TagElement("ul", applyAttrs = attrs, applyStyle = style, content = content)

@Composable
inline fun Ol(
    crossinline attrs: AttrsBuilder<Attrs.Ol>.() -> Unit = {},
    crossinline style: (StyleBuilder.() -> Unit) = {},
    content: @Composable ElementScope.() -> Unit
) = TagElement("ol", applyAttrs = attrs, applyStyle = style, content = content)

@Composable
inline fun Li(
    crossinline attrs: AttrsBuilder<Attrs.Li>.() -> Unit = {},
    crossinline style: (StyleBuilder.() -> Unit) = {},
    content: @Composable ElementScope.() -> Unit
) = TagElement("li", applyAttrs = attrs, applyStyle = style, content = content)

@Composable
inline fun Img(
    src: String,
    alt: String = "",
    crossinline attrs: AttrsBuilder<Attrs.Img>.() -> Unit = {},
    crossinline style: (StyleBuilder.() -> Unit) = {},
    content: @Composable ElementScope.() -> Unit
) = TagElement<Attrs.Img>(
    tagName = "img",
    applyAttrs = {
        src(src).alt(alt)
        attrs()
    },
    applyStyle = style, content = content
)

@Composable
inline fun Form(
    action: String? = null,
    crossinline attrs: AttrsBuilder<Attrs.Form>.() -> Unit = {},
    crossinline style: (StyleBuilder.() -> Unit) = {},
    content: @Composable ElementScope.() -> Unit
) = TagElement<Attrs.Form>(
    tagName = "form",
    applyAttrs = {
        if (!action.isNullOrEmpty()) action(action)
        attrs()
    },
    applyStyle = style, content = content
)

@Composable
inline fun Select(
    crossinline attrs: AttrsBuilder<Attrs.Select>.() -> Unit = {},
    crossinline style: (StyleBuilder.() -> Unit) = {},
    content: @Composable ElementScope.() -> Unit
) = TagElement<Attrs.Select>(
    tagName = "select",
    applyAttrs = attrs,
    applyStyle = style,
    content = content
)

@Composable
inline fun Option(
    value: String,
    crossinline attrs: AttrsBuilder<Attrs.Option>.() -> Unit = {},
    crossinline style: (StyleBuilder.() -> Unit) = {},
    content: @Composable ElementScope.() -> Unit
) = TagElement<Attrs.Option>(
    tagName = "option",
    applyAttrs = {
        value(value)
        attrs()
    },
    applyStyle = style,
    content = content
)

@Composable
inline fun OptGroup(
    label: String,
    crossinline attrs: AttrsBuilder<Attrs.OptGroup>.() -> Unit = {},
    crossinline style: (StyleBuilder.() -> Unit) = {},
    content: @Composable ElementScope.() -> Unit
) = TagElement<Attrs.OptGroup>(
    tagName = "optgroup",
    applyAttrs = {
        label(label)
        attrs()
    },
    applyStyle = style,
    content = content
)

@Composable
inline fun Section(
    crossinline attrs: AttrsBuilder<Attrs>.() -> Unit = {},
    crossinline style: (StyleBuilder.() -> Unit) = {},
    content: @Composable ElementScope.() -> Unit
) = TagElement<Attrs>(
    tagName = "section",
    applyAttrs = attrs,
    applyStyle = style,
    content = content
)

@Composable
inline fun TextArea(
    crossinline attrs: AttrsBuilder<Attrs.TextArea>.() -> Unit = {},
    crossinline style: (StyleBuilder.() -> Unit) = {},
    value: String
) = TagElement<Attrs.TextArea>(
    tagName = "textarea",
    applyAttrs = {
        valueProp(value)
        attrs()
    },
    applyStyle = style
) {
    Text(value)
}

@Composable
inline fun Nav(
    crossinline attrs: AttrsBuilder<Attrs.Nav>.() -> Unit = {},
    crossinline style: (StyleBuilder.() -> Unit) = {},
    content: @Composable ElementScope.() -> Unit
) = TagElement<Attrs.Nav>(
    tagName = "nav",
    applyAttrs = attrs,
    applyStyle = style,
    content = content
)
