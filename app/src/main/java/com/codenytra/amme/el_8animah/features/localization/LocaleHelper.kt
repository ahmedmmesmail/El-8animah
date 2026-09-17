package com.codenytra.amme.el_8animah.features.localization

import android.content.Context
import android.content.res.Configuration
import androidx.core.content.edit
import java.util.Locale

/*
* LocaleHelper handles:
 - saving the user's chosen language in SharedPreferences
 - applying that language to a Context at runtime
* Works on Android 8+
*/

object LocaleHelper {

    private const val PREFS_NAME = "el_8animah_locale"
    private const val KEY_LOCALE = "selected_locale"
    private const val DEFAULT_LOCALE = "default"

    data class AppLanguage(
        val code: String,
        val displayName: String,
        val nativeName: String,
        val flag: String,
        val isRTL: Boolean = false
    )

    val supportedLanguages = listOf(
        AppLanguage("default", "System Default", "حسب لغة الجهاز", "⚙️"),
        AppLanguage("en", "English", "English", "🇬🇧"),
        AppLanguage("ar", "Arabic", "العربية", "🇸🇦", isRTL = true),
        AppLanguage("fr", "French", "Français", "🇫🇷"),
    )

    fun saveLocale(context: Context, localeCode: String) {
        context
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit {
                putString(KEY_LOCALE, localeCode)
            }
    }

    fun getSavedLocale(context: Context): String {
        return context
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_LOCALE, DEFAULT_LOCALE) ?: DEFAULT_LOCALE
    }

    // Builds a new Context with the given locale applied.
    // "default" means: keep whatever locale the device is already using.
    fun applyLocale(context: Context, localeCode: String): Context {
        val locale = when (localeCode) {
            "default" -> context.resources.configuration.locales[0]
            else -> Locale.forLanguageTag(localeCode.replace('_', '-'))
        }
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale) // switches layout to RTL/LTR based on the locale

        return context.createConfigurationContext(config)
    }

    // Reads the saved locale and applies it. Used from Activity.attachBaseContext.
    fun wrap(context: Context): Context {
        val code = getSavedLocale(context)
        return applyLocale(context, code)
    }


    /*
    // Bonus helpers for localizing numbers

    // Converts Western digits (0-9) to Arabic-Indic digits when the language is Arabic
    fun String.localizeNumbers(languageCode: String): String {
        if (!languageCode.startsWith("ar")) return this

        return this
            .replace('0', '٠')
            .replace('1', '١')
            .replace('2', '٢')
            .replace('3', '٣')
            .replace('4', '٤')
            .replace('5', '٥')
            .replace('6', '٦')
            .replace('7', '٧')
            .replace('8', '٨')
            .replace('9', '٩')
    }

    // NOTE: this overload does not use the receiver string — it just returns
    // the currently saved locale code
    fun String.localizeNumbers(context: Context): String {
        return getSavedLocale(context)
    }
*/
}