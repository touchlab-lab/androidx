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

package androidx.compose.web.attributes

import org.w3c.dom.HTMLInputElement

object Attrs {
    object Div
    object A
    object Button
    object Form
    object Input
    object Select
    object Option
    object OptGroup
    object H
    object Ul
    object Ol
    object Li
    object Img
    object TextArea
    object Nav
}

/* Anchor <a> attributes */

fun AttrsBuilder<Attrs.A>.href(value: String?) =
    attr("href", value)

fun AttrsBuilder<Attrs.A>.target(value: ATarget = ATarget.Self) =
    attr("target", value.targetStr)

fun AttrsBuilder<Attrs.A>.ref(value: ARel) =
    attr("rel", value.relStr)

fun AttrsBuilder<Attrs.A>.ping(value: String) =
    attr("ping", value)

fun AttrsBuilder<Attrs.A>.ping(vararg urls: String) =
    attr("ping", urls.joinToString(" "))

fun AttrsBuilder<Attrs.A>.hreflang(value: String) =
    attr("hreflang", value)

fun AttrsBuilder<Attrs.A>.download(value: String = "") =
    attr("download", value)

/* Button attributes */

fun AttrsBuilder<Attrs.Button>.autoFocus(value: Boolean = true) =
    attr("autofocus", value.toString())

fun AttrsBuilder<Attrs.Button>.disabled(value: Boolean = true) =
    attr("disabled", value.toString())

fun AttrsBuilder<Attrs.Button>.form(formId: String) =
    attr("form", formId)

fun AttrsBuilder<Attrs.Button>.formAction(url: String) =
    attr("formaction", url)

fun AttrsBuilder<Attrs.Button>.formEncType(value: ButtonFormEncType) =
    attr("formenctype", value.typeStr)

fun AttrsBuilder<Attrs.Button>.formMethod(value: ButtonFormMethod) =
    attr("formmethod", value.methodStr)

fun AttrsBuilder<Attrs.Button>.formNoValidate(value: Boolean = true) =
    attr("formnovalidate", value.toString())

fun AttrsBuilder<Attrs.Button>.formTarget(value: ButtonFormTarget) =
    attr("formtarget", value.targetStr)

fun AttrsBuilder<Attrs.Button>.name(value: String) =
    attr("name", value)

fun AttrsBuilder<Attrs.Button>.type(value: ButtonType) =
    attr("type", value.str)

fun AttrsBuilder<Attrs.Button>.value(value: String) =
    attr("value", value)

/* Form attributes */

fun AttrsBuilder<Attrs.Form>.action(value: String) =
    attr("action", value)

fun AttrsBuilder<Attrs.Form>.acceptCharset(value: String) =
    attr("accept-charset", value)

fun AttrsBuilder<Attrs.Form>.autoComplete(value: Boolean) =
    attr("autocomplete", value.toString())

fun AttrsBuilder<Attrs.Form>.encType(value: FormEncType) =
    attr("enctype", value.typeStr)

fun AttrsBuilder<Attrs.Form>.method(value: FormMethod) =
    attr("method", value.methodStr)

fun AttrsBuilder<Attrs.Form>.noValidate(value: Boolean = true) =
    attr("novalidate", value.toString())

fun AttrsBuilder<Attrs.Form>.target(value: FormTarget) =
    attr("target", value.targetStr)

/* Input attributes */

fun AttrsBuilder<Attrs.Input>.type(value: InputType) =
    attr("type", value.typeStr)

fun AttrsBuilder<Attrs.Input>.accept(value: String) =
    attr("accept", value) // type: file only

fun AttrsBuilder<Attrs.Input>.alt(value: String) =
    attr("alt", value) // type: image only

fun AttrsBuilder<Attrs.Input>.autoComplete(value: Boolean = true) =
    attr("autocomplete", value.toString())

fun AttrsBuilder<Attrs.Input>.autoFocus(value: Boolean = true) =
    attr("autofocus", value.toString())

fun AttrsBuilder<Attrs.Input>.capture(value: String) =
    attr("capture", value) // type: file only

fun AttrsBuilder<Attrs.Input>.checked(value: Boolean = true) =
    attr("checked", value.toString()) // radio, checkbox

fun AttrsBuilder<Attrs.Input>.dirName(value: String) =
    attr("dirname", value) // text, search

fun AttrsBuilder<Attrs.Input>.disabled(value: Boolean = true) =
    attr("disabled", value.toString())

fun AttrsBuilder<Attrs.Input>.form(id: String) =
    attr("form", id)

fun AttrsBuilder<Attrs.Input>.formAction(url: String) =
    attr("formaction", url)

fun AttrsBuilder<Attrs.Input>.formEncType(value: InputFormEncType) =
    attr("formenctype", value.typeStr)

fun AttrsBuilder<Attrs.Input>.formMethod(value: InputFormMethod) =
    attr("formmethod", value.methodStr)

fun AttrsBuilder<Attrs.Input>.formNoValidate(value: Boolean = true) =
    attr("formnovalidate", value.toString())

fun AttrsBuilder<Attrs.Input>.formTarget(value: InputFormTarget) =
    attr("formtarget", value.targetStr)

fun AttrsBuilder<Attrs.Input>.height(value: Int) =
    attr("height", value.toString()) // image only

fun AttrsBuilder<Attrs.Input>.width(value: Int) =
    attr("width", value.toString()) // image only

fun AttrsBuilder<Attrs.Input>.list(dataListId: String) =
    attr("list", dataListId)

fun AttrsBuilder<Attrs.Input>.max(value: String) =
    attr("max", value)

fun AttrsBuilder<Attrs.Input>.maxLength(value: Int) =
    attr("maxlength", value.toString())

fun AttrsBuilder<Attrs.Input>.min(value: String) =
    attr("min", value)

fun AttrsBuilder<Attrs.Input>.minLength(value: Int) =
    attr("minlength", value.toString())

fun AttrsBuilder<Attrs.Input>.multiple(value: Boolean = true) =
    attr("multiple", value.toString())

fun AttrsBuilder<Attrs.Input>.name(value: String) =
    attr("name", value)

fun AttrsBuilder<Attrs.Input>.pattern(value: String) =
    attr("pattern", value)

fun AttrsBuilder<Attrs.Input>.placeholder(value: String) =
    attr("placeholder", value)

fun AttrsBuilder<Attrs.Input>.readOnly(value: Boolean = true) =
    attr("readonly", value.toString())

fun AttrsBuilder<Attrs.Input>.required(value: Boolean = true) =
    attr("required", value.toString())

fun AttrsBuilder<Attrs.Input>.size(value: Int) =
    attr("size", value.toString())

fun AttrsBuilder<Attrs.Input>.src(value: String) =
    attr("src", value.toString()) // image only

fun AttrsBuilder<Attrs.Input>.step(value: Int) =
    attr("step", value.toString()) // numeric types only

fun AttrsBuilder<Attrs.Input>.valueAttr(value: String) =
    attr("value", value)

fun AttrsBuilder<Attrs.Input>.valueProp(value: String): AttrsBuilder<Attrs.Input> {
    prop(setInputValue, value)
    return this
}

/* Option attributes */

fun AttrsBuilder<Attrs.Option>.value(value: String) =
    attr("value", value)

fun AttrsBuilder<Attrs.Option>.disabled(value: Boolean = true) =
    attr("disabled", value.toString())

fun AttrsBuilder<Attrs.Option>.selected(value: Boolean = true) =
    attr("selected", value.toString())

fun AttrsBuilder<Attrs.Option>.label(value: String) =
    attr("label", value)

/* Select attributes */

fun AttrsBuilder<Attrs.Select>.autocomplete(value: String) =
    attr("autocomplete", value)

fun AttrsBuilder<Attrs.Select>.autofocus(value: Boolean = true) =
    attr("autofocus", value.toString())

fun AttrsBuilder<Attrs.Select>.disabled(value: Boolean = true) =
    attr("disabled", value.toString())

fun AttrsBuilder<Attrs.Select>.form(formId: String) =
    attr("form", formId)

fun AttrsBuilder<Attrs.Select>.multiple(value: Boolean = true) =
    attr("multiple", value.toString())

fun AttrsBuilder<Attrs.Select>.name(value: String) =
    attr("name", value)

fun AttrsBuilder<Attrs.Select>.required(value: Boolean = true) =
    attr("required", value.toString())

fun AttrsBuilder<Attrs.Select>.size(numberOfRows: Int) =
    attr("size", numberOfRows.toString())

/* OptGroup attributes */

fun AttrsBuilder<Attrs.OptGroup>.label(value: String) =
    attr("label", value)

fun AttrsBuilder<Attrs.OptGroup>.disabled(value: Boolean = true) =
    attr("disabled", value.toString())

/* TextArea attributes */

fun AttrsBuilder<Attrs.TextArea>.autoComplete(value: Boolean = true) =
    attr("autocomplete", if (value) "on" else "off")

fun AttrsBuilder<Attrs.TextArea>.autoFocus(value: Boolean = true) =
    attr("autofocus", value.toString())

fun AttrsBuilder<Attrs.TextArea>.cols(value: Int) =
    attr("cols", value.toString())

fun AttrsBuilder<Attrs.TextArea>.disabled(value: Boolean = true) =
    attr("disabled", value.toString())

fun AttrsBuilder<Attrs.TextArea>.form(formId: String) =
    attr("form", formId)

fun AttrsBuilder<Attrs.TextArea>.maxLength(value: Int) =
    attr("maxlength", value.toString())

fun AttrsBuilder<Attrs.TextArea>.minLength(value: Int) =
    attr("minlength", value.toString())

fun AttrsBuilder<Attrs.TextArea>.name(value: String) =
    attr("name", value)

fun AttrsBuilder<Attrs.TextArea>.placeholder(value: String) =
    attr("placeholder", value)

fun AttrsBuilder<Attrs.TextArea>.readOnly(value: Boolean = true) =
    attr("readonly", value.toString())

fun AttrsBuilder<Attrs.TextArea>.required(value: Boolean = true) =
    attr("required", value.toString())

fun AttrsBuilder<Attrs.TextArea>.rows(value: Int) =
    attr("rows", value.toString())

fun AttrsBuilder<Attrs.TextArea>.wrap(value: TextAreaWrap) =
    attr("wrap", value.str)

fun AttrsBuilder<Attrs.TextArea>.valueProp(value: String): AttrsBuilder<Attrs.TextArea> {
    prop(setInputValue, value)
    return this
}

/* Img attributes */

fun AttrsBuilder<Attrs.Img>.src(value: String?): AttrsBuilder<Attrs.Img> =
    attr("src", value)

fun AttrsBuilder<Attrs.Img>.alt(value: String?): AttrsBuilder<Attrs.Img> =
    attr("alt", value)

private val setInputValue: (HTMLInputElement, String) -> Unit = { e, v ->
    e.value = v
}