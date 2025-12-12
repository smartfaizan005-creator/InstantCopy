package com.example.textselection.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class PreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    companion object {
        private const val KEY_SENSITIVITY_THRESHOLD = "sensitivity_threshold"
        private const val KEY_SERVICE_ENABLED = "service_enabled"
        private const val DEFAULT_SENSITIVITY_THRESHOLD = 300 // milliseconds
        private const val DEFAULT_SERVICE_ENABLED = true
    }

    fun getSensitivityThreshold(): Long {
        return try {
            sharedPreferences.getString(KEY_SENSITIVITY_THRESHOLD, DEFAULT_SENSITIVITY_THRESHOLD.toString())
                ?.toLongOrNull() ?: DEFAULT_SENSITIVITY_THRESHOLD.toLong()
        } catch (e: Exception) {
            DEFAULT_SENSITIVITY_THRESHOLD.toLong()
        }
    }

    fun setSensitivityThreshold(thresholdMs: Long) {
        sharedPreferences.edit().putString(KEY_SENSITIVITY_THRESHOLD, thresholdMs.toString()).apply()
    }

    fun isServiceEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_SERVICE_ENABLED, DEFAULT_SERVICE_ENABLED)
    }

    fun setServiceEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_SERVICE_ENABLED, enabled).apply()
    }
}
