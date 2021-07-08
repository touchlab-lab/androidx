package androidx.compose.ui.input.key

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.util.packInts
import platform.UIKit.UIKeyboardHIDUsage
import platform.UIKit.UIKeyboardHIDUsageKeyboardA
import platform.UIKit.UIKeyboardHIDUsageKeyboardB
import platform.UIKit.UIKeyboardHIDUsageKeyboardC
import platform.UIKit.UIKeyboardHIDUsageKeyboardD
import platform.UIKit.UIKeyboardHIDUsageKeyboardE
import platform.UIKit.UIKeyboardHIDUsageKeyboardF
import platform.UIKit.UIKeyboardHIDUsageKeyboardG
import platform.UIKit.UIKeyboardHIDUsageKeyboardH
import platform.UIKit.UIKeyboardHIDUsageKeyboardI
import platform.UIKit.UIKeyboardHIDUsageKeyboardJ
import platform.UIKit.UIKeyboardHIDUsageKeyboardK
import platform.UIKit.UIKeyboardHIDUsageKeyboardL
import platform.UIKit.UIKeyboardHIDUsageKeyboardM
import platform.UIKit.UIKeyboardHIDUsageKeyboardN
import platform.UIKit.UIKeyboardHIDUsageKeyboardO
import platform.UIKit.UIKeyboardHIDUsageKeyboardP
import platform.UIKit.UIKeyboardHIDUsageKeyboardQ
import platform.UIKit.UIKeyboardHIDUsageKeyboardR
import platform.UIKit.UIKeyboardHIDUsageKeyboardS
import platform.UIKit.UIKeyboardHIDUsageKeyboardT
import platform.UIKit.UIKeyboardHIDUsageKeyboardU
import platform.UIKit.UIKeyboardHIDUsageKeyboardV
import platform.UIKit.UIKeyboardHIDUsageKeyboardW
import platform.UIKit.UIKeyboardHIDUsageKeyboardX
import platform.UIKit.UIKeyboardHIDUsageKeyboardY
import platform.UIKit.UIKeyboardHIDUsageKeyboardZ
import platform.UIKit.UIKeyboardHIDUsageKeyboard0
import platform.UIKit.UIKeyboardHIDUsageKeyboard1
import platform.UIKit.UIKeyboardHIDUsageKeyboard2
import platform.UIKit.UIKeyboardHIDUsageKeyboard3
import platform.UIKit.UIKeyboardHIDUsageKeyboard4
import platform.UIKit.UIKeyboardHIDUsageKeyboard5
import platform.UIKit.UIKeyboardHIDUsageKeyboard6
import platform.UIKit.UIKeyboardHIDUsageKeyboard7
import platform.UIKit.UIKeyboardHIDUsageKeyboard8
import platform.UIKit.UIKeyboardHIDUsageKeyboard9
import platform.UIKit.UIKeyboardHIDUsageKeyboardUpArrow
import platform.UIKit.UIKeyboardHIDUsageKeyboardDownArrow
import platform.UIKit.UIKeyboardHIDUsageKeyboardLeftArrow
import platform.UIKit.UIKeyboardHIDUsageKeyboardRightArrow
import platform.UIKit.UIKeyboardHIDUsageKeyboardPower
import platform.UIKit.UIKeyboardHIDUsageKeyboard_Reserved
import platform.UIKit.UIKeyboardHIDUsageKeyboardLeftGUI
import platform.UIKit.UIKeyboardHIDUsageKeyboardRightGUI
import platform.UIKit.UIKeyboardHIDUsageKeyboardHome
import platform.UIKit.UIKeyboardHIDUsageKeyboardEnd
import platform.UIKit.UIKeyboardHIDUsageKeyboardDeleteOrBackspace
import platform.UIKit.UIKeyboardHIDUsageKeyboardDeleteForward
import platform.UIKit.UIKeyboardHIDUsageKeyboardEscape
import platform.UIKit.UIKeyboardHIDUsageKeyboardHelp
import platform.UIKit.UIKeyboardHIDUsageKeypad0
import platform.UIKit.UIKeyboardHIDUsageKeypad1
import platform.UIKit.UIKeyboardHIDUsageKeypad2
import platform.UIKit.UIKeyboardHIDUsageKeypad3
import platform.UIKit.UIKeyboardHIDUsageKeypad4
import platform.UIKit.UIKeyboardHIDUsageKeypad5
import platform.UIKit.UIKeyboardHIDUsageKeypad6
import platform.UIKit.UIKeyboardHIDUsageKeypad7
import platform.UIKit.UIKeyboardHIDUsageKeypad8
import platform.UIKit.UIKeyboardHIDUsageKeypad9
import platform.UIKit.UIKeyboardHIDUsageKeypadPeriod
import platform.UIKit.UIKeyboardHIDUsageKeyboardNonUSPound
import platform.UIKit.UIKeyboardHIDUsageKeypadEqualSign
import platform.UIKit.UIKeyboardHIDUsageKeyboardClear
import platform.UIKit.UIKeyboardHIDUsageKeypadPlus
import platform.UIKit.UIKeyboardHIDUsageKeypadHyphen
import platform.UIKit.UIKeyboardHIDUsageKeypadAsterisk


actual inline class Key(val keyCode: Long) {
    actual companion object {
        /** Unknown key. */
        @ExperimentalComposeUiApi
        actual val Unknown = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Soft Left key.
         *
         * Usually situated below the display on phones and used as a multi-function
         * feature key for selecting a software defined function shown on the bottom left
         * of the display.
         */
        @ExperimentalComposeUiApi
        actual val SoftLeft = Key(UIKeyboardHIDUsageKeyboardLeftGUI)

        /**
         * Soft Right key.
         *
         * Usually situated below the display on phones and used as a multi-function
         * feature key for selecting a software defined function shown on the bottom right
         * of the display.
         */
        @ExperimentalComposeUiApi
        actual val SoftRight = Key(UIKeyboardHIDUsageKeyboardRightGUI)

        /**
         * Home key.
         *
         * This key is handled by the framework and is never delivered to applications.
         */
        @ExperimentalComposeUiApi
        actual val Home = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Back key. */
        @ExperimentalComposeUiApi
        actual val Back = Key(UIKeyboardHIDUsageKeyboardEscape)

        /** Help key. */
        @ExperimentalComposeUiApi
        actual val Help = Key(UIKeyboardHIDUsageKeyboardHelp)

        /**
         * Navigate to previous key.
         *
         * Goes backward by one item in an ordered collection of items.
         */
        @ExperimentalComposeUiApi
        actual val NavigatePrevious = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Navigate to next key.
         *
         * Advances to the next item in an ordered collection of items.
         */
        @ExperimentalComposeUiApi
        actual val NavigateNext = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Navigate in key.
         *
         * Activates the item that currently has focus or expands to the next level of a navigation
         * hierarchy.
         */
        @ExperimentalComposeUiApi
        actual val NavigateIn = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Navigate out key.
         *
         * Backs out one level of a navigation hierarchy or collapses the item that currently has
         * focus.
         */
        @ExperimentalComposeUiApi
        actual val NavigateOut = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Consumed by the system for navigation up. */
        @ExperimentalComposeUiApi
        actual val SystemNavigationUp = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Consumed by the system for navigation down. */
        @ExperimentalComposeUiApi
        actual val SystemNavigationDown = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Consumed by the system for navigation left. */
        @ExperimentalComposeUiApi
        actual val SystemNavigationLeft = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Consumed by the system for navigation right. */
        @ExperimentalComposeUiApi
        actual val SystemNavigationRight = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Call key. */
        @ExperimentalComposeUiApi
        actual val Call = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** End Call key. */
        @ExperimentalComposeUiApi
        actual val EndCall = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Up Arrow Key / Directional Pad Up key.
         *
         * May also be synthesized from trackball motions.
         */
        @ExperimentalComposeUiApi
        actual val DirectionUp = Key(UIKeyboardHIDUsageKeyboardUpArrow)

        /**
         * Down Arrow Key / Directional Pad Down key.
         *
         * May also be synthesized from trackball motions.
         */
        @ExperimentalComposeUiApi
        actual val DirectionDown = Key(UIKeyboardHIDUsageKeyboardDownArrow)

        /**
         * Left Arrow Key / Directional Pad Left key.
         *
         * May also be synthesized from trackball motions.
         */
        @ExperimentalComposeUiApi
        actual val DirectionLeft = Key(UIKeyboardHIDUsageKeyboardLeftArrow)

        /**
         * Right Arrow Key / Directional Pad Right key.
         *
         * May also be synthesized from trackball motions.
         */
        @ExperimentalComposeUiApi
        actual val DirectionRight = Key(UIKeyboardHIDUsageKeyboardRightArrow)

        /**
         * Center Arrow Key / Directional Pad Center key.
         *
         * May also be synthesized from trackball motions.
         */
        @ExperimentalComposeUiApi
        actual val DirectionCenter = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Directional Pad Up-Left. */
        @ExperimentalComposeUiApi
        actual val DirectionUpLeft = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Directional Pad Down-Left. */
        @ExperimentalComposeUiApi
        actual val DirectionDownLeft = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Directional Pad Up-Right. */
        @ExperimentalComposeUiApi
        actual val DirectionUpRight = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Directional Pad Down-Right. */
        @ExperimentalComposeUiApi
        actual val DirectionDownRight = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Volume Up key.
         *
         * Adjusts the speaker volume up.
         */
        @ExperimentalComposeUiApi
        actual val VolumeUp = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Volume Down key.
         *
         * Adjusts the speaker volume down.
         */
        @ExperimentalComposeUiApi
        actual val VolumeDown = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Power key.  */
        @ExperimentalComposeUiApi
        actual val Power = Key(UIKeyboardHIDUsageKeyboardPower)

        /**
         * Camera key.
         *
         * Used to launch a camera application or take pictures.
         */
        @ExperimentalComposeUiApi
        actual val Camera = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Clear key. */
        @ExperimentalComposeUiApi
        actual val Clear = Key(UIKeyboardHIDUsageKeyboardClear)

        /** '0' key. */
        @ExperimentalComposeUiApi
        actual val Zero = Key(UIKeyboardHIDUsageKeyboard0)

        /** '1' key. */
        @ExperimentalComposeUiApi
        actual val One = Key(UIKeyboardHIDUsageKeyboard1)

        /** '2' key. */
        @ExperimentalComposeUiApi
        actual val Two = Key(UIKeyboardHIDUsageKeyboard2)

        /** '3' key. */
        @ExperimentalComposeUiApi
        actual val Three = Key(UIKeyboardHIDUsageKeyboard3)

        /** '4' key. */
        @ExperimentalComposeUiApi
        actual val Four = Key(UIKeyboardHIDUsageKeyboard4)

        /** '5' key. */
        @ExperimentalComposeUiApi
        actual val Five = Key(UIKeyboardHIDUsageKeyboard5)

        /** '6' key. */
        @ExperimentalComposeUiApi
        actual val Six = Key(UIKeyboardHIDUsageKeyboard6)

        /** '7' key. */
        @ExperimentalComposeUiApi
        actual val Seven = Key(UIKeyboardHIDUsageKeyboard7)

        /** '8' key. */
        @ExperimentalComposeUiApi
        actual val Eight = Key(UIKeyboardHIDUsageKeyboard8)

        /** '9' key. */
        @ExperimentalComposeUiApi
        actual val Nine = Key(UIKeyboardHIDUsageKeyboard9)

        /** '+' key. */
        @ExperimentalComposeUiApi
        actual val Plus = Key(UIKeyboardHIDUsageKeypadPlus)

        /** '-' key. */
        @ExperimentalComposeUiApi
        actual val Minus = Key(UIKeyboardHIDUsageKeypadHyphen)

        /** '*' key. */
        @ExperimentalComposeUiApi
        actual val Multiply = Key(UIKeyboardHIDUsageKeypadAsterisk)

        /** '=' key. */
        @ExperimentalComposeUiApi
        actual val Equals = Key(UIKeyboardHIDUsageKeypadEqualSign)

        /** '#' key. */
        @ExperimentalComposeUiApi
        actual val Pound = Key(UIKeyboardHIDUsageKeyboardNonUSPound)

        /** 'A' key. */
        @ExperimentalComposeUiApi
        actual val A = Key(UIKeyboardHIDUsageKeyboardA)

        /** 'B' key. */
        @ExperimentalComposeUiApi
        actual val B = Key(UIKeyboardHIDUsageKeyboardB)

        /** 'C' key. */
        @ExperimentalComposeUiApi
        actual val C = Key(UIKeyboardHIDUsageKeyboardC)

        /** 'D' key. */
        @ExperimentalComposeUiApi
        actual val D = Key(UIKeyboardHIDUsageKeyboardD)

        /** 'E' key. */
        @ExperimentalComposeUiApi
        actual val E = Key(UIKeyboardHIDUsageKeyboardE)

        /** 'F' key. */
        @ExperimentalComposeUiApi
        actual val F = Key(UIKeyboardHIDUsageKeyboardF)

        /** 'G' key. */
        @ExperimentalComposeUiApi
        actual val G = Key(UIKeyboardHIDUsageKeyboardG)

        /** 'H' key. */
        @ExperimentalComposeUiApi
        actual val H = Key(UIKeyboardHIDUsageKeyboardH)

        /** 'I' key. */
        @ExperimentalComposeUiApi
        actual val I = Key(UIKeyboardHIDUsageKeyboardI)

        /** 'J' key. */
        @ExperimentalComposeUiApi
        actual val J = Key(UIKeyboardHIDUsageKeyboardJ)

        /** 'K' key. */
        @ExperimentalComposeUiApi
        actual val K = Key(UIKeyboardHIDUsageKeyboardK)

        /** 'L' key. */
        @ExperimentalComposeUiApi
        actual val L = Key(UIKeyboardHIDUsageKeyboardL)

        /** 'M' key. */
        @ExperimentalComposeUiApi
        actual val M = Key(UIKeyboardHIDUsageKeyboardM)

        /** 'N' key. */
        @ExperimentalComposeUiApi
        actual val N = Key(UIKeyboardHIDUsageKeyboardN)

        /** 'O' key. */
        @ExperimentalComposeUiApi
        actual val O = Key(UIKeyboardHIDUsageKeyboardO)

        /** 'P' key. */
        @ExperimentalComposeUiApi
        actual val P = Key(UIKeyboardHIDUsageKeyboardP)

        /** 'Q' key. */
        @ExperimentalComposeUiApi
        actual val Q = Key(UIKeyboardHIDUsageKeyboardQ)

        /** 'R' key. */
        @ExperimentalComposeUiApi
        actual val R = Key(UIKeyboardHIDUsageKeyboardR)

        /** 'S' key. */
        @ExperimentalComposeUiApi
        actual val S = Key(UIKeyboardHIDUsageKeyboardS)

        /** 'T' key. */
        @ExperimentalComposeUiApi
        actual val T = Key(UIKeyboardHIDUsageKeyboardT)

        /** 'U' key. */
        @ExperimentalComposeUiApi
        actual val U = Key(UIKeyboardHIDUsageKeyboardU)

        /** 'V' key. */
        @ExperimentalComposeUiApi
        actual val V = Key(UIKeyboardHIDUsageKeyboardV)

        /** 'W' key. */
        @ExperimentalComposeUiApi
        actual val W = Key(UIKeyboardHIDUsageKeyboardW)

        /** 'X' key. */
        @ExperimentalComposeUiApi
        actual val X = Key(UIKeyboardHIDUsageKeyboardX)

        /** 'Y' key. */
        @ExperimentalComposeUiApi
        actual val Y = Key(UIKeyboardHIDUsageKeyboardY)

        /** 'Z' key. */
        @ExperimentalComposeUiApi
        actual val Z = Key(UIKeyboardHIDUsageKeyboardZ)

        /** ',' key. */
        @ExperimentalComposeUiApi
        actual val Comma = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** '.' key. */
        @ExperimentalComposeUiApi
        actual val Period = Key(UIKeyboardHIDUsageKeypadPeriod)

        /** Left Alt modifier key. */
        @ExperimentalComposeUiApi
        actual val AltLeft = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Right Alt modifier key. */
        @ExperimentalComposeUiApi
        actual val AltRight = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Left Shift modifier key. */
        @ExperimentalComposeUiApi
        actual val ShiftLeft = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Right Shift modifier key. */
        @ExperimentalComposeUiApi
        actual val ShiftRight = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Tab key. */
        @ExperimentalComposeUiApi
        actual val Tab = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Space key. */
        @ExperimentalComposeUiApi
        actual val Spacebar = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Symbol modifier key.
         *
         * Used to enter alternate symbols.
         */
        @ExperimentalComposeUiApi
        actual val Symbol = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Browser special function key.
         *
         * Used to launch a browser application.
         */
        @ExperimentalComposeUiApi
        actual val Browser = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Envelope special function key.
         *
         * Used to launch a mail application.
         */
        @ExperimentalComposeUiApi
        actual val Envelope = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Enter key. */
        @ExperimentalComposeUiApi
        actual val Enter = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Backspace key.
         *
         * Deletes characters before the insertion point, unlike [Delete].
         */
        @ExperimentalComposeUiApi
        actual val Backspace = Key(UIKeyboardHIDUsageKeyboardDeleteOrBackspace)

        /**
         * Delete key.
         *
         * Deletes characters ahead of the insertion point, unlike [Backspace].
         */
        @ExperimentalComposeUiApi
        actual val Delete = Key(UIKeyboardHIDUsageKeyboardDeleteForward)

        /** Escape key. */
        @ExperimentalComposeUiApi
        actual val Escape = Key(UIKeyboardHIDUsageKeyboardEscape)

        /** Left Control modifier key. */
        @ExperimentalComposeUiApi
        actual val CtrlLeft = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Right Control modifier key. */
        @ExperimentalComposeUiApi
        actual val CtrlRight = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Caps Lock key. */
        @ExperimentalComposeUiApi
        actual val CapsLock = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Scroll Lock key. */
        @ExperimentalComposeUiApi
        actual val ScrollLock = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Left Meta modifier key. */
        @ExperimentalComposeUiApi
        actual val MetaLeft = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Right Meta modifier key. */
        @ExperimentalComposeUiApi
        actual val MetaRight = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Function modifier key. */
        @ExperimentalComposeUiApi
        actual val Function = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** System Request / Print Screen key. */
        @ExperimentalComposeUiApi
        actual val PrintScreen = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Break / Pause key. */
        @ExperimentalComposeUiApi
        actual val Break = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Home Movement key.
         *
         * Used for scrolling or moving the cursor around to the start of a line
         * or to the top of a list.
         */
        @ExperimentalComposeUiApi
        actual val MoveHome = Key(UIKeyboardHIDUsageKeyboardHome)

        /**
         * End Movement key.
         *
         * Used for scrolling or moving the cursor around to the end of a line
         * or to the bottom of a list.
         */
        @ExperimentalComposeUiApi
        actual val MoveEnd = Key(UIKeyboardHIDUsageKeyboardEnd)

        /**
         * Insert key.
         *
         * Toggles insert / overwrite edit mode.
         */
        @ExperimentalComposeUiApi
        actual val Insert = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Cut key. */
        @ExperimentalComposeUiApi
        actual val Cut = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Copy key. */
        @ExperimentalComposeUiApi
        actual val Copy = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Paste key. */
        @ExperimentalComposeUiApi
        actual val Paste = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** '`' (backtick) key. */
        @ExperimentalComposeUiApi
        actual val Grave = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** '[' key. */
        @ExperimentalComposeUiApi
        actual val LeftBracket = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** ']' key. */
        @ExperimentalComposeUiApi
        actual val RightBracket = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** '/' key. */
        @ExperimentalComposeUiApi
        actual val Slash = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** '\' key. */
        @ExperimentalComposeUiApi
        actual val Backslash = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** ';' key. */
        @ExperimentalComposeUiApi
        actual val Semicolon = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** ''' (apostrophe) key. */
        @ExperimentalComposeUiApi
        actual val Apostrophe = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** '@' key. */
        @ExperimentalComposeUiApi
        actual val At = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Number modifier key.
         *
         * Used to enter numeric symbols.
         * This key is not Num Lock; it is more like  [AltLeft].
         */
        @ExperimentalComposeUiApi
        actual val Number = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Headset Hook key.
         *
         * Used to hang up calls and stop media.
         */
        @ExperimentalComposeUiApi
        actual val HeadsetHook = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Camera Focus key.
         *
         * Used to focus the camera.
         */
        @ExperimentalComposeUiApi
        actual val Focus = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Menu key. */
        @ExperimentalComposeUiApi
        actual val Menu = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Notification key. */
        @ExperimentalComposeUiApi
        actual val Notification = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Search key. */
        @ExperimentalComposeUiApi
        actual val Search = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Page Up key. */
        @ExperimentalComposeUiApi
        actual val PageUp = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Page Down key. */
        @ExperimentalComposeUiApi
        actual val PageDown = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Picture Symbols modifier key.
         *
         * Used to switch symbol sets (Emoji, Kao-moji).
         */
        @ExperimentalComposeUiApi
        actual val PictureSymbols = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Switch Charset modifier key.
         *
         * Used to switch character sets (Kanji, Katakana).
         */
        @ExperimentalComposeUiApi
        actual val SwitchCharset = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * A Button key.
         *
         * On a game controller, the A button should be either the button labeled A
         * or the first button on the bottom row of controller buttons.
         */
        @ExperimentalComposeUiApi
        actual val ButtonA = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * B Button key.
         *
         * On a game controller, the B button should be either the button labeled B
         * or the second button on the bottom row of controller buttons.
         */
        @ExperimentalComposeUiApi
        actual val ButtonB = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * C Button key.
         *
         * On a game controller, the C button should be either the button labeled C
         * or the third button on the bottom row of controller buttons.
         */
        @ExperimentalComposeUiApi
        actual val ButtonC = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * X Button key.
         *
         * On a game controller, the X button should be either the button labeled X
         * or the first button on the upper row of controller buttons.
         */
        @ExperimentalComposeUiApi
        actual val ButtonX = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Y Button key.
         *
         * On a game controller, the Y button should be either the button labeled Y
         * or the second button on the upper row of controller buttons.
         */
        @ExperimentalComposeUiApi
        actual val ButtonY = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Z Button key.
         *
         * On a game controller, the Z button should be either the button labeled Z
         * or the third button on the upper row of controller buttons.
         */
        @ExperimentalComposeUiApi
        actual val ButtonZ = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * L1 Button key.
         *
         * On a game controller, the L1 button should be either the button labeled L1 (or L)
         * or the top left trigger button.
         */
        @ExperimentalComposeUiApi
        actual val ButtonL1 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * R1 Button key.
         *
         * On a game controller, the R1 button should be either the button labeled R1 (or R)
         * or the top right trigger button.
         */
        @ExperimentalComposeUiApi
        actual val ButtonR1 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * L2 Button key.
         *
         * On a game controller, the L2 button should be either the button labeled L2
         * or the bottom left trigger button.
         */
        @ExperimentalComposeUiApi
        actual val ButtonL2 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * R2 Button key.
         *
         * On a game controller, the R2 button should be either the button labeled R2
         * or the bottom right trigger button.
         */
        @ExperimentalComposeUiApi
        actual val ButtonR2 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Left Thumb Button key.
         *
         * On a game controller, the left thumb button indicates that the left (or only)
         * joystick is pressed.
         */
        @ExperimentalComposeUiApi
        actual val ButtonThumbLeft = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Right Thumb Button key.
         *
         * On a game controller, the right thumb button indicates that the right
         * joystick is pressed.
         */
        @ExperimentalComposeUiApi
        actual val ButtonThumbRight = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Start Button key.
         *
         * On a game controller, the button labeled Start.
         */
        @ExperimentalComposeUiApi
        actual val ButtonStart = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Select Button key.
         *
         * On a game controller, the button labeled Select.
         */
        @ExperimentalComposeUiApi
        actual val ButtonSelect = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Mode Button key.
         *
         * On a game controller, the button labeled Mode.
         */
        @ExperimentalComposeUiApi
        actual val ButtonMode = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Generic Game Pad Button #1. */
        @ExperimentalComposeUiApi
        actual val Button1 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Generic Game Pad Button #2. */
        @ExperimentalComposeUiApi
        actual val Button2 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Generic Game Pad Button #3. */
        @ExperimentalComposeUiApi
        actual val Button3 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Generic Game Pad Button #4. */
        @ExperimentalComposeUiApi
        actual val Button4 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Generic Game Pad Button #5. */
        @ExperimentalComposeUiApi
        actual val Button5 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Generic Game Pad Button #6. */
        @ExperimentalComposeUiApi
        actual val Button6 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Generic Game Pad Button #7. */
        @ExperimentalComposeUiApi
        actual val Button7 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Generic Game Pad Button #8. */
        @ExperimentalComposeUiApi
        actual val Button8 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Generic Game Pad Button #9. */
        @ExperimentalComposeUiApi
        actual val Button9 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Generic Game Pad Button #10. */
        @ExperimentalComposeUiApi
        actual val Button10 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Generic Game Pad Button #11. */
        @ExperimentalComposeUiApi
        actual val Button11 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Generic Game Pad Button #12. */
        @ExperimentalComposeUiApi
        actual val Button12 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Generic Game Pad Button #13. */
        @ExperimentalComposeUiApi
        actual val Button13 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Generic Game Pad Button #14. */
        @ExperimentalComposeUiApi
        actual val Button14 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Generic Game Pad Button #15. */
        @ExperimentalComposeUiApi
        actual val Button15 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Generic Game Pad Button #16. */
        @ExperimentalComposeUiApi
        actual val Button16 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Forward key.
         *
         * Navigates forward in the history stack. Complement of [Back].
         */
        @ExperimentalComposeUiApi
        actual val Forward = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** F1 key. */
        @ExperimentalComposeUiApi
        actual val F1 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** F2 key. */
        @ExperimentalComposeUiApi
        actual val F2 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** F3 key. */
        @ExperimentalComposeUiApi
        actual val F3 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** F4 key. */
        @ExperimentalComposeUiApi
        actual val F4 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** F5 key. */
        @ExperimentalComposeUiApi
        actual val F5 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** F6 key. */
        @ExperimentalComposeUiApi
        actual val F6 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** F7 key. */
        @ExperimentalComposeUiApi
        actual val F7 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** F8 key. */
        @ExperimentalComposeUiApi
        actual val F8 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** F9 key. */
        @ExperimentalComposeUiApi
        actual val F9 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** F10 key. */
        @ExperimentalComposeUiApi
        actual val F10 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** F11 key. */
        @ExperimentalComposeUiApi
        actual val F11 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** F12 key. */
        @ExperimentalComposeUiApi
        actual val F12 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Num Lock key.
         *
         * This is the Num Lock key; it is different from [Number].
         * This key alters the behavior of other keys on the numeric keypad.
         */
        @ExperimentalComposeUiApi
        actual val NumLock = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Numeric keypad '0' key. */
        @ExperimentalComposeUiApi
        actual val NumPad0 = Key(UIKeyboardHIDUsageKeypad0)

        /** Numeric keypad '1' key. */
        @ExperimentalComposeUiApi
        actual val NumPad1 = Key(UIKeyboardHIDUsageKeypad1)

        /** Numeric keypad '2' key. */
        @ExperimentalComposeUiApi
        actual val NumPad2 = Key(UIKeyboardHIDUsageKeypad2)

        /** Numeric keypad '3' key. */
        @ExperimentalComposeUiApi
        actual val NumPad3 = Key(UIKeyboardHIDUsageKeypad3)

        /** Numeric keypad '4' key. */
        @ExperimentalComposeUiApi
        actual val NumPad4 = Key(UIKeyboardHIDUsageKeypad4)

        /** Numeric keypad '5' key. */
        @ExperimentalComposeUiApi
        actual val NumPad5 = Key(UIKeyboardHIDUsageKeypad5)

        /** Numeric keypad '6' key. */
        @ExperimentalComposeUiApi
        actual val NumPad6 = Key(UIKeyboardHIDUsageKeypad6)

        /** Numeric keypad '7' key. */
        @ExperimentalComposeUiApi
        actual val NumPad7 = Key(UIKeyboardHIDUsageKeypad7)

        /** Numeric keypad '8' key. */
        @ExperimentalComposeUiApi
        actual val NumPad8 = Key(UIKeyboardHIDUsageKeypad8)

        /** Numeric keypad '9' key. */
        @ExperimentalComposeUiApi
        actual val NumPad9 = Key(UIKeyboardHIDUsageKeypad9)

        /** Numeric keypad '/' key (for division). */
        @ExperimentalComposeUiApi
        actual val NumPadDivide = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Numeric keypad '*' key (for multiplication). */
        @ExperimentalComposeUiApi
        actual val NumPadMultiply = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Numeric keypad '-' key (for subtraction). */
        @ExperimentalComposeUiApi
        actual val NumPadSubtract = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Numeric keypad '+' key (for addition). */
        @ExperimentalComposeUiApi
        actual val NumPadAdd = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Numeric keypad '.' key (for decimals or digit grouping). */
        @ExperimentalComposeUiApi
        actual val NumPadDot = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Numeric keypad ',' key (for decimals or digit grouping). */
        @ExperimentalComposeUiApi
        actual val NumPadComma = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Numeric keypad Enter key. */
        @ExperimentalComposeUiApi
        actual val NumPadEnter = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Numeric keypad '=' key. */
        @ExperimentalComposeUiApi
        actual val NumPadEquals = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Numeric keypad '(' key. */
        @ExperimentalComposeUiApi
        actual val NumPadLeftParenthesis = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Numeric keypad ')' key. */
        @ExperimentalComposeUiApi
        actual val NumPadRightParenthesis = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Play media key. */
        @ExperimentalComposeUiApi
        actual val MediaPlay = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Pause media key. */
        @ExperimentalComposeUiApi
        actual val MediaPause = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Play/Pause media key. */
        @ExperimentalComposeUiApi
        actual val MediaPlayPause = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Stop media key. */
        @ExperimentalComposeUiApi
        actual val MediaStop = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Record media key. */
        @ExperimentalComposeUiApi
        actual val MediaRecord = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Play Next media key. */
        @ExperimentalComposeUiApi
        actual val MediaNext = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Play Previous media key. */
        @ExperimentalComposeUiApi
        actual val MediaPrevious = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Rewind media key. */
        @ExperimentalComposeUiApi
        actual val MediaRewind = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Fast Forward media key. */
        @ExperimentalComposeUiApi
        actual val MediaFastForward = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Close media key.
         *
         * May be used to close a CD tray, for example.
         */
        @ExperimentalComposeUiApi
        actual val MediaClose = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Audio Track key.
         *
         * Switches the audio tracks.
         */
        @ExperimentalComposeUiApi
        actual val MediaAudioTrack = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Eject media key.
         *
         * May be used to eject a CD tray, for example.
         */
        @ExperimentalComposeUiApi
        actual val MediaEject = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Media Top Menu key.
         *
         * Goes to the top of media menu.
         */
        @ExperimentalComposeUiApi
        actual val MediaTopMenu = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Skip forward media key. */
        @ExperimentalComposeUiApi
        actual val MediaSkipForward = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Skip backward media key. */
        @ExperimentalComposeUiApi
        actual val MediaSkipBackward = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Step forward media key.
         *
         * Steps media forward, one frame at a time.
         */
        @ExperimentalComposeUiApi
        actual val MediaStepForward = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Step backward media key.
         *
         * Steps media backward, one frame at a time.
         */
        @ExperimentalComposeUiApi
        actual val MediaStepBackward = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Mute key.
         *
         * Mutes the microphone, unlike [VolumeMute].
         */
        @ExperimentalComposeUiApi
        actual val MicrophoneMute = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Volume Mute key.
         *
         * Mutes the speaker, unlike [MicrophoneMute].
         *
         * This key should normally be implemented as a toggle such that the first press
         * mutes the speaker and the second press restores the original volume.
         */
        @ExperimentalComposeUiApi
        actual val VolumeMute = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Info key.
         *
         * Common on TV remotes to show additional information related to what is
         * currently being viewed.
         */
        @ExperimentalComposeUiApi
        actual val Info = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Channel up key.
         *
         * On TV remotes, increments the television channel.
         */
        @ExperimentalComposeUiApi
        actual val ChannelUp = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Channel down key.
         *
         * On TV remotes, decrements the television channel.
         */
        @ExperimentalComposeUiApi
        actual val ChannelDown = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Zoom in key. */
        @ExperimentalComposeUiApi
        actual val ZoomIn = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Zoom out key. */
        @ExperimentalComposeUiApi
        actual val ZoomOut = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * TV key.
         *
         * On TV remotes, switches to viewing live TV.
         */
        @ExperimentalComposeUiApi
        actual val Tv = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Window key.
         *
         * On TV remotes, toggles picture-in-picture mode or other windowing functions.
         * On Android Wear devices, triggers a display offset.
         */
        @ExperimentalComposeUiApi
        actual val Window = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Guide key.
         *
         * On TV remotes, shows a programming guide.
         */
        @ExperimentalComposeUiApi
        actual val Guide = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * DVR key.
         *
         * On some TV remotes, switches to a DVR mode for recorded shows.
         */
        @ExperimentalComposeUiApi
        actual val Dvr = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Bookmark key.
         *
         * On some TV remotes, bookmarks content or web pages.
         */
        @ExperimentalComposeUiApi
        actual val Bookmark = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Toggle captions key.
         *
         * Switches the mode for closed-captioning text, for example during television shows.
         */
        @ExperimentalComposeUiApi
        actual val Captions = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Settings key.
         *
         * Starts the system settings activity.
         */
        @ExperimentalComposeUiApi
        actual val Settings = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * TV power key.
         *
         * On TV remotes, toggles the power on a television screen.
         */
        @ExperimentalComposeUiApi
        actual val TvPower = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * TV input key.
         *
         * On TV remotes, switches the input on a television screen.
         */
        @ExperimentalComposeUiApi
        actual val TvInput = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Set-top-box power key.
         *
         * On TV remotes, toggles the power on an external Set-top-box.
         */
        @ExperimentalComposeUiApi
        actual val SetTopBoxPower = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Set-top-box input key.
         *
         * On TV remotes, switches the input mode on an external Set-top-box.
         */
        @ExperimentalComposeUiApi
        actual val SetTopBoxInput = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * A/V Receiver power key.
         *
         * On TV remotes, toggles the power on an external A/V Receiver.
         */
        @ExperimentalComposeUiApi
        actual val AvReceiverPower = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * A/V Receiver input key.
         *
         * On TV remotes, switches the input mode on an external A/V Receiver.
         */
        @ExperimentalComposeUiApi
        actual val AvReceiverInput = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Red "programmable" key.
         *
         * On TV remotes, acts as a contextual/programmable key.
         */
        @ExperimentalComposeUiApi
        actual val ProgramRed = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Green "programmable" key.
         *
         * On TV remotes, acts as a contextual/programmable key.
         */
        @ExperimentalComposeUiApi
        actual val ProgramGreen = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Yellow "programmable" key.
         *
         * On TV remotes, acts as a contextual/programmable key.
         */
        @ExperimentalComposeUiApi
        actual val ProgramYellow = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Blue "programmable" key.
         *
         * On TV remotes, acts as a contextual/programmable key.
         */
        @ExperimentalComposeUiApi
        actual val ProgramBlue = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * App switch key.
         *
         * Should bring up the application switcher dialog.
         */
        @ExperimentalComposeUiApi
        actual val AppSwitch = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Language Switch key.
         *
         * Toggles the current input language such as switching between English and Japanese on
         * a QWERTY keyboard.  On some devices, the same function may be performed by
         * pressing Shift+Space.
         */
        @ExperimentalComposeUiApi
        actual val LanguageSwitch = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Manner Mode key.
         *
         * Toggles silent or vibrate mode on and off to make the device behave more politely
         * in certain settings such as on a crowded train.  On some devices, the key may only
         * operate when long-pressed.
         */
        @ExperimentalComposeUiApi
        actual val MannerMode = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * 3D Mode key.
         *
         * Toggles the display between 2D and 3D mode.
         */
        @ExperimentalComposeUiApi
        actual val Toggle2D3D = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Contacts special function key.
         *
         * Used to launch an address book application.
         */
        @ExperimentalComposeUiApi
        actual val Contacts = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Calendar special function key.
         *
         * Used to launch a calendar application.
         */
        @ExperimentalComposeUiApi
        actual val Calendar = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Music special function key.
         *
         * Used to launch a music player application.
         */
        @ExperimentalComposeUiApi
        actual val Music = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Calculator special function key.
         *
         * Used to launch a calculator application.
         */
        @ExperimentalComposeUiApi
        actual val Calculator = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Japanese full-width / half-width key. */
        @ExperimentalComposeUiApi
        actual val ZenkakuHankaru = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Japanese alphanumeric key. */
        @ExperimentalComposeUiApi
        actual val Eisu = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Japanese non-conversion key. */
        @ExperimentalComposeUiApi
        actual val Muhenkan = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Japanese conversion key. */
        @ExperimentalComposeUiApi
        actual val Henkan = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Japanese katakana / hiragana key. */
        @ExperimentalComposeUiApi
        actual val KatakanaHiragana = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Japanese Yen key. */
        @ExperimentalComposeUiApi
        actual val Yen = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Japanese Ro key. */
        @ExperimentalComposeUiApi
        actual val Ro = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Japanese kana key. */
        @ExperimentalComposeUiApi
        actual val Kana = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Assist key.
         *
         * Launches the global assist activity.  Not delivered to applications.
         */
        @ExperimentalComposeUiApi
        actual val Assist = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Brightness Down key.
         *
         * Adjusts the screen brightness down.
         */
        @ExperimentalComposeUiApi
        actual val BrightnessDown = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Brightness Up key.
         *
         * Adjusts the screen brightness up.
         */
        @ExperimentalComposeUiApi
        actual val BrightnessUp = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Sleep key.
         *
         * Puts the device to sleep. Behaves somewhat like [Power] but it
         * has no effect if the device is already asleep.
         */
        @ExperimentalComposeUiApi
        actual val Sleep = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Wakeup key.
         *
         * Wakes up the device.  Behaves somewhat like [Power] but it
         * has no effect if the device is already awake.
         */
        @ExperimentalComposeUiApi
        actual val WakeUp = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Put device to sleep unless a wakelock is held.  */
        @ExperimentalComposeUiApi
        actual val SoftSleep = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Pairing key.
         *
         * Initiates peripheral pairing mode. Useful for pairing remote control
         * devices or game controllers, especially if no other input mode is
         * available.
         */
        @ExperimentalComposeUiApi
        actual val Pairing = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Last Channel key.
         *
         * Goes to the last viewed channel.
         */
        @ExperimentalComposeUiApi
        actual val LastChannel = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * TV data service key.
         *
         * Displays data services like weather, sports.
         */
        @ExperimentalComposeUiApi
        actual val TvDataService = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Voice Assist key.
         *
         * Launches the global voice assist activity. Not delivered to applications.
         */
        @ExperimentalComposeUiApi
        actual val VoiceAssist = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Radio key.
         *
         * Toggles TV service / Radio service.
         */
        @ExperimentalComposeUiApi
        actual val TvRadioService = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Teletext key.
         *
         * Displays Teletext service.
         */
        @ExperimentalComposeUiApi
        actual val TvTeletext = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Number entry key.
         *
         * Initiates to enter multi-digit channel number when each digit key is assigned
         * for selecting separate channel. Corresponds to Number Entry Mode (0x1D) of CEC
         * User Control Code.
         */
        @ExperimentalComposeUiApi
        actual val TvNumberEntry = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Analog Terrestrial key.
         *
         * Switches to analog terrestrial broadcast service.
         */
        @ExperimentalComposeUiApi
        actual val TvTerrestrialAnalog = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Digital Terrestrial key.
         *
         * Switches to digital terrestrial broadcast service.
         */
        @ExperimentalComposeUiApi
        actual val TvTerrestrialDigital = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Satellite key.
         *
         * Switches to digital satellite broadcast service.
         */
        @ExperimentalComposeUiApi
        actual val TvSatellite = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * BS key.
         *
         * Switches to BS digital satellite broadcasting service available in Japan.
         */
        @ExperimentalComposeUiApi
        actual val TvSatelliteBs = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * CS key.
         *
         * Switches to CS digital satellite broadcasting service available in Japan.
         */
        @ExperimentalComposeUiApi
        actual val TvSatelliteCs = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * BS/CS key.
         *
         * Toggles between BS and CS digital satellite services.
         */
        @ExperimentalComposeUiApi
        actual val TvSatelliteService = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Toggle Network key.
         *
         * Toggles selecting broadcast services.
         */
        @ExperimentalComposeUiApi
        actual val TvNetwork = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Antenna/Cable key.
         *
         * Toggles broadcast input source between antenna and cable.
         */
        @ExperimentalComposeUiApi
        actual val TvAntennaCable = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * HDMI #1 key.
         *
         * Switches to HDMI input #1.
         */
        @ExperimentalComposeUiApi
        actual val TvInputHdmi1 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * HDMI #2 key.
         *
         * Switches to HDMI input #2.
         */
        @ExperimentalComposeUiApi
        actual val TvInputHdmi2 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * HDMI #3 key.
         *
         * Switches to HDMI input #3.
         */
        @ExperimentalComposeUiApi
        actual val TvInputHdmi3 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * HDMI #4 key.
         *
         * Switches to HDMI input #4.
         */
        @ExperimentalComposeUiApi
        actual val TvInputHdmi4 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Composite #1 key.
         *
         * Switches to composite video input #1.
         */
        @ExperimentalComposeUiApi
        actual val TvInputComposite1 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Composite #2 key.
         *
         * Switches to composite video input #2.
         */
        @ExperimentalComposeUiApi
        actual val TvInputComposite2 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Component #1 key.
         *
         * Switches to component video input #1.
         */
        @ExperimentalComposeUiApi
        actual val TvInputComponent1 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Component #2 key.
         *
         * Switches to component video input #2.
         */
        @ExperimentalComposeUiApi
        actual val TvInputComponent2 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * VGA #1 key.
         *
         * Switches to VGA (analog RGB) input #1.
         */
        @ExperimentalComposeUiApi
        actual val TvInputVga1 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Audio description key.
         *
         * Toggles audio description off / on.
         */
        @ExperimentalComposeUiApi
        actual val TvAudioDescription = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Audio description mixing volume up key.
         *
         * Increase the audio description volume as compared with normal audio volume.
         */
        @ExperimentalComposeUiApi
        actual val TvAudioDescriptionMixingVolumeUp = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Audio description mixing volume down key.
         *
         * Lessen audio description volume as compared with normal audio volume.
         */
        @ExperimentalComposeUiApi
        actual val TvAudioDescriptionMixingVolumeDown = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Zoom mode key.
         *
         * Changes Zoom mode (Normal, Full, Zoom, Wide-zoom, etc.)
         */
        @ExperimentalComposeUiApi
        actual val TvZoomMode = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Contents menu key.
         *
         * Goes to the title list. Corresponds to Contents Menu (0x0B) of CEC User Control Code
         */
        @ExperimentalComposeUiApi
        actual val TvContentsMenu = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Media context menu key.
         *
         * Goes to the context menu of media contents. Corresponds to Media Context-sensitive
         * Menu (0x11) of CEC User Control Code.
         */
        @ExperimentalComposeUiApi
        actual val TvMediaContextMenu = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Timer programming key.
         *
         * Goes to the timer recording menu. Corresponds to Timer Programming (0x54) of
         * CEC User Control Code.
         */
        @ExperimentalComposeUiApi
        actual val TvTimerProgramming = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Primary stem key for Wearables.
         *
         * Main power/reset button.
         */
        @ExperimentalComposeUiApi
        actual val StemPrimary = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Generic stem key 1 for Wearables. */
        @ExperimentalComposeUiApi
        actual val Stem1 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Generic stem key 2 for Wearables. */
        @ExperimentalComposeUiApi
        actual val Stem2 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Generic stem key 3 for Wearables. */
        @ExperimentalComposeUiApi
        actual val Stem3 = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Show all apps. */
        @ExperimentalComposeUiApi
        actual val AllApps = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Refresh key. */
        @ExperimentalComposeUiApi
        actual val Refresh = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Thumbs up key. Apps can use this to let user up-vote content. */
        @ExperimentalComposeUiApi
        actual val ThumbsUp = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /** Thumbs down key. Apps can use this to let user down-vote content. */
        @ExperimentalComposeUiApi
        actual val ThumbsDown = Key(UIKeyboardHIDUsageKeyboard_Reserved)

        /**
         * Used to switch current [account][android.accounts.Account] that is
         * consuming content. May be consumed by system to set account globally.
         */
        @ExperimentalComposeUiApi
        actual val ProfileSwitch = Key(UIKeyboardHIDUsageKeyboard_Reserved)
    }

    actual override fun toString(): String = "Key code: $keyCode"
}
