package androidx.compose.ui.graphics

actual fun BlendMode.isSupported(): Boolean {
    return this == BlendMode.SrcOver
}