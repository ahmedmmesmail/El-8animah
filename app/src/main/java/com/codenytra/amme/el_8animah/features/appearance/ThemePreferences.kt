package com.codenytra.amme.el_8animah.features.appearance

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.edit

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

// Manages persistent storage of theme preferences using SharedPreferences
object ThemePreferences {
    private const val PREFS_NAME = "theme_prefs"

    private const val KEY_THEME_MODE  = "theme_mode"
    private const val KEY_DYNAMIC     = "dynamic_color"
    private const val KEY_BLACK       = "black_theme"
    private const val KEY_SEED_COLOR  = "seed_color"

    fun getThemeMode(context: Context): ThemeMode {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val saved = prefs.getString(KEY_THEME_MODE, ThemeMode.SYSTEM.name)
        return try {
            enumValueOf(saved ?: ThemeMode.SYSTEM.name)
        } catch (_: IllegalArgumentException) {
           ThemeMode.SYSTEM
        }
    }

    fun getDynamicColor(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_DYNAMIC, false)
    }

    fun getBlackTheme(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_BLACK, false)
    }

    fun getSeedColor(context: Context): Color? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val colorInt = prefs.getInt(KEY_SEED_COLOR, 0)
        return if (colorInt == 0) null else Color(colorInt)
    }

    fun setThemeMode(context: Context, themeMode: ThemeMode) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit { putString(KEY_THEME_MODE, themeMode.name) }
    }

    fun setDynamicColor(context: Context, enabled: Boolean) {
        context
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit {
                putBoolean(KEY_DYNAMIC, enabled)
            }
    }

    fun setBlackTheme(context: Context, enabled: Boolean) {
        context
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit {
                putBoolean(KEY_BLACK, enabled)
            }
    }

    fun setSeedColor(context: Context, color: Color?) {
        context
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit {
                putInt(KEY_SEED_COLOR, color?.toArgb() ?: 0)
            }
    }

    fun reset(context: Context) {
        context
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit {
                clear()
            }
    }

}