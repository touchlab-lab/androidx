package androidx.compose.ui.input.key

import platform.darwin.NSObject

/**
 * The native Android [KeyEvent][NativeKeyEvent].
 */
actual typealias NativeKeyEvent = NSObject

/**
 * The key that was pressed.
 *
 * @sample androidx.compose.ui.samples.KeyEventIsAltPressedSample
 */
actual val KeyEvent.key: Key
    get() = TODO() // Key(nativeKeyEvent.keyCode)

/**
 * The UTF16 value corresponding to the key event that was pressed. The unicode character
 * takes into account any meta keys that are pressed (eg. Pressing shift results in capital
 * alphabets). The UTF16 value uses the
 * [U+n notation][http://www.unicode.org/reports/tr27/#notation] of the Unicode Standard.
 *
 * An [Int] is used instead of a [Char] so that we can support supplementary characters. The
 * Unicode Standard allows for characters whose representation requires more than 16 bits.
 * The range of legal code points is U+0000 to U+10FFFF, known as Unicode scalar value.
 *
 * The set of characters from U+0000 to U+FFFF is sometimes referred to as the Basic
 * Multilingual Plane (BMP). Characters whose code points are greater than U+FFFF are called
 * supplementary characters. In this representation, supplementary characters are represented
 * as a pair of char values, the first from the high-surrogates range, (\uD800-\uDBFF), the
 * second from the low-surrogates range (\uDC00-\uDFFF).
 */
actual val KeyEvent.utf16CodePoint: Int
    get() = TODO() // nativeKeyEvent.unicodeChar

/**
 * The [type][KeyEventType] of key event.
 *
 * @sample androidx.compose.ui.samples.KeyEventTypeSample
 */
actual val KeyEvent.type: KeyEventType
    get() = TODO() /* when (nativeKeyEvent.action) {
        android.view.KeyEvent.ACTION_DOWN -> KeyEventType.KeyDown
        android.view.KeyEvent.ACTION_UP -> KeyEventType.KeyUp
        else -> KeyEventType.Unknown
    }*/

/**
 * Indicates whether the Alt key is pressed.
 *
 * @sample androidx.compose.ui.samples.KeyEventIsAltPressedSample
 */
actual val KeyEvent.isAltPressed: Boolean
    get() = TODO() // nativeKeyEvent.isAltPressed

/**
 * Indicates whether the Ctrl key is pressed.
 *
 * @sample androidx.compose.ui.samples.KeyEventIsCtrlPressedSample
 */
actual val KeyEvent.isCtrlPressed: Boolean
    get() = TODO() // nativeKeyEvent.isCtrlPressed

/**
 * Indicates whether the Meta key is pressed.
 *
 * @sample androidx.compose.ui.samples.KeyEventIsMetaPressedSample
 */
actual val KeyEvent.isMetaPressed: Boolean
    get() = TODO() // nativeKeyEvent.isMetaPressed

/**
 * Indicates whether the Shift key is pressed.
 *
 * @sample androidx.compose.ui.samples.KeyEventIsShiftPressedSample
 */
actual val KeyEvent.isShiftPressed: Boolean
    get() = TODO() // nativeKeyEvent.isShiftPressed
