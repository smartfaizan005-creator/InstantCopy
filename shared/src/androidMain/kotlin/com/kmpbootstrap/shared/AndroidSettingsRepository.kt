package com.kmpbootstrap.shared

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Android implementation of SettingsRepository
 */
class AndroidSettingsRepository(private val context: Context) : SettingsRepository {
    
    private val preferences: SharedPreferences by lazy {
        context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
    }
    
    override suspend fun getSettings(): SettingsState = withContext(Dispatchers.IO) {
        val themeString = preferences.getString("theme", Theme.SYSTEM.name) ?: Theme.SYSTEM.name
        val language = preferences.getString("language", "en") ?: "en"
        val notificationsEnabled = preferences.getBoolean("notifications_enabled", true)
        val autoSave = preferences.getBoolean("auto_save", true)
        val debugMode = preferences.getBoolean("debug_mode", false)
        
        SettingsState(
            theme = Theme.valueOf(themeString),
            language = language,
            notificationsEnabled = notificationsEnabled,
            autoSave = autoSave,
            debugMode = debugMode
        )
    }
    
    override suspend fun updateSettings(settings: SettingsState) = withContext(Dispatchers.IO) {
        preferences.edit()
            .putString("theme", settings.theme.name)
            .putString("language", settings.language)
            .putBoolean("notifications_enabled", settings.notificationsEnabled)
            .putBoolean("auto_save", settings.autoSave)
            .putBoolean("debug_mode", settings.debugMode)
            .apply()
    }
    
    override suspend fun resetToDefaults() = withContext(Dispatchers.IO) {
        preferences.edit().clear().apply()
    }
}