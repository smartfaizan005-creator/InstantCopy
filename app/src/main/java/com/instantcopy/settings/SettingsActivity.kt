package com.instantcopy.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    
    private lateinit var prefs: SharedPreferences
    private lateinit var settingsManager: SettingsManager
    
    private lateinit var masterSwitch: Switch
    private lateinit var sensitivitySeekBar: SeekBar
    private lateinit var sensitivityValue: TextView
    private lateinit var sensitivityCard: android.view.View
    private lateinit var autoStartSwitch: Switch
    
    private lateinit var statusText: TextView
    private lateinit var statusSensitivity: TextView
    private lateinit var statusAutoStart: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        
        prefs = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE)
        settingsManager = SettingsManager(this)
        
        initializeViews()
        setupListeners()
        loadCurrentSettings()
    }
    
    private fun initializeViews() {
        masterSwitch = findViewById(R.id.master_switch)
        sensitivitySeekBar = findViewById(R.id.sensitivity_seekbar)
        sensitivityValue = findViewById(R.id.sensitivity_value)
        sensitivityCard = findViewById(R.id.sensitivity_card)
        autoStartSwitch = findViewById(R.id.auto_start_switch)
        
        statusText = findViewById(R.id.status_text)
        statusSensitivity = findViewById(R.id.status_sensitivity)
        statusAutoStart = findViewById(R.id.status_auto_start)
    }
    
    private fun setupListeners() {
        masterSwitch.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean(MainActivity.KEY_MASTER_ENABLED, isChecked).apply()
            updateSensitivityVisibility(isChecked)
            applySettings()
        }
        
        sensitivitySeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val sensitivity = progress + 100  // Minimum 100ms
                    sensitivityValue.text = "${sensitivity}ms"
                    sensitivitySeekBar.contentDescription = "Sensitivity adjustment slider, current value $sensitivity milliseconds"
                    statusSensitivity.text = "${sensitivity}ms"
                    statusSensitivity.contentDescription = "Sensitivity: $sensitivity milliseconds"
                }
            }
            
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val sensitivity = seekBar!!.progress + 100
                prefs.edit().putInt(MainActivity.KEY_SENSITIVITY, sensitivity).apply()
                applySettings()
            }
        })
        
        autoStartSwitch.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean(MainActivity.KEY_AUTO_START, isChecked).apply()
            applySettings()
        }
    }
    
    private fun loadCurrentSettings() {
        val masterEnabled = prefs.getBoolean(MainActivity.KEY_MASTER_ENABLED, false)
        val sensitivity = prefs.getInt(MainActivity.KEY_SENSITIVITY, 1000)
        val autoStart = prefs.getBoolean(MainActivity.KEY_AUTO_START, false)
        
        masterSwitch.isChecked = masterEnabled
        updateSensitivityVisibility(masterEnabled)
        
        sensitivitySeekBar.progress = sensitivity - 100
        sensitivityValue.text = "${sensitivity}ms"
        sensitivitySeekBar.contentDescription = "Sensitivity adjustment slider, current value $sensitivity milliseconds"
        
        autoStartSwitch.isChecked = autoStart
        
        updateStatusDisplay(masterEnabled, sensitivity, autoStart)
    }
    
    private fun updateSensitivityVisibility(enabled: Boolean) {
        sensitivityCard.visibility = if (enabled) android.view.View.VISIBLE else android.view.View.GONE
    }
    
    private fun applySettings() {
        val masterEnabled = masterSwitch.isChecked
        val sensitivity = sensitivitySeekBar.progress + 100
        val autoStart = autoStartSwitch.isChecked
        
        // Update SharedPreferences and apply to accessibility service
        settingsManager.updateAccessibilitySettings(masterEnabled, sensitivity, autoStart)
        
        // Update status display
        updateStatusDisplay(masterEnabled, sensitivity, autoStart)
        
        // Update InstantCopyAccessibilityService if it's running
        InstantCopyAccessibilityService.updateSettings(masterEnabled, sensitivity)
    }
    
    private fun updateStatusDisplay(masterEnabled: Boolean, sensitivity: Int, autoStart: Boolean) {
        val status = if (masterEnabled) "Enabled" else "Disabled"
        statusText.text = status
        statusText.setTextColor(
            if (masterEnabled) 
                getColor(R.color.teal_200) 
            else 
                getColor(R.color.purple_500)
        )
        statusText.contentDescription = "Status: $status"
        
        statusSensitivity.text = "${sensitivity}ms"
        statusSensitivity.contentDescription = "Sensitivity: $sensitivity milliseconds"
        
        val autoStartStatus = if (autoStart) "On" else "Off"
        statusAutoStart.text = autoStartStatus
        statusAutoStart.contentDescription = "Auto-start: $autoStartStatus"
    }
    
    override fun onResume() {
        super.onResume()
        loadCurrentSettings() // Refresh settings when returning to the activity
    }
}