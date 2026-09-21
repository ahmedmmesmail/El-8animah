package com.codenytra.amme.el_8animah.features.pinlock

import android.content.Context
import androidx.core.content.edit
import java.security.MessageDigest

object PinManager {

    private const val PIN_KEY = "pin"
    private const val PIN_HASH = "hash"
    private const val PREFS = "el_8animah_pin"
    private const val BIOMETRIC_ENABLED = "biometric_enabled"

    fun hasPinSet(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return prefs.contains(PIN_KEY) && prefs.contains(PIN_HASH)
    }

    fun setPin(context: Context, pin: String) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val hash = sha256(pin)
        prefs.edit {
            putString(PIN_KEY, pin)
                .putString(PIN_HASH, hash)
        }
    }

    fun verifyPin(context: Context, pin: String): Boolean {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val savedHash = prefs.getString(PIN_HASH, null)
        return sha256(pin) == savedHash
    }

    fun clearPin(context: Context) {
        context
            .getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit {
                remove(PIN_HASH)
                    .putBoolean(BIOMETRIC_ENABLED, false)
                    .putBoolean(PIN_KEY, false)
            }
    }

    fun isBiometricEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return prefs.getBoolean(BIOMETRIC_ENABLED, false)
    }

    fun setBiometricEnabled(context: Context, enabled: Boolean) {
        context
            .getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit {
                putBoolean(BIOMETRIC_ENABLED, enabled)
            }
    }

    private fun sha256(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}