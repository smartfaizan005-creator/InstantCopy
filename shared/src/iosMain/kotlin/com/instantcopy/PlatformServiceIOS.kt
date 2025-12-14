package com.instantcopy

import platform.Darwin.dispatch_async
import platform.Darwin.dispatch_get_main_queue
import platform.Foundation.NSUserDefaults
import platform.UIKit.UIPasteboard

actual class PlatformService {
    private val userDefaults = NSUserDefaults.standardUserDefaults

    actual fun getClipboardContent(): ClipboardContent? {
        return try {
            val pasteboard = UIPasteboard.generalPasteboard
            val text = pasteboard.string ?: return null
            ClipboardContent(text)
        } catch (_: Exception) {
            null
        }
    }

    actual fun setClipboardContent(content: String) {
        try {
            val pasteboard = UIPasteboard.generalPasteboard
            dispatch_async(dispatch_get_main_queue()) {
                pasteboard.string = content
            }
        } catch (_: Exception) {
        }
    }

    actual fun saveSettings(settings: SettingsState) {
        userDefaults.setBool(settings.isEnabled, forKey = "enabled")
        userDefaults.setInteger(settings.sensitivity.toLong(), forKey = "sensitivity")
        userDefaults.setBool(settings.autoStart, forKey = "auto_start")
        userDefaults.synchronize()
    }

    actual fun loadSettings(): SettingsState {
        return SettingsState(
            isEnabled = userDefaults.boolForKey("enabled"),
            sensitivity = userDefaults.integerForKey("sensitivity").toInt(),
            autoStart = userDefaults.boolForKey("auto_start")
        )
    }

    actual fun startMonitoring() {
    }

    actual fun stopMonitoring() {
    }
}

actual fun getPlatformService(): PlatformService = PlatformService()
