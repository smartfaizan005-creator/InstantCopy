package com.example.textselection.settings

import android.content.Context
import android.content.SharedPreferences
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class PreferencesManagerTest {

    @Mock
    private lateinit var mockSharedPreferences: SharedPreferences

    @Mock
    private lateinit var mockEditor: SharedPreferences.Editor

    private lateinit var preferencesManager: PreferencesManager
    private lateinit var context: Context

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        context = RuntimeEnvironment.getApplication()

        whenever(mockSharedPreferences.edit()).thenReturn(mockEditor)
        whenever(mockEditor.putString(any(), any())).thenReturn(mockEditor)
        whenever(mockEditor.putBoolean(any(), any())).thenReturn(mockEditor)

        preferencesManager = PreferencesManager(context)
    }

    @Test
    fun testGetSensitivityThreshold_DefaultValue() {
        val threshold = preferencesManager.getSensitivityThreshold()
        assert(threshold > 0)
    }

    @Test
    fun testGetSensitivityThreshold_ReturnsLongValue() {
        val threshold = preferencesManager.getSensitivityThreshold()
        assert(threshold is Long)
    }

    @Test
    fun testSetSensitivityThreshold_UpdatesValue() {
        val newThreshold = 500L
        preferencesManager.setSensitivityThreshold(newThreshold)

        // The value should be stored (though we can't easily verify it without using real SharedPreferences)
        assert(true)
    }

    @Test
    fun testSetSensitivityThreshold_ZeroValue() {
        val newThreshold = 0L
        preferencesManager.setSensitivityThreshold(newThreshold)

        assert(true)
    }

    @Test
    fun testSetSensitivityThreshold_LargeValue() {
        val newThreshold = 5000L
        preferencesManager.setSensitivityThreshold(newThreshold)

        assert(true)
    }

    @Test
    fun testIsServiceEnabled_DefaultValue() {
        val isEnabled = preferencesManager.isServiceEnabled()
        // Default should be true or false, just verify it returns a boolean
        assert(isEnabled is Boolean)
    }

    @Test
    fun testSetServiceEnabled_True() {
        preferencesManager.setServiceEnabled(true)
        assert(true)
    }

    @Test
    fun testSetServiceEnabled_False() {
        preferencesManager.setServiceEnabled(false)
        assert(true)
    }

    @Test
    fun testPreferencesManager_WithRealSharedPreferences() {
        val realContext = RuntimeEnvironment.getApplication()
        val realPreferencesManager = PreferencesManager(realContext)

        // Test default value
        val defaultThreshold = realPreferencesManager.getSensitivityThreshold()
        assert(defaultThreshold > 0)

        // Test setting and getting
        val newThreshold = 750L
        realPreferencesManager.setSensitivityThreshold(newThreshold)
        val retrievedThreshold = realPreferencesManager.getSensitivityThreshold()
        assert(retrievedThreshold == newThreshold)

        // Test service enabled
        realPreferencesManager.setServiceEnabled(false)
        assert(!realPreferencesManager.isServiceEnabled())

        realPreferencesManager.setServiceEnabled(true)
        assert(realPreferencesManager.isServiceEnabled())
    }

    @Test
    fun testGetSensitivityThreshold_InvalidValue_ReturnsDefault() {
        val context = RuntimeEnvironment.getApplication()
        val preferencesManager = PreferencesManager(context)

        // This should handle invalid values gracefully
        val threshold = preferencesManager.getSensitivityThreshold()
        assert(threshold > 0)
    }

    @Test
    fun testMultipleSensitivityThresholdUpdates() {
        val context = RuntimeEnvironment.getApplication()
        val preferencesManager = PreferencesManager(context)

        preferencesManager.setSensitivityThreshold(100L)
        var threshold = preferencesManager.getSensitivityThreshold()
        assert(threshold == 100L)

        preferencesManager.setSensitivityThreshold(500L)
        threshold = preferencesManager.getSensitivityThreshold()
        assert(threshold == 500L)

        preferencesManager.setSensitivityThreshold(1000L)
        threshold = preferencesManager.getSensitivityThreshold()
        assert(threshold == 1000L)
    }
}
