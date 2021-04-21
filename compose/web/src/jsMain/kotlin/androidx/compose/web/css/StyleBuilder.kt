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

import kotlin.properties.ReadOnlyProperty

interface StyleBuilder {
    fun property(propertyName: String, value: StylePropertyValue)
    fun variable(variableName: String, value: StylePropertyValue)

    operator fun <TValue> CSSStyleVariable<TValue>.invoke(value: TValue) {
        variable(this.name, (value as? CustomStyleValue)?.styleValue() ?: value(value.toString()))
    }

    fun <TValue> CSSStyleVariable<TValue>.value(fallback: TValue? = null) =
        CSSVariableValue<TValue>(variableValue(
            name,
            fallback?.let {
                (fallback as? CustomStyleValue)?.styleValue() ?: value(fallback.toString())
            }
        ))
}

@Suppress("NOTHING_TO_INLINE")
inline fun StyleBuilder.value(value: String) = StylePropertyValue(value)
@Suppress("NOTHING_TO_INLINE")
inline fun StyleBuilder.value(value: Number) = StylePropertyValue(value)
@Suppress("NOTHING_TO_INLINE")
inline fun StyleBuilder.value(value: CSSStyleValue) = StylePropertyValue(value)

fun StyleBuilder.variableValue(variableName: String, fallback: StylePropertyValue? = null) =
    StylePropertyValue("var(--$variableName${fallback?.let { ", $it" } ?: "" })")

interface CSSVariableValue<TValue>: StylePropertyValue {
    companion object {
        operator fun <TValue> invoke(value: String) = StylePropertyValue(value).unsafeCast<CSSVariableValue<TValue>>()
        operator fun <TValue> invoke(value: Number) = StylePropertyValue(value).unsafeCast<CSSVariableValue<TValue>>()
        operator fun <TValue: CSSStyleValue> invoke(value: TValue) = StylePropertyValue(value).unsafeCast<CSSVariableValue<TValue>>()
        operator fun <TValue> invoke(value: StylePropertyValue) = value.unsafeCast<CSSVariableValue<TValue>>()
    }
}

// after adding `variable` word `add` became ambiguous
@Deprecated(
    "use property instead, will remove it soon",
    ReplaceWith("property(propertyName, value)")
)
fun StyleBuilder.add(
    propertyName: String,
    value: StylePropertyValue
) = property(propertyName, value)



interface CSSVariables

interface CSSVariable {
    val name: String
}

interface CustomStyleValue {
    fun styleValue(): StylePropertyValue
}

data class CSSStyleVariable<TValue>(override val name: String): CSSVariable

fun <TValue> CSSVariables.variable() =
    ReadOnlyProperty<Any?, CSSStyleVariable<TValue>> { _, property ->
        CSSStyleVariable(property.name)
    }

interface StyleHolder {
    val properties: StylePropertyList
    val variables: StylePropertyList
}

open class StyleBuilderImpl : StyleBuilder, StyleHolder {
    override val properties: MutableStylePropertyList = mutableListOf()
    override val variables: MutableStylePropertyList = mutableListOf()

    override fun property(propertyName: String, value: StylePropertyValue) {
        properties.add(StylePropertyDeclaration(propertyName, value))
    }

    override fun variable(variableName: String, value: StylePropertyValue) {
        variables.add(StylePropertyDeclaration(variableName, value))
    }

    // StylePropertyValue is js native object without equals
    override fun equals(other: Any?): Boolean {
        return if (other is StyleHolder) {
            properties.nativeEquals(other.properties) &&
                variables.nativeEquals(other.variables)
        } else false
    }
}

data class StylePropertyDeclaration(
    val name: String,
    val value: StylePropertyValue
)
typealias StylePropertyList = List<StylePropertyDeclaration>
typealias MutableStylePropertyList = MutableList<StylePropertyDeclaration>

fun StylePropertyList.nativeEquals(properties: StylePropertyList): Boolean {
    var index = 0
    return all { prop ->
        val otherProp = properties[index++]
        prop.name == otherProp.name &&
            prop.value.toString() == otherProp.value.toString()
    }
}
