package com.instantcopy

expect class PlatformService {
    fun getClipboardContent(): ClipboardContent?
    fun setClipboardContent(content: String)
    fun saveSettings(settings: SettingsState)
    fun loadSettings(): SettingsState
    fun startMonitoring()
    fun stopMonitoring()
}

expect fun getPlatformService(): PlatformService
