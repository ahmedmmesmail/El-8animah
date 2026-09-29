# Appearance & Dynamic Theming Feature

A modern, production-grade theming engine built with **Jetpack Compose** and **Material 3**. It supports system/light/dark modes, Material You Dynamic Colors (Android 12+), custom seed color palettes, and an OLED Pure Black mode.

---

## 🌟 Key Features

- 🌓 **Theme Modes**: Supports `SYSTEM` (follow OS), `LIGHT`, and `DARK`.
- 🎨 **Dynamic Color (Material You)**: Extracts system palette on Android 12+ (API 31+).
- 🌈 **Custom Color Schemes**: 12+ curated accent seed colors (Indigo, Teal, Amber, Rose, etc.) generating full dynamic tonal palettes.
- 🖤 **AMOLED Pure Black Mode**: Deep pure black (`#000000`) background for OLED displays and battery savings.
- 💾 **Persistent Settings**: Instant persistence using `SharedPreferences` and reactive UI state via Kotlin `StateFlow`.

---

## 📁 File Structure

```text
features/appearance/
├── AppearanceActivity.kt  # Compose UI for theme & color configuration
├── ElGhanimahApp.kt       # Application class initializing theme state
├── ThemePreferences.kt    # SharedPreferences persistence layer
└── ThemeViewModel.kt      # StateFlow / ViewModel for reactive updates
```

---

## 🚀 How to Implement in Your Own Project

### 1. Add Dependencies

In your module `build.gradle.kts`:

```kotlin
dependencies {
    implementation("androidx.compose.material3:material3:1.3.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
    implementation("androidx.core:core-ktx:1.13.1")
}
```

### 2. Copy the Preference Layer (`ThemePreferences.kt`)

`ThemePreferences` manages saving and retrieving user preferences:

```kotlin
enum class ThemeMode { SYSTEM, LIGHT, DARK }

object ThemePreferences {
    private const val PREFS_NAME = "theme_prefs"

    fun getThemeMode(context: Context): ThemeMode = ...
    fun setThemeMode(context: Context, themeMode: ThemeMode) = ...
    fun getDynamicColor(context: Context): Boolean = ...
    fun setDynamicColor(context: Context, enabled: Boolean) = ...
    fun getBlackTheme(context: Context): Boolean = ...
    fun setBlackTheme(context: Context, enabled: Boolean) = ...
    fun getSeedColor(context: Context): Color? = ...
    fun setSeedColor(context: Context, color: Color?) = ...
}
```

### 3. Implement the ViewModel (`ThemeViewModel.kt`)

Use `StateFlow` to notify your root Compose hierarchy of theme changes in real time:

```kotlin
class ThemeViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext

    private val _themeMode = MutableStateFlow(ThemePreferences.getThemeMode(context))
    val themeMode: StateFlow<ThemeMode> = _themeMode

    private val _dynamicColor = MutableStateFlow(ThemePreferences.getDynamicColor(context))
    val dynamicColor: StateFlow<Boolean> = _dynamicColor

    private val _blackTheme = MutableStateFlow(ThemePreferences.getBlackTheme(context))
    val blackTheme: StateFlow<Boolean> = _blackTheme

    private val _seedColor = MutableStateFlow(ThemePreferences.getSeedColor(context))
    val seedColor: StateFlow<Color?> = _seedColor

    fun setThemeMode(mode: ThemeMode) {
        ThemePreferences.setThemeMode(context, mode)
        _themeMode.value = mode
    }

    fun setDynamicColor(enabled: Boolean) {
        ThemePreferences.setDynamicColor(context, enabled)
        _dynamicColor.value = enabled
    }

    fun setBlackTheme(enabled: Boolean) {
        ThemePreferences.setBlackTheme(context, enabled)
        _blackTheme.value = enabled
    }

    fun setSeedColor(color: Color?) {
        ThemePreferences.setSeedColor(context, color)
        _seedColor.value = color
    }
}
```

### 4. Apply to your App Theme composable

In your `ui/theme/Theme.kt`:

```kotlin
@Composable
fun AppTheme(
    themeViewModel: ThemeViewModel = viewModel(),
    content: @Composable () -> Unit
) {
    val themeMode by themeViewModel.themeMode.collectAsState()
    val dynamicColor by themeViewModel.dynamicColor.collectAsState()
    val blackTheme by themeViewModel.blackTheme.collectAsState()
    val seedColor by themeViewModel.seedColor.collectAsState()

    val context = LocalContext.current
    val isDark = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (isDark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        seedColor != null -> {
            // Generate tonal scheme from seed color or use custom mapping
            if (isDark) darkColorScheme(primary = seedColor!!) else lightColorScheme(primary = seedColor!!)
        }
        isDark -> darkColorScheme()
        else -> lightColorScheme()
    }.let { scheme ->
        if (isDark && blackTheme) {
            scheme.copy(surface = Color.Black, background = Color.Black)
        } else scheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
```

### 5. Launch `AppearanceActivity`

To let users customize their appearance, start the activity:

```kotlin
val intent = Intent(context, AppearanceActivity::class.java)
context.startActivity(intent)
```
