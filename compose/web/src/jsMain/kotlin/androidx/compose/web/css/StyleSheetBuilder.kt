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

interface CSSRulesHolder {
    val cssRules: CSSRuleDeclarationList
    fun add(cssRule: CSSRuleDeclaration)
    fun add(selector: CSSSelector, properties: StylePropertyList) {
        add(CSSRuleDeclaration(selector, properties))
    }
}

interface StyleSheetBuilder : CSSRulesHolder {
    fun rule(selector: CSSSelector, cssRule: CSSRuleBuilder.() -> Unit) {
        add(selector, buildCSSRule(cssRule))
    }

    operator fun CSSSelector.invoke(cssRule: CSSRuleBuilder.() -> Unit) {
        rule(this, cssRule)
    }

    infix fun CSSSelector.style(cssRule: CSSRuleBuilder.() -> Unit) {
        rule(this, cssRule)
    }

    operator fun String.invoke(cssRule: CSSRuleBuilder.() -> Unit) {
        rule(CSSSelector.Raw(this), cssRule)
    }

    infix fun String.style(cssRule: CSSRuleBuilder.() -> Unit) {
        rule(CSSSelector.Raw(this), cssRule)
    }
}

open class StyleSheetBuilderImpl : StyleSheetBuilder {
    override val cssRules: MutableCSSRuleDeclarationList = mutableListOf()

    override fun add(cssRule: CSSRuleDeclaration) {
        cssRules.add(cssRule)
    }
}
