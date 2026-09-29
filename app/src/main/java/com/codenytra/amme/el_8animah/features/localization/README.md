# Localization & Runtime Language Switching Feature

A dynamic, multi-language localization framework for Android applications. It enables users to switch the app language at runtime on **Android 8.0+ (API 26+)** without requiring a full device reboot or changing system settings.

---

## 🌟 Key Features

- 🌍 **Multi-Language Support**: English (Default), Arabic (RTL), Spanish, French, German, Russian, Chinese (Simplified), Japanese, Hindi, Portuguese, Turkish, and Italian.
- 🔄 **Instant In-App Switching**: Context configuration wrapping using `attachBaseContext`.
- 🔁 **RTL & LTR Automatic Handling**: Automatically flips layout directions for Right-to-Left languages (such as Arabic) via `Configuration.setLayoutDirection()`.
- ⚙️ **System Default Fallback**: Provides a "System Default" option to seamlessly match the user's OS language.
- 📜 **Scrollable BottomSheet UI**: Beautiful Material 3 selection sheet with country flags and native language names.

---

## 📁 File Structure

```text
features/localization/
├── LanguagePickerSheet.kt  # Scrollable ModalBottomSheet for language selection
├── LocaleHelper.kt         # Core Context wrapper, SharedPreferences persistence, & RTL handler
└── LocaleViewModel.kt      # ViewModel observing the currently active language code
```

---

## 🚀 How to Implement in Your Own Project

### 1. Define `BaseActivity.kt`

Every Activity in your app should inherit from `BaseActivity` to dynamically apply the saved locale:

```kotlin
open class BaseActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: Context) {
        // Wraps the context with the saved locale
        super.attachBaseContext(LocaleHelper.wrap(newBase))
    }

    override fun onResume() {
        super.onResume()
        // If the user changed the language in another screen, recreate to refresh strings
        val currentSaved = LocaleHelper.getSavedLocale(this)
        val activeLocale = resources.configuration.locales[0].language
        if (currentSaved != "default" && currentSaved != activeLocale) {
            recreate()
        }
    }
}
```

---

### 2. Copy `LocaleHelper.kt`

`LocaleHelper` handles persisting user selection and creating localized contexts:

```kotlin
object LocaleHelper {
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
        AppLanguage("es", "Spanish", "Español", "🇪🇸"),
        AppLanguage("fr", "French", "Français", "🇫🇷"),
        AppLanguage("de", "German", "Deutsch", "🇩🇪"),
        AppLanguage("ru", "Russian", "Русский", "🇷🇺"),
        AppLanguage("zh", "Chinese", "中文 (简体)", "🇨🇳"),
        AppLanguage("ja", "Japanese", "日本語", "🇯🇵"),
        AppLanguage("hi", "Hindi", "हिन्दी", "🇮🇳"),
        AppLanguage("pt", "Portuguese", "Português", "🇵🇹"),
        AppLanguage("tr", "Turkish", "Türkçe", "🇹🇷"),
        AppLanguage("it", "Italian", "Italiano", "🇮🇹")
    )

    fun applyLocale(context: Context, localeCode: String): Context {
        val locale = when (localeCode) {
            "default" -> context.resources.configuration.locales[0]
            else -> Locale.forLanguageTag(localeCode.replace('_', '-'))
        }
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        return context.createConfigurationContext(config)
    }

    fun wrap(context: Context): Context {
        val code = getSavedLocale(context)
        return applyLocale(context, code)
    }
}
```

---

### 3. Add String Resource Folders

Under `app/src/main/res/`, create localized directories matching the language codes:
- `values/strings.xml` (Default / English)
- `values-ar/strings.xml` (Arabic)
- `values-es/strings.xml` (Spanish)
- `values-fr/strings.xml` (French)
- `values-de/strings.xml` (German)
- `values-ru/strings.xml` (Russian)
- `values-zh/strings.xml` (Chinese Simplified)
- `values-ja/strings.xml` (Japanese)
- `values-hi/strings.xml` (Hindi)
- `values-pt/strings.xml` (Portuguese)
- `values-tr/strings.xml` (Turkish)
- `values-it/strings.xml` (Italian)

---

### 4. Open the Language Picker Sheet in Compose

```kotlin
var showLanguageSheet by remember { mutableStateOf(false) }
val currentLocale = LocaleHelper.getSavedLocale(context)

if (showLanguageSheet) {
    LanguagePickerSheet(
        currentCode = currentLocale,
        onSelect = { selectedLang ->
            LocaleHelper.saveLocale(context, selectedLang.code)
            showLanguageSheet = false
            // Recreate activity to apply changes immediately
            (context as? Activity)?.recreate()
        },
        onDismiss = { showLanguageSheet = false }
    )
}
```
