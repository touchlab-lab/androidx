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
package org.jetbrains.compose.demo.falling

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.core.graphics.ww.Color

data class PieceData(val game: Game, val velocity: Float, val color: Color) {
    private fun Game.pickPiece(piece: PieceData) {
        score += piece.velocity.toInt()
        clicked++
        if (clicked == numBlocks) {
            finished = true
        }
    }

    var picked: Boolean by mutableStateOf(false)
    var position: Float by mutableStateOf(0f)

    fun update(dt: Long) {
        if (picked) return
        val delta = (dt / 1E8 * velocity).toFloat()
        position = if (game.isInBoundaries(this)) position + delta else 0f
    }

    fun pick() {
        if (!picked && !game.paused) {
            picked = true
            game.pickPiece(this)
        }
    }
}

