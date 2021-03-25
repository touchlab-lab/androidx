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

package androidx.compose.web.attributes

import androidx.compose.web.events.WrappedEvent

open class EventsListenerBuilder {
    class Options {
        // TODO: add options for addEventListener

        companion object {
            val DEFAULT = Options()
        }
    }

    class EventListener(
        val event: String,
        val options: Options,
        val listener: (WrappedEvent) -> Unit
    )

    private val listeners = mutableListOf<EventListener>()

    fun onCopy(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(COPY, options, listener))
    }

    fun onCut(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(CUT, options, listener))
    }

    fun onPaste(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(PASTE, options, listener))
    }

    fun onContextMenu(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(CONTEXTMENU, options, listener))
    }

    fun onClick(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(CLICK, options, listener))
    }

    fun onDoubleClick(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(DBLCLICK, options, listener))
    }

    fun onInput(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(INPUT, options, listener))
    }

    fun onChange(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(CHANGE, options, listener))
    }

    fun onInvalid(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(INVALID, options, listener))
    }

    fun onSearch(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(SEARCH, options, listener))
    }

    fun onFocus(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(FOCUS, options, listener))
    }

    fun onBlur(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(BLUR, options, listener))
    }

    fun onFocusIn(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(FOCUSIN, options, listener))
    }

    fun onFocusOut(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(FOCUSOUT, options, listener))
    }

    fun onKeyDown(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(KEYDOWN, options, listener))
    }

    fun onKeyUp(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(KEYUP, options, listener))
    }

    fun onMouseDown(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(MOUSEDOWN, options, listener))
    }

    fun onMouseUp(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(MOUSEUP, options, listener))
    }

    fun onMouseEnter(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(MOUSEENTER, options, listener))
    }

    fun onMouseLeave(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(MOUSELEAVE, options, listener))
    }

    fun onMouseMove(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(MOUSEMOVE, options, listener))
    }

    fun onMouseOut(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(MOUSEOUT, options, listener))
    }

    fun onMouseOver(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(MOUSEOVER, options, listener))
    }

    fun onWheel(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(WHEEL, options, listener))
    }

    fun onScroll(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(SCROLL, options, listener))
    }

    fun onSelect(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(SELECT, options, listener))
    }

    fun onTouchCancel(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(TOUCHCANCEL, options, listener))
    }

    fun onTouchMove(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(TOUCHMOVE, options, listener))
    }

    fun onTouchEnd(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(TOUCHEND, options, listener))
    }

    fun onTouchStart(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(TOUCHSTART, options, listener))
    }

    fun onAnimationEnd(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(ANIMATIONEND, options, listener))
    }

    fun onAnimationIteration(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(ANIMATIONITERATION, options, listener))
    }

    fun onAnimationStart(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(ANIMATIONSTART, options, listener))
    }

    fun onBeforeInput(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(BEFOREINPUT, options, listener))
    }

    fun onDrag(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(DRAG, options, listener))
    }

    fun onDrop(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(DROP, options, listener))
    }

    fun onDragStart(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(DRAGSTART, options, listener))
    }

    fun onDragEnd(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(DRAGEND, options, listener))
    }

    fun onDragOver(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(DRAGOVER, options, listener))
    }

    fun onDragEnter(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(DRAGENTER, options, listener))
    }

    fun onDragLeave(options: Options = Options.DEFAULT, listener: (WrappedEvent) -> Unit) {
        listeners.add(EventListener(DRAGLEAVE, options, listener))
    }

    fun asList(): List<EventListener> = listeners

    fun addEventListener(
        eventName: String,
        options: Options = Options.DEFAULT,
        listener: (WrappedEvent) -> Unit
    ) {
        listeners.add(EventListener(eventName, options, listener))
    }

    companion object {
        const val COPY = "copy"
        const val CUT = "cut"
        const val PASTE = "paste"
        const val CONTEXTMENU = "contextmenu"

        const val CLICK = "click"
        const val DBLCLICK = "dblclick"
        const val FOCUS = "focus"
        const val BLUR = "blue"
        const val FOCUSIN = "focusin"
        const val FOCUSOUT = "focusout"

        const val KEYDOWN = "keydown"
        const val KEYUP = "keyup"
        const val MOUSEDOWN = "mousedown"
        const val MOUSEUP = "mouseup"
        const val MOUSEENTER = "mouseenter"
        const val MOUSELEAVE = "mouseleave"
        const val MOUSEMOVE = "mousemove"
        const val MOUSEOUT = "mouseout"
        const val MOUSEOVER = "mouseover"
        const val WHEEL = "wheel"
        const val SCROLL = "scroll"
        const val SELECT = "select"

        const val TOUCHCANCEL = "touchcancel"
        const val TOUCHEND = "touchend"
        const val TOUCHMOVE = "touchmove"
        const val TOUCHSTART = "touchstart"

        const val ANIMATIONCANCEL = "animationcancel" // firefox and safari only
        const val ANIMATIONEND = "animationend"
        const val ANIMATIONITERATION = "animationiteration"
        const val ANIMATIONSTART = "animationstart"

        const val BEFOREINPUT = "beforeinput"
        const val INPUT = "input"
        const val CHANGE = "change"
        const val INVALID = "invalid"
        const val SEARCH = "search"

        const val DRAG = "drag"
        const val DROP = "drop"
        const val DRAGSTART = "dragstart"
        const val DRAGEND = "dragend"
        const val DRAGOVER = "dragover"
        const val DRAGENTER = "dragenter"
        const val DRAGLEAVE = "dragleave"
    }
}