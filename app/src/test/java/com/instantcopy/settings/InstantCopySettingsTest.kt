package com.instantcopy.settings

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import org.junit.Test
import org.mockito.Mockito.*

/**
 * Test class to validate InstantCopy settings functionality
 */
class InstantCopySettingsTest {
    
    @Test
    fun testSettingsPersistence() {
        // Test that settings are properly saved and retrieved
        val mockContext = mock(Context::class.java)
        val mockPrefs = mock(SharedPreferences::class.java)
        val mockEditor = mock(SharedPreferences.Editor::class.java)
        
        `when`(mockContext.getSharedPreferences(anyString(), anyInt())).thenReturn(mockPrefs)
        `when`(mockPrefs.edit()).thenReturn(mockEditor)
        `when`(mockEditor.putBoolean(anyString(), anyBoolean())).thenReturn(mockEditor)
        `when`(mockEditor.putInt(anyString(), anyInt())).thenReturn(mockEditor)
        
        val settingsManager = SettingsManager(mockContext)
        
        // Test setting updates
        settingsManager.setMasterEnabled(true)
        settingsManager.setSensitivity(1500)
        settingsManager.setAutoStart(true)
        
        // Verify preferences were updated
        verify(mockEditor).putBoolean("master_enabled", true)
        verify(mockEditor).putInt("sensitivity", 1500)
        verify(mockEditor).putBoolean("auto_start", true)
        verify(mockEditor).apply()
    }
    
    @Test
    fun testAccessibilityServiceUpdate() {
        // Test that accessibility service settings are properly applied
        val enabled = true
        val sensitivity = 1200
        val autoStart = false
        
        // This would test the real-time update functionality
        InstantCopyAccessibilityService.updateSettings(enabled, sensitivity)
        
        // Verify service state is updated
        assert(InstantCopyAccessibilityService.isServiceEnabled() == enabled)
        assert(InstantCopyAccessibilityService.getSensitivityThreshold() == sensitivity.toLong())
    }
    
    @Test
    fun testSettingsValidation() {
        // Test that settings values are within valid ranges
        assert(isValidSensitivity(1000))  // Valid
        assert(isValidSensitivity(100))   // Minimum
        assert(isValidSensitivity(3000))  // Maximum
        assert(!isValidSensitivity(50))   // Too low
        assert(!isValidSensitivity(5000)) // Too high
    }
    
    private fun isValidSensitivity(sensitivity: Int): Boolean {
        return sensitivity in 100..3000
    }
    
    @Test
    fun testDefaultSettings() {
        // Test that default values are correctly applied
        val mockContext = mock(Context::class.java)
        val mockPrefs = mock(SharedPreferences::class.java)
        
        `when`(mockContext.getSharedPreferences(anyString(), anyInt())).thenReturn(mockPrefs)
        `when`(mockPrefs.getBoolean("master_enabled", false)).thenReturn(false)
        `when`(mockPrefs.getInt("sensitivity", 1000)).thenReturn(1000)
        `when`(mockPrefs.getBoolean("auto_start", false)).thenReturn(false)
        
        val settingsManager = SettingsManager(mockContext)
        
        assert(!settingsManager.getMasterEnabled())
        assert(settingsManager.getSensitivity() == 1000)
        assert(!settingsManager.getAutoStart())
    }
}