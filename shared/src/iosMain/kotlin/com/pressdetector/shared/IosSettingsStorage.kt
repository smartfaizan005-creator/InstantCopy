package com.pressdetector.shared

import platform.Foundation.NSUserDefaults

class IosSettingsStorage : SettingsStorage {
    private val userDefaults = NSUserDefaults.standardUserDefaults
    
    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        if (!userDefaults.objectForKey(key)) {
            return defaultValue
        }
        return userDefaults.boolForKey(key)
    }
    
    override fun putBoolean(key: String, value: Boolean) {
        userDefaults.setBool(value, key)
        userDefaults.synchronize()
    }
    
    override fun getFloat(key: String, defaultValue: Float): Float {
        if (!userDefaults.objectForKey(key)) {
            return defaultValue
        }
        return userDefaults.floatForKey(key)
    }
    
    override fun putFloat(key: String, value: Float) {
        userDefaults.setFloat(value, key)
        userDefaults.synchronize()
    }
    
    override fun getString(key: String, defaultValue: String): String {
        return userDefaults.stringForKey(key) ?: defaultValue
    }
    
    override fun putString(key: String, value: String) {
        userDefaults.setObject(value, key)
        userDefaults.synchronize()
    }
}
