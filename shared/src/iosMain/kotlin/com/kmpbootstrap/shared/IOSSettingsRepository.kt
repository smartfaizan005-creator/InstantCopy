package com.kmpbootstrap.shared

import platform.UIKit.UIPasteboard
import platform.UIKit.UIApplication
import platform.Foundation.NSUserDefaults
import platform.Foundation.NSBundle
import kotlinx.coroutines.*
import platform.darwin.NSObject

/**
 * iOS implementation of SettingsRepository using UserDefaults
 */
class IOSSettingsRepository : SettingsRepository {
    
    private val defaults = NSUserDefaults.standardUserDefaults
    
    override suspend fun getSettings(): SettingsState {
        return withContext(Dispatchers.Main) {
            val themeString = defaults.stringForKey("theme") ?: Theme.SYSTEM.name
            val language = defaults.stringForKey("language") ?: "en"
            val notificationsEnabled = defaults.boolForKey("notifications_enabled")
            val autoSave = defaults.boolForKey("auto_save")
            val debugMode = defaults.boolForKey("debug_mode")
            
            SettingsState(
                theme = try {
                    Theme.valueOf(themeString)
                } catch (e: Exception) {
                    Theme.SYSTEM
                },
                language = language,
                notificationsEnabled = notificationsEnabled,
                autoSave = autoSave,
                debugMode = debugMode
            )
        }
    }
    
    override suspend fun updateSettings(settings: SettingsState) {
        withContext(Dispatchers.Main) {
            defaults.setObject(settings.theme.name, forKey = "theme")
            defaults.setObject(settings.language, forKey = "language")
            defaults.setBool(settings.notificationsEnabled, forKey = "notifications_enabled")
            defaults.setBool(settings.autoSave, forKey = "auto_save")
            defaults.setBool(settings.debugMode, forKey = "debug_mode")
            defaults.synchronize()
        }
    }
    
    override suspend fun resetToDefaults() {
        withContext(Dispatchers.Main) {
            defaults.removePersistentDomainForName(NSBundle.mainBundle.bundleIdentifier ?: "com.kmpbootstrap.shared")
        }
    }
}