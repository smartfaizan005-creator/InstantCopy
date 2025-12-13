package com.instantcopy.settings

import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings
import android.widget.Toast

class SettingsManager(private val context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        MainActivity.PREFS_NAME, 
        Context.MODE_PRIVATE
    )
    
    companion object {
        private const val TAG = "SettingsManager"
    }
    
    fun updateAccessibilitySettings(enabled: Boolean, sensitivity: Int, autoStart: Boolean) {
        // Update SharedPreferences
        prefs.edit()
            .putBoolean(MainActivity.KEY_MASTER_ENABLED, enabled)
            .putInt(MainActivity.KEY_SENSITIVITY, sensitivity)
            .putBoolean(MainActivity.KEY_AUTO_START, autoStart)
            .apply()
        
        // Apply changes to accessibility service
        applyAccessibilitySettings(enabled, sensitivity)
        
        // Show feedback to user
        val status = if (enabled) "enabled" else "disabled"
        Toast.makeText(context, "InstantCopy $status", Toast.LENGTH_SHORT).show()
    }
    
    private fun applyAccessibilitySettings(enabled: Boolean, sensitivity: Int) {
        val serviceComponent = "${context.packageName}/.InstantCopyAccessibilityService"
        val enabledServices = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: ""
        
        val services = enabledServices.split(":").filter { it.isNotEmpty() }.toMutableSet()
        
        if (enabled) {
            services.add(serviceComponent)
            // Update service with current sensitivity settings
            // In a real implementation, you would update service configuration here
        } else {
            services.remove(serviceComponent)
        }
        
        val updatedServices = services.joinToString(":")
        Settings.Secure.putString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES,
            updatedServices
        )
        
        // Force settings to refresh
        Settings.Secure.putString(
            context.contentResolver,
            Settings.Secure.ACCESSIBILITY_ENABLED, 
            "1"
        )
    }
    
    fun getMasterEnabled(): Boolean {
        return prefs.getBoolean(MainActivity.KEY_MASTER_ENABLED, false)
    }
    
    fun getSensitivity(): Int {
        return prefs.getInt(MainActivity.KEY_SENSITIVITY, 1000)
    }
    
    fun getAutoStart(): Boolean {
        return prefs.getBoolean(MainActivity.KEY_AUTO_START, false)
    }
    
    fun setMasterEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(MainActivity.KEY_MASTER_ENABLED, enabled).apply()
    }
    
    fun setSensitivity(sensitivity: Int) {
        prefs.edit().putInt(MainActivity.KEY_SENSITIVITY, sensitivity).apply()
    }
    
    fun setAutoStart(enabled: Boolean) {
        prefs.edit().putBoolean(MainActivity.KEY_AUTO_START, enabled).apply()
    }
    
    fun isAccessibilityServiceEnabled(): Boolean {
        val serviceComponent = "${context.packageName}/.InstantCopyAccessibilityService"
        val enabledServices = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: ""
        
        return enabledServices.split(":").any { it == serviceComponent }
    }
}