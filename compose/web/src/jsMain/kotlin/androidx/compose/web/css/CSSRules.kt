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

import androidx.compose.web.css.selectors.CSSSelector

interface CSSStyleRuleBuilder : StyleBuilder

open class CSSRuleBuilderImpl : CSSStyleRuleBuilder, StyleBuilderImpl()

abstract class CSSRuleDeclaration {
    abstract val header: String

    abstract override fun equals(other: Any?): Boolean
}

class CSSStyleRuleDeclaration(
    val selector: CSSSelector,
    val properties: StylePropertyList
) : CSSRuleDeclaration() {
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
) : CSSRuleDeclaration()

typealias CSSRuleDeclarationList = List<CSSRuleDeclaration>
typealias MutableCSSRuleDeclarationList = MutableList<CSSRuleDeclaration>

fun buildCSSStyleRule(cssRule: CSSStyleRuleBuilder.() -> Unit): StylePropertyList {
    val builder = CSSRuleBuilderImpl()
    builder.cssRule()
    return builder.properties
}
