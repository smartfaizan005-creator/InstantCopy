package com.pressdetector.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.pressdetector.shared.AndroidSettingsStorage
import com.pressdetector.shared.AppSettingsRepositoryImpl
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var settingsRepository: AppSettingsRepositoryImpl
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val storage = AndroidSettingsStorage(applicationContext)
        settingsRepository = AppSettingsRepositoryImpl(storage)
        
        settingsRepository.onSettingsChanged { settings ->
            if (settings.isEnabled) {
                startAccessibilityService()
            } else {
                stopAccessibilityService()
            }
            
            if (settings.backgroundServiceEnabled) {
                startBackgroundService()
            } else {
                stopBackgroundService()
            }
            
            updateAutoStart(settings.autoStartEnabled)
        }
        
        setContent {
            AppTheme {
                SettingsScreen(settingsRepository)
            }
        }
    }
    
    private fun startAccessibilityService() {
    }
    
    private fun stopAccessibilityService() {
    }
    
    private fun startBackgroundService() {
    }
    
    private fun stopBackgroundService() {
    }
    
    private fun updateAutoStart(enabled: Boolean) {
    }
}

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(),
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(repository: AppSettingsRepositoryImpl) {
    val settings by repository.settings.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            SettingToggle(
                label = "Master Enable",
                description = "Enable press detection functionality",
                checked = settings.isEnabled,
                onCheckedChange = { repository.setEnabled(it) }
            )
            
            SensitivitySlider(
                value = settings.pressDurationThreshold,
                onValueChange = { repository.setPressDurationThreshold(it) }
            )
            
            SettingToggle(
                label = "Auto-Start",
                description = "Start automatically when device boots",
                checked = settings.autoStartEnabled,
                onCheckedChange = { repository.setAutoStartEnabled(it) }
            )
            
            SettingToggle(
                label = "Background Service",
                description = "Keep running in background",
                checked = settings.backgroundServiceEnabled,
                onCheckedChange = { repository.setBackgroundServiceEnabled(it) }
            )
        }
    }
}

@Composable
fun SettingToggle(
    label: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { contentDescription = "$label toggle" },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.semantics { 
                contentDescription = "$label switch" 
            }
        )
    }
}

@Composable
fun SensitivitySlider(
    value: Float,
    onValueChange: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { contentDescription = "Sensitivity slider" }
    ) {
        Text(
            text = "Press Duration Threshold",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Fast",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Slow",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0.1f..2.0f,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { 
                    contentDescription = "Press duration threshold slider, current value ${String.format("%.1f", value)} seconds" 
                }
        )
        Text(
            text = "${String.format("%.1f", value)}s",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}
