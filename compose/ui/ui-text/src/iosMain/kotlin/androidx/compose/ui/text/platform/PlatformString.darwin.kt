package androidx.compose.ui.text.platform

import androidx.compose.ui.text.PlatformStringDelegate
import androidx.compose.ui.text.intl.PlatformLocale

internal actual fun ActualStringDelegate(): PlatformStringDelegate = DarwinStringDelegate()

internal class DarwinStringDelegate: PlatformStringDelegate {
    override fun toUpperCase(string: String, locale: PlatformLocale): String {
        return string.uppercase()
    }

    override fun toLowerCase(string: String, locale: PlatformLocale): String {
        return string.lowercase()
    }

    override fun capitalize(string: String, locale: PlatformLocale): String {
        return string.replaceFirstChar { it.uppercase() }
    }

    override fun decapitalize(string: String, locale: PlatformLocale): String {
        return string.replaceFirstChar { it.lowercase() }
    }

}