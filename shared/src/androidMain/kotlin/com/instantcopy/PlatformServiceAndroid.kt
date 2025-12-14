package com.instantcopy

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

actual class PlatformService actual constructor() {
    companion object {
        private var applicationContext: Context? = null

        fun initialize(ctx: Context) {
            applicationContext = ctx.applicationContext
        }
    }

    private val context: Context
        get() = applicationContext ?: throw IllegalStateException("PlatformService not initialized")

    private val clipboardManager: ClipboardManager
        get() = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    private val sharedPreferences
        get() = context.getSharedPreferences("instantcopy_prefs", Context.MODE_PRIVATE)

    actual fun getClipboardContent(): ClipboardContent? {
        return try {
            val clip = clipboardManager.primaryClip
            if (clip != null && clip.itemCount > 0) {
                val text = clip.getItemAt(0).text?.toString() ?: return null
                ClipboardContent(text)
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }

    actual fun setClipboardContent(content: String) {
        try {
            val clip = ClipData.newPlainText("InstantCopy", content)
            clipboardManager.setPrimaryClip(clip)
        } catch (_: Exception) {
        }
    }

    actual fun saveSettings(settings: SettingsState) {
        sharedPreferences.edit().apply {
            putBoolean("enabled", settings.isEnabled)
            putInt("sensitivity", settings.sensitivity)
            putBoolean("auto_start", settings.autoStart)
            apply()
        }
    }

    actual fun loadSettings(): SettingsState {
        return SettingsState(
            isEnabled = sharedPreferences.getBoolean("enabled", true),
            sensitivity = sharedPreferences.getInt("sensitivity", 500),
            autoStart = sharedPreferences.getBoolean("auto_start", true)
        )
    }

    actual fun startMonitoring() {
    }

    actual fun stopMonitoring() {
    }
}

actual fun getPlatformService(): PlatformService = PlatformService()
