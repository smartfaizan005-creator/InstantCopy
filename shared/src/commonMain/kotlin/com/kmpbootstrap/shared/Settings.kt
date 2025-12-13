package com.kmpbootstrap.shared

/**
 * Data class representing application settings state
 */
data class SettingsState(
    val theme: Theme = Theme.SYSTEM,
    val language: String = "en",
    val notificationsEnabled: Boolean = true,
    val autoSave: Boolean = true,
    val debugMode: Boolean = false
)

/**
 * Enum representing available themes
 */
enum class Theme {
    LIGHT,
    DARK,
    SYSTEM
}

/**
 * Settings repository interface for platform-specific implementations
 */
interface SettingsRepository {
    suspend fun getSettings(): SettingsState
    suspend fun updateSettings(settings: SettingsState)
    suspend fun resetToDefaults()
}

/**
 * Settings use case for business logic
 */
class SettingsUseCase(private val repository: SettingsRepository) {
    suspend fun getSettings(): SettingsState = repository.getSettings()
    
    suspend fun updateTheme(theme: Theme) {
        val currentSettings = repository.getSettings()
        repository.updateSettings(currentSettings.copy(theme = theme))
    }
    
    suspend fun toggleNotifications() {
        val currentSettings = repository.getSettings()
        repository.updateSettings(currentSettings.copy(notificationsEnabled = !currentSettings.notificationsEnabled))
    }
    
    suspend fun toggleDebugMode() {
        val currentSettings = repository.getSettings()
        repository.updateSettings(currentSettings.copy(debugMode = !currentSettings.debugMode))
    }
}