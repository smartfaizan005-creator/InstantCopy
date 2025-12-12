package com.example.textselection.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.example.textselection.settings.PreferencesManager

class BootCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent?.action != Intent.ACTION_BOOT_COMPLETED) {
            return
        }

        val preferencesManager = PreferencesManager(context)

        // Only attempt to enable if the user had previously enabled the service
        if (preferencesManager.isServiceEnabled()) {
            // Check if the accessibility service is enabled in settings
            if (isAccessibilityServiceEnabled(context)) {
                // Service will auto-start if enabled in accessibility settings
                // No need to do anything here; the system will manage the service lifecycle
            }
        }
    }

    private fun isAccessibilityServiceEnabled(context: Context): Boolean {
        return try {
            val enabledServices = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            ) ?: return false

            enabledServices.contains("com.example.textselection/.service.TextSelectionAccessibilityService")
        } catch (e: Exception) {
            false
        }
    }
}
