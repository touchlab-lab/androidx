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

package org.jetbrains.compose.demo.falling.views

import androidx.compose.runtime.Composable
import org.jetbrains.compose.demo.falling.PieceData
import androidx.core.graphics.ww.Color
import org.jetbrains.ui.ww.Modifier
import androidx.compose.foundation.layout.ww.Box
import androidx.compose.ui.unit.ww.dp
import androidx.compose.foundation.layout.ww.offset
import org.jetbrains.ui.ww.background
import org.jetbrains.ui.ww.size
import androidx.compose.foundation.ww.clickable

@Composable
fun piece(index: Int, piece: PieceData) {
    val boxSize = 40.dp
    Box(
        Modifier
            .offset((boxSize.value * index * 5 / 3).dp, piece.position.dp)
//            .shadow(30)
            .size(boxSize, boxSize)
            .background(if (piece.picked) Color.Gray else piece.color)
            .clickable{ piece.pick() }
//            .clip()
    ) {}
}
