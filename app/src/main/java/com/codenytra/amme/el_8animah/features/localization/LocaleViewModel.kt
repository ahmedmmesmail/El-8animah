package com.codenytra.amme.el_8animah.features.localization

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LocaleViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = application.getSharedPreferences("el_8animah_locale", Context.MODE_PRIVATE)

    // Compose state — reading this in a Composable triggers recomposition on change
    var currentLocale by mutableStateOf(LocaleHelper.getSavedLocale(application))
        private set

    // StateFlow version of the same value, for non-Compose consumers
    private val _language = MutableStateFlow(prefs.getString("selected_locale", "default") ?: "default")
    val language: StateFlow<String> = _language

    // Saves the new locale to SharedPreferences and updates Compose state
    fun updateLocale(locale: String) {
        LocaleHelper.saveLocale(getApplication(), locale)
        currentLocale = locale
    }
}