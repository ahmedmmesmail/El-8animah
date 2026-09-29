package com.codenytra.amme.el_8animah.features.appearance

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel

// Holds reactive Compose state for theme preferences; changes trigger UI recomposition app-wide
class ThemeViewModel(application: Application) : AndroidViewModel(application) {

    // ─── Theme State (Observable by Compose) ──────────────
    var themeMode by mutableStateOf(ThemePreferences.getThemeMode(application))
        private set

    var dynamicColor by mutableStateOf(ThemePreferences.getDynamicColor(application))
        private set

    var blackTheme by mutableStateOf(ThemePreferences.getBlackTheme(application))
        private set

    var seedColor by mutableStateOf(ThemePreferences.getSeedColor(application))
        private set


    // ─── Theme Update Functions ───────────────────────────
    fun updateThemeMode(mode: ThemeMode) {
        themeMode = mode
        ThemePreferences.setThemeMode(getApplication(), mode)
    }

    fun updateDynamicColor(enabled: Boolean) {
        dynamicColor = enabled
        ThemePreferences.setDynamicColor(getApplication(), enabled)
        if (enabled) seedColor = null
    }

    fun updateBlackTheme(enabled: Boolean) {
        blackTheme = enabled
        ThemePreferences.setBlackTheme(getApplication(), enabled)
    }

    fun updateSeedColor(color: Color?) {
        seedColor = color
        ThemePreferences.setSeedColor(getApplication(), color)
        if (color != null) dynamicColor = false
    }

}
