package androidx.compose.ui.text.intl

import platform.Foundation.NSLocale
import platform.Foundation.countryCode
import platform.Foundation.languageCode
import platform.Foundation.localeIdentifier
import platform.Foundation.scriptCode
import platform.Foundation.preferredLanguages

internal actual fun createPlatformLocaleDelegate(): PlatformLocaleDelegate = DarwinLocaleDelegate()

internal class DarwinLocale(val nsLocale: NSLocale) : PlatformLocale {
    override val language: String
        get() = nsLocale.languageCode

    override val script: String
        get() = nsLocale.scriptCode ?: ""

    override val region: String
        get() = nsLocale.countryCode ?: ""

    override fun toLanguageTag(): String = nsLocale.localeIdentifier
}

internal class DarwinLocaleDelegate: PlatformLocaleDelegate {

    override val current: List<PlatformLocale>
        get() = NSLocale.preferredLanguages.map { DarwinLocale(NSLocale(localeIdentifier = it as String)) }

    override fun parseLanguageTag(languageTag: String): PlatformLocale =
        DarwinLocale(NSLocale(localeIdentifier = languageTag))
}