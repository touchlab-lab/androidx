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
package org.jetbrains.ui.ww

import androidx.compose.ui.unit.ww.Dp
import androidx.compose.ui.unit.ww.implementation
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.background
import androidx.core.graphics.ww.Color
import androidx.core.graphics.ww.implementation
import org.jetbrains.compose.web.ww.internal.castOrCreate
import androidx.compose.foundation.layout.padding

actual fun Modifier.size(size: Dp): Modifier = castOrCreate().apply {
    modifier = modifier.size(size.implementation)
}

actual fun Modifier.background(color: Color): Modifier = castOrCreate().apply {
    modifier = modifier.background(color.implementation)
}

actual fun Modifier.padding(all: Dp): Modifier = castOrCreate().apply {
    modifier = modifier.padding(all.implementation)
}

val Modifier.implementation
    get() = castOrCreate().modifier