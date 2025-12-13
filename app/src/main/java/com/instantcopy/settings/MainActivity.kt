package com.instantcopy.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.instantcopy.settings.ui.theme.InstantCopySettingsTheme

class MainActivity : ComponentActivity() {
    
    companion object {
        const val PREFS_NAME = "InstantCopySettings"
        const val KEY_MASTER_ENABLED = "master_enabled"
        const val KEY_SENSITIVITY = "sensitivity"
        const val KEY_AUTO_START = "auto_start"
        
        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InstantCopySettingsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SettingsScreen()
                }
            }
        }
    }
}

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE) }
    
    var masterEnabled by remember { mutableStateOf(prefs.getBoolean(MainActivity.KEY_MASTER_ENABLED, false)) }
    var sensitivity by remember { mutableStateOf(prefs.getInt(MainActivity.KEY_SENSITIVITY, 1000)) }
    var autoStart by remember { mutableStateOf(prefs.getBoolean(MainActivity.KEY_AUTO_START, false)) }
    
    val settingsManager = remember { SettingsManager(context) }
    
    // Real-time updates to accessibility service
    LaunchedEffect(masterEnabled, sensitivity, autoStart) {
        settingsManager.updateAccessibilitySettings(masterEnabled, sensitivity, autoStart)
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .semantics { },
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "InstantCopy Settings",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.semantics { 
                contentDescription = "InstantCopy Settings Screen" 
            }
        )
        
        // Master Enable Toggle
        SettingsToggleCard(
            title = "Master Enable",
            description = "Turn InstantCopy functionality on or off",
            checked = masterEnabled,
            onCheckedChange = { enabled ->
                masterEnabled = enabled
                prefs.edit().putBoolean(MainActivity.KEY_MASTER_ENABLED, enabled).apply()
            },
            contentDescription = "Master Enable Toggle"
        )
        
        // Sensitivity Slider
        if (masterEnabled) {
            SettingsSliderCard(
                title = "Sensitivity",
                description = "Press duration threshold in milliseconds",
                value = sensitivity.toFloat(),
                onValueChange = { value ->
                    sensitivity = value.toInt()
                    prefs.edit().putInt(MainActivity.KEY_SENSITIVITY, sensitivity).apply()
                },
                valueRange = 100f..3000f,
                steps = 28,
                contentDescription = "Sensitivity Slider"
            )
        }
        
        // Auto-start on Boot Toggle
        SettingsToggleCard(
            title = "Auto-start on Boot",
            description = "Automatically enable InstantCopy when device boots",
            checked = autoStart,
            onCheckedChange = { enabled ->
                autoStart = enabled
                prefs.edit().putBoolean(MainActivity.KEY_AUTO_START, enabled).apply()
            },
            contentDescription = "Auto-start on Boot Toggle"
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Status Display
        SettingsStatusCard(
            masterEnabled = masterEnabled,
            sensitivity = sensitivity,
            autoStart = autoStart
        )
    }
}

@Composable
fun SettingsToggleCard(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    contentDescription: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { contentDescription = contentDescription }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = checked,
                    onCheckedChange = onCheckedChange,
                    modifier = Modifier.semantics {
                        contentDescription = "$title Switch"
                    }
                )
            }
        }
    }
}

@Composable
fun SettingsSliderCard(
    title: String,
    description: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    contentDescription: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { contentDescription = contentDescription }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = "${value.toInt()}ms",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.semantics {
                        contentDescription = "Current sensitivity value ${value.toInt()} milliseconds"
                    }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = valueRange,
                steps = steps,
                modifier = Modifier.semantics {
                    contentDescription = "Sensitivity adjustment slider"
                }
            )
        }
    }
}

@Composable
fun SettingsStatusCard(
    masterEnabled: Boolean,
    sensitivity: Int,
    autoStart: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { contentDescription = "Current Settings Status" }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Current Settings",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            SettingsStatusRow("Status", if (masterEnabled) "Enabled" else "Disabled")
            SettingsStatusRow("Sensitivity", "${sensitivity}ms")
            SettingsStatusRow("Auto-start", if (autoStart) "On" else "Off")
        }
    }
}

@Composable
fun SettingsStatusRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}