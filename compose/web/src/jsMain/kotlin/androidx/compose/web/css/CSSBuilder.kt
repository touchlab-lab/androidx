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

interface CSSBuilder : CSSRuleBuilder {
    val root: CSSSelector
    val self: CSSSelector

    fun rule(selector: CSSSelector, cssRule: CSSBuilder.() -> Unit)

    operator fun CSSSelector.invoke(cssRule: CSSBuilder.() -> Unit) {
        rule(this, cssRule)
    }

    infix fun CSSSelector.style(cssRule: CSSBuilder.() -> Unit) {
        rule(this, cssRule)
    }

    operator fun String.invoke(cssRule: CSSBuilder.() -> Unit) {
        rule(CSSSelector.Raw(this), cssRule)
    }

    infix fun String.style(cssRule: CSSBuilder.() -> Unit) {
        rule(CSSSelector.Raw(this), cssRule)
    }
}

class CSSBuilderImpl(
    override val root: CSSSelector,
    override val self: CSSSelector,
    sheet: StyleSheetBuilder
) : CSSBuilder, CSSRuleBuilderImpl(), StyleSheetBuilder by sheet {
    override fun rule(selector: CSSSelector, cssRule: CSSBuilder.() -> Unit) {
        val (properties, rules) = buildCSS(root, selector, cssRule)
        add(selector, properties)
        rules.forEach { add(it) }
    }
}
