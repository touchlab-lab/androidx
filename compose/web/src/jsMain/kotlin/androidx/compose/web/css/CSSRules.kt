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

import androidx.compose.web.attributes.AttrsBuilder
import androidx.compose.web.attributes.Tag
import androidx.compose.web.css.selectors.CSSSelector
import org.w3c.dom.HTMLStyleElement
import org.w3c.dom.css.CSSRule
import org.w3c.dom.css.StyleSheet
import kotlin.js.Promise

interface CSSRuleBuilder : StyleBuilder

open class CSSRuleBuilderImpl : CSSRuleBuilder, StyleBuilderImpl()

data class CSSRuleDeclaration(
    val selector: CSSSelector,
    val properties: StylePropertyList
) {
    // StylePropertyValue is js native object without equals
    override fun equals(other: Any?): Boolean {
        return if (other is CSSRuleDeclaration) {
            var index = 0
            selector == other.selector && properties.all { prop ->
                val otherProp = other.properties[index++]
                prop.name == otherProp.name && prop.value.toString() == otherProp.value.toString()
            }
        } else false
    }
}

typealias CSSRuleDeclarationList = List<CSSRuleDeclaration>
typealias MutableCSSRuleDeclarationList = MutableList<CSSRuleDeclaration>

private fun StyleSheet.addRule(cssRule: String): CSSRule {
    val cssRuleIndex = this.insertRule(cssRule, this.cssRules.length)
    return this.cssRules[cssRuleIndex]
}

private fun StyleSheet.addRule(rule: CSSRuleDeclaration) {
    val cssRule = addRule("${rule.selector} {}")
    rule.properties.forEach { (name, value) ->
        cssRule.styleMap.set(name, value)
    }
}

fun AttrsBuilder<Tag.Style>.cssRules(rules: CSSRuleDeclarationList): AttrsBuilder<Tag.Style> {
    prop(setCSSRules, rules)
    return this
}

private val setCSSRules: (HTMLStyleElement, CSSRuleDeclarationList) -> Unit = { e, cssRules ->
    // TODO: Make CSSRule as composable child of Style element instead of replacing each time
    val sheet = e.sheet
    if (sheet == null) {
        // schedule after element is attached
        Promise.resolve(Unit).then {
            e.sheet?.let {
                setCSSRules(it, cssRules)
            }
        }
    } else {
        setCSSRules(sheet, cssRules)
    }
}

private fun setCSSRules(sheet: StyleSheet, cssRules: CSSRuleDeclarationList) {
    repeat(sheet.cssRules.length) {
        sheet.deleteRule(0)
    }
    cssRules.forEach { cssRule ->
        sheet.addRule(cssRule)
    }
}

fun buildCSSRule(cssRule: CSSRuleBuilder.() -> Unit): StylePropertyList {
    val builder = CSSRuleBuilderImpl()
    builder.cssRule()
    return builder.properties
}
