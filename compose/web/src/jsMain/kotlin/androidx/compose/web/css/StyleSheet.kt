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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.web.css.selectors.CSSSelector
import androidx.compose.web.css.selectors.className
import androidx.compose.web.elements.Style
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class CSSRulesHolderState : CSSRulesHolder {
    override var cssRules: CSSRuleDeclarationList by mutableStateOf(listOf())

    override fun add(cssRule: CSSRuleDeclaration) {
        cssRules += cssRule
    }
}

open class StyleSheet(
    private val rulesHolder: CSSRulesHolder = CSSRulesHolderState()
) : StyleSheetBuilder, CSSRulesHolder by rulesHolder {
    private val boundClasses = mutableMapOf<String, CSSRuleDeclarationList>()

    protected fun style(cssRule: CSSBuilder.() -> Unit) = CSSHolder(cssRule)

    companion object {
        var counter = 0
    }

    data class CSSSelfSelector(var selector: CSSSelector? = null) : CSSSelector() {
        override fun toString(): String = selector.toString()
        override fun equals(other: Any?): Boolean {
            return other is CSSSelfSelector
        }
    }

    // TODO: just proof of concept, do not use it
    fun css(cssBuilder: CSSBuilder.() -> Unit): String {
        val selfSelector = CSSSelfSelector()
        val (properties, newCssRules) = buildCSS(selfSelector, selfSelector, cssBuilder)
        val cssRule = cssRules.find {
            it.selector is CSSSelector.CSSClass && it.properties == properties &&
                (boundClasses[it.selector.className] ?: emptyList()) == newCssRules
        }
        js("debugger")
        return if (cssRule != null) {
            cssRule.selector.unsafeCast<CSSSelector.CSSClass>().className
        } else {
            val classNameSelector = className("auto-${counter++}")
            selfSelector.selector = classNameSelector
            add(classNameSelector, properties)
            newCssRules.forEach { add(it) }
            boundClasses[classNameSelector.className] = newCssRules
            classNameSelector.className
        }
    }

    protected class CSSHolder(val cssBuilder: CSSBuilder.() -> Unit) {
        operator fun provideDelegate(
            sheet: StyleSheet,
            property: KProperty<*>
        ): ReadOnlyProperty<Any?, String> {
            val sheetName = "${sheet::class.simpleName}-"
            val selector = className("$sheetName${property.name}")
            val (properties, rules) = buildCSS(selector, selector, cssBuilder)
            sheet.add(selector, properties)
            rules.forEach { sheet.add(it) }

            return ReadOnlyProperty { _, _ ->
                selector.className
            }
        }
    }
}

fun buildCSS(
    thisClass: CSSSelector,
    thisContext: CSSSelector,
    cssRule: CSSBuilder.() -> Unit
): Pair<StylePropertyList, CSSRuleDeclarationList> {
    val styleSheet = StyleSheetBuilderImpl()
    val builder = CSSBuilderImpl(thisClass, thisContext, styleSheet)
    builder.cssRule()
    return builder.properties to styleSheet.cssRules
}

@Composable
inline fun Style(
    styleSheet: CSSRulesHolder
) {
    Style(cssRules = styleSheet.cssRules)
}
