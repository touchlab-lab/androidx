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

package androidx.compose.foundation.layout.ww

import org.jetbrains.ui.ww.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.web.elements.Div
import org.jetbrains.ui.ww.asStyleBuilderApplier
import androidx.compose.ui.ww.Alignment

@Composable
internal actual fun RowActual(
    modifier: Modifier,
    horizontalArrangement: Arrangement.Horizontal,
    verticalAlignment: Alignment.Vertical,
    content: @Composable () -> Unit
) {
    Div(
        style = modifier.asStyleBuilderApplier()
    ) {
        content()
    }
}