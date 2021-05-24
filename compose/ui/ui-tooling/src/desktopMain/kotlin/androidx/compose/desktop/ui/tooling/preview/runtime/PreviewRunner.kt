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

package androidx.compose.desktop.ui.tooling.preview.runtime

import androidx.compose.desktop.ComposeWindow
import androidx.compose.desktop.LocalAppWindow
import androidx.compose.desktop.Window
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.CommonPreviewUtils.invokeComposableViaReflection
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import java.awt.event.WindowListener
import java.awt.event.WindowEvent

class PreviewRunner {
    companion object {
        private var previewComposition: @Composable () -> Unit = {}

        private val location: MutableState<IntOffset> = mutableStateOf(IntOffset(0, 0))
        private val size: MutableState<IntSize> = mutableStateOf(IntSize(0, 0))
        private val isPanelVisible: MutableState<Boolean> = mutableStateOf(false)
        private val isIdeInFocus: MutableState<Boolean> = mutableStateOf(false)
        private val isMeInFocus: MutableState<Boolean> = mutableStateOf(false)

        @JvmStatic
        fun preview(fqName: String) {
            val className = fqName.substringBeforeLast(".")
            val methodName = fqName.substringAfterLast(".")

            previewComposition = @Composable {
                // We need to delay the reflection instantiation of the class until we are in the
                // composable to ensure all the right initialization has happened and the Composable
                // class loads correctly.
                invokeComposableViaReflection(
                    className,
                    methodName,
                    currentComposer
                )
            }
        }

        @JvmStatic
        fun locationAndSize(x: Int, y: Int, width: Int, height: Int) {
            location.value = IntOffset(x = x, y = y)
            size.value = IntSize(width = width, height = height)
        }

        @JvmStatic
        fun isPanelVisible(value: Boolean) {
            isPanelVisible.value = value
        }

        @JvmStatic
        fun isIdeInFocus(value: Boolean) {
            isIdeInFocus.value = value
        }

        @JvmStatic
        fun main(args: Array<String>) {
            preview(args[0])

            Window(undecorated = true, size = size.value, location = location.value) {
                val appWindow = LocalAppWindow.current
                val isPanelVisible = remember { isPanelVisible }
                val isIdeInFocus = remember { isIdeInFocus }
                val isMeInFocus = remember { isMeInFocus }
                //appWindow.window.setFocusableWindowState(false)

                LaunchedEffect(Unit) {
                    appWindow.window.addWindowListener(object : WindowListener {
                        override fun windowActivated(e: WindowEvent) {
                            isMeInFocus.value = true
                        }
                        override fun windowClosed(e: WindowEvent) {}
                        override fun windowClosing(e: WindowEvent) {}
                        override fun windowDeactivated(e: WindowEvent) {
                            isMeInFocus.value = false
                        }
                        override fun windowDeiconified(e: WindowEvent) {}
                        override fun windowIconified(e: WindowEvent) {}
                        override fun windowOpened(e: WindowEvent) {
                        }
                    })
                    isMeInFocus.value = appWindow.window.isActive()

                }
                LaunchedEffect(isPanelVisible.value, isIdeInFocus.value, isMeInFocus.value) {
                    val isVisible = isPanelVisible.value &&
                        (isIdeInFocus.value || isMeInFocus.value)
                    appWindow.window.isAlwaysOnTop = isVisible
                    appWindow.window.setVisible(isVisible)
                }

                appWindow.setLocation(location.value.x, location.value.y)
                appWindow.setSize(size.value.width, size.value.height)
                previewComposition()
            }
        }
    }
}