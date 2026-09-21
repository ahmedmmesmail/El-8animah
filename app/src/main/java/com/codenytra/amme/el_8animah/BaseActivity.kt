package com.codenytra.amme.el_8animah

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.codenytra.amme.el_8animah.features.localization.LocaleHelper

open class BaseActivity : FragmentActivity() {

    private var localeWhenCreated: String? = null

    override fun attachBaseContext(newBase: Context) {
        // Save the locale this Activity was started with
        localeWhenCreated = LocaleHelper.getSavedLocale(newBase)
        // LocaleHelper.wrap reads the saved locale and applies it to the Context
        super.attachBaseContext(LocaleHelper.wrap(newBase))
    }

    override fun onResume() {
        super.onResume()
        // Check if the currently saved locale differs from the one we started with.
        // A difference means the user changed the language in Settings,
        // so we recreate the Activity to reload strings in the correct language.
        val currentLocale = LocaleHelper.getSavedLocale(this)
        if (localeWhenCreated != null && localeWhenCreated != currentLocale) {
            recreate()
        }
    }
}