package com.instantcopy

data class SettingsState(
    val isEnabled: Boolean = true,
    val sensitivity: Int = 500,
    val autoStart: Boolean = true
)
