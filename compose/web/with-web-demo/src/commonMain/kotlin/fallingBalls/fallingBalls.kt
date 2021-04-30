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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.withFrameNanos
import org.jetbrains.compose.demo.falling.Game
import androidx.core.graphics.ww.Color
import androidx.compose.material.ww.Text
import androidx.compose.foundation.layout.ww.Column
import androidx.compose.material.ww.Slider
import androidx.compose.foundation.layout.ww.Row
import androidx.compose.foundation.layout.ww.Box
import androidx.compose.material.ww.Button
import org.jetbrains.ui.ww.Modifier
import androidx.compose.ui.unit.ww.em
import androidx.compose.ui.unit.ww.sp
import androidx.compose.ui.unit.ww.dp
import androidx.compose.foundation.layout.ww.offset
import androidx.compose.foundation.layout.ww.width
import androidx.compose.foundation.layout.ww.fillMaxWidth
import androidx.compose.foundation.layout.ww.fillMaxHeight
import androidx.compose.ui.layout.ww.onSizeChanged
import org.jetbrains.ui.ww.background
import androidx.compose.foundation.ww.border
import org.jetbrains.ui.ww.size

@Composable
fun fallingBalls(game: Game) {
    Column() {
        Box() {
            Text(
                "Catch balls!${if (game.finished) " Game over!" else ""}",
                size = 1.8f.em,
                color = Color(218, 120, 91)
            )
        }
        Box() {
            Text(
                "Score: ${game.score} Time: ${game.elapsed / 1_000_000} Blocks: ${game.numBlocks}",
                size = 1.8f.em
            )
        }
        Row() {
            if (!game.started) {
                Slider(
                    value = game.numBlocks / 20f,
                    onValueChange = { game.numBlocks = (it * 20f).toInt().coerceAtLeast(1) },
                    modifier = Modifier.width(100.dp)
                )
            }
            Button(
                Modifier
                    .border(2.dp, Color(255, 215, 0))
                    .background(Color.Yellow),
                onClick = {
                    game.started = !game.started
                    if (game.started) {
                        game.start()
                    }
                }
            ) {
                Text(if (game.started) "Stop" else "Start", size = 2f.em)
            }
            if (game.started) {
                Button(
                    Modifier
                        .offset(10.dp, 0.dp)
                        .border(2.dp, Color(255, 215, 0))
                        .background(Color.Yellow),
                    onClick = {
                        game.togglePause()
                    }
                ) {
                    Text(if (game.paused) "Resume" else "Pause", size = 2f.em)
                }
            }
        }

        if (game.started) {
            Box(
                Modifier
//                    .fillMaxWidth()
//                    .fillMaxHeight(0.5f)
                    .background(Color(248, 248, 255))
                    .size(game.width.dp, game.height.dp)
                    .onSizeChanged {
                        game.size = it
                    }
            ) {
                game.pieces.forEachIndexed { index, piece ->
                    piece(index, piece)
                }
            }
        }

        LaunchedEffect(Unit) {
            while (true) {
                withFrameNanos {
                    if (game.started && !game.paused && !game.finished)
                        game.update(it)
                }
            }
        }
    }
}
