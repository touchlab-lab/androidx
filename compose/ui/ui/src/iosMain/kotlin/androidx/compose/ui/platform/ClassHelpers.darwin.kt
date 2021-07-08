package androidx.compose.ui.platform

import platform.darwin.NSObject

internal actual fun Any.nativeClass(): Any = (this as? NSObject)?.`class`() as Any
