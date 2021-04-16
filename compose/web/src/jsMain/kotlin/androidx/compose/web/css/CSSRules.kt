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
import org.w3c.dom.css.CSSGroupingRule
import org.w3c.dom.css.CSSRule
import org.w3c.dom.css.StyleSheet
import kotlin.js.Promise

interface CSSStyleRuleBuilder : StyleBuilder

open class CSSRuleBuilderImpl : CSSStyleRuleBuilder, StyleBuilderImpl()

abstract class CSSRuleDeclaration {
    abstract val header: String

    abstract override fun equals(other: Any?): Boolean
}

class CSSStyleRuleDeclaration(
    val selector: CSSSelector,
    val properties: StylePropertyList
): CSSRuleDeclaration() {
    override val header
        get() = selector.toString()
    // StylePropertyValue is js native object without equals
    override fun equals(other: Any?): Boolean {
        return if (other is CSSStyleRuleDeclaration) {
            var index = 0
            selector == other.selector && properties.all { prop ->
                val otherProp = other.properties[index++]
                prop.name == otherProp.name && prop.value.toString() == otherProp.value.toString()
            }
        } else false
    }
}

abstract class CSSGroupingRuleDeclaration(
    val rules: CSSRuleDeclarationList
): CSSRuleDeclaration()

typealias CSSRuleDeclarationList = List<CSSRuleDeclaration>
typealias MutableCSSRuleDeclarationList = MutableList<CSSRuleDeclaration>

private fun StyleSheet.addRule(cssRule: String): CSSRule {
    val cssRuleIndex = this.insertRule(cssRule, this.cssRules.length)
    return this.cssRules[cssRuleIndex]
}

private fun CSSGroupingRule.addRule(cssRule: String): CSSRule {
    val cssRuleIndex = this.insertRule(cssRule, this.cssRules.length)
    return this.cssRules[cssRuleIndex]
}

private fun StyleSheet.addRule(cssRuleDeclaration: CSSRuleDeclaration) {
    val cssRule = addRule("${cssRuleDeclaration.header} {}")
    fillRule(cssRuleDeclaration, cssRule)
}

private fun CSSGroupingRule.addRule(cssRuleDeclaration: CSSRuleDeclaration) {
    val cssRule = addRule("${cssRuleDeclaration.header} {}")
    fillRule(cssRuleDeclaration, cssRule)
}

private fun fillRule(
    cssRuleDeclaration: CSSRuleDeclaration,
    cssRule: CSSRule
) {
    when (cssRuleDeclaration) {
        is CSSStyleRuleDeclaration ->
            cssRuleDeclaration.properties.forEach { (name, value) ->
                cssRule.styleMap.set(name, value)
            }
        is CSSGroupingRuleDeclaration -> {
            val cssGroupingRule = cssRule.unsafeCast<CSSGroupingRule>()
            cssRuleDeclaration.rules.forEach { childRuleDeclaration ->
                cssGroupingRule.addRule(childRuleDeclaration)
            }
        }
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

fun buildCSSStyleRule(cssRule: CSSStyleRuleBuilder.() -> Unit): StylePropertyList {
    val builder = CSSRuleBuilderImpl()
    builder.cssRule()
    return builder.properties
}
