package com.pressdetector.shared

interface SettingsStorage {
    fun getBoolean(key: String, defaultValue: Boolean): Boolean
    fun putBoolean(key: String, value: Boolean)
    
    fun getFloat(key: String, defaultValue: Float): Float
    fun putFloat(key: String, value: Float)
    
    fun getString(key: String, defaultValue: String): String
    fun putString(key: String, value: String)
}
