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
package androidx.compose.web.ww.demo

import androidx.compose.desktop.Window
import androidx.compose.ui.unit.IntSize
import org.jetbrains.compose.demo.falling.views.fallingBalls
import org.jetbrains.compose.demo.falling.Game
import androidx.compose.runtime.remember

class JvmGame : Game() {
    override fun saveTime() {
        previousTime = System.nanoTime()
    }
}

fun main() {
    Window(title = "Demo", size = IntSize(800, 800)) {
        //App()
        fallingBalls(remember { JvmGame() })
    }
}