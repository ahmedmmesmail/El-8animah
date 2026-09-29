package com.codenytra.amme.el_8animah.features.appearance

import android.app.Application

// Custom Application class; must be registered in AndroidManifest.xml as android:name=".features.appearance.ElGhanimahApp"
class ElGhanimahApp : Application() {

    // App-wide ThemeViewModel instance, accessible by all Activities
    lateinit var themeViewModel: ThemeViewModel
        private set

    override fun onCreate() {
        super.onCreate()
        // Initialize the singleton ThemeViewModel with the Application context
        themeViewModel = ThemeViewModel(this)
        instance = this
    }

    companion object {
        // Global singleton accessor to the Application instance
        lateinit var instance: ElGhanimahApp
            private set
    }
}