package androidx.compose.ui.platform

import platform.Foundation.NSString
import platform.Foundation.stringWithFormat
import kotlin.native.identityHashCode

internal actual fun simpleIdentityToString(obj: Any, name: String?): String {
    val className = name ?: obj::class.qualifiedName ?: obj::class.simpleName ?: obj::class.toString()

    return className + "@" + NSString.stringWithFormat("%07x", obj.identityHashCode()).toString()
}