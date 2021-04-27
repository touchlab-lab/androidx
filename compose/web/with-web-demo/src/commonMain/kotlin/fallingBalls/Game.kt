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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.graphics.ww.Color
import org.jetbrains.compose.ui.ww.Size
import kotlin.random.Random

private fun Color.Companion.random() = Color((0..255).random(), (0..255).random(), (0..255).random())

abstract class Game {
    internal var previousTime: Long = Long.MAX_VALUE
    private var startTime = 0L

    var size by mutableStateOf(Size(0, 0))

    var pieces = mutableStateListOf<PieceData>()
        private set

    var elapsed by mutableStateOf(0L)
    var score by mutableStateOf(0)
    var clicked by mutableStateOf(0)

    var started by mutableStateOf(false)
    var paused by mutableStateOf(false)
    var finished by mutableStateOf(false)

    var numBlocks by mutableStateOf(5)

    fun isInBoundaries(pieceData: PieceData): Boolean = pieceData.position < size.height

    abstract fun saveTime()

    fun togglePause() {
        paused = !paused
        saveTime()
    }

    fun start() {
        saveTime()
        startTime = previousTime
        clicked = 0
        started = true
        finished = false
        paused = false
        pieces.clear()
        repeat(numBlocks) { index ->
            pieces.add(PieceData(this, index * 1.5f + 5f, Color.random()).also { piece ->
                piece.position = Random.nextDouble(0.0, 100.0).toFloat()
            })
        }
    }

    fun update(nanos: Long) {
        val dt = (nanos - previousTime).coerceAtLeast(0)
        previousTime = nanos
        elapsed = nanos - startTime
        pieces.forEach { it.update(dt) }
    }
}
