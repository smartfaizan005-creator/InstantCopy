package com.pressdetector.shared

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AppSettingsData(
    val isEnabled: Boolean = false,
    val pressDurationThreshold: Float = 0.5f,
    val autoStartEnabled: Boolean = false,
    val backgroundServiceEnabled: Boolean = false
)

interface AppSettingsRepository {
    val settings: StateFlow<AppSettingsData>
    
    fun setEnabled(enabled: Boolean)
    fun setPressDurationThreshold(threshold: Float)
    fun setAutoStartEnabled(enabled: Boolean)
    fun setBackgroundServiceEnabled(enabled: Boolean)
    
    fun onSettingsChanged(callback: (AppSettingsData) -> Unit)
}

class AppSettingsRepositoryImpl(
    private val settingsStorage: SettingsStorage
) : AppSettingsRepository {
    
    private val _settings = MutableStateFlow(loadSettings())
    override val settings: StateFlow<AppSettingsData> = _settings.asStateFlow()
    
    private var settingsChangedCallback: ((AppSettingsData) -> Unit)? = null
    
    private fun loadSettings(): AppSettingsData {
        return AppSettingsData(
            isEnabled = settingsStorage.getBoolean(KEY_ENABLED, false),
            pressDurationThreshold = settingsStorage.getFloat(KEY_THRESHOLD, 0.5f),
            autoStartEnabled = settingsStorage.getBoolean(KEY_AUTO_START, false),
            backgroundServiceEnabled = settingsStorage.getBoolean(KEY_BACKGROUND_SERVICE, false)
        )
    }
    
    override fun setEnabled(enabled: Boolean) {
        settingsStorage.putBoolean(KEY_ENABLED, enabled)
        updateSettings { it.copy(isEnabled = enabled) }
    }
    
    override fun setPressDurationThreshold(threshold: Float) {
        settingsStorage.putFloat(KEY_THRESHOLD, threshold)
        updateSettings { it.copy(pressDurationThreshold = threshold) }
    }
    
    override fun setAutoStartEnabled(enabled: Boolean) {
        settingsStorage.putBoolean(KEY_AUTO_START, enabled)
        updateSettings { it.copy(autoStartEnabled = enabled) }
    }
    
    override fun setBackgroundServiceEnabled(enabled: Boolean) {
        settingsStorage.putBoolean(KEY_BACKGROUND_SERVICE, enabled)
        updateSettings { it.copy(backgroundServiceEnabled = enabled) }
    }
    
    override fun onSettingsChanged(callback: (AppSettingsData) -> Unit) {
        settingsChangedCallback = callback
    }
    
    private fun updateSettings(update: (AppSettingsData) -> AppSettingsData) {
        val newSettings = update(_settings.value)
        _settings.value = newSettings
        settingsChangedCallback?.invoke(newSettings)
    }
    
    companion object {
        private const val KEY_ENABLED = "is_enabled"
        private const val KEY_THRESHOLD = "press_duration_threshold"
        private const val KEY_AUTO_START = "auto_start_enabled"
        private const val KEY_BACKGROUND_SERVICE = "background_service_enabled"
    }
}
