package com.instantcopy

import platform.UIKit.UIPasteboard
import platform.Foundation.NSUserDefaults
import platform.Darwin.dispatch_async
import platform.Darwin.dispatch_get_main_queue

actual class PlatformService {
    private val userDefaults = NSUserDefaults.standardUserDefaults

    actual fun getClipboardContent(): ClipboardContent? {
        return try {
            val pasteboard = UIPasteboard.generalPasteboard
            val text = pasteboard.string ?: return null
            ClipboardContent(text.toString())
        } catch (e: Exception) {
            null
        }
    }

    actual fun setClipboardContent(content: String) {
        try {
            val pasteboard = UIPasteboard.generalPasteboard
            dispatch_async(dispatch_get_main_queue()) {
                pasteboard.setString(content)
            }
        } catch (e: Exception) {
            // Silently fail
        }
    }

    actual fun saveSettings(settings: SettingsState) {
        userDefaults.setBool(settings.isEnabled, "enabled")
        userDefaults.setInteger(settings.sensitivity.toLong(), "sensitivity")
        userDefaults.setBool(settings.autoStart, "auto_start")
        userDefaults.synchronize()
    }

    actual fun loadSettings(): SettingsState {
        return SettingsState(
            isEnabled = userDefaults.boolForKey("enabled"),
            sensitivity = userDefaults.integerForKey("sensitivity"),
            autoStart = userDefaults.boolForKey("auto_start")
        )
    }

    actual fun startMonitoring() {
        // Background monitoring handled by UIApplicationDelegate
    }

    actual fun stopMonitoring() {
        // Background monitoring handled by UIApplicationDelegate
    }
}

actual fun getPlatformService(): PlatformService {
    return PlatformService()
}
