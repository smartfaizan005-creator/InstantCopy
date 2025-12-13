package com.instantcopy

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.instantcopy.service.ClipboardAccessibilityService

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PlatformService.initialize(this)
        setContent {
            InstantCopyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SettingsScreen(context = this@MainActivity)
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(context: Context) {
    val sharedPreferences = context.getSharedPreferences("instantcopy_prefs", Context.MODE_PRIVATE)
    var isEnabled by remember { mutableStateOf(sharedPreferences.getBoolean("enabled", true)) }
    var autoStart by remember { mutableStateOf(sharedPreferences.getBoolean("auto_start", true)) }
    var sensitivity by remember { mutableStateOf(sharedPreferences.getInt("sensitivity", 500).toFloat()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "InstantCopy Settings",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Enable InstantCopy", style = MaterialTheme.typography.bodyMedium)
                    Switch(
                        checked = isEnabled,
                        onCheckedChange = { newValue ->
                            isEnabled = newValue
                            sharedPreferences.edit().putBoolean("enabled", newValue).apply()
                        }
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Auto-Start", style = MaterialTheme.typography.bodyMedium)
                    Switch(
                        checked = autoStart,
                        onCheckedChange = { newValue ->
                            autoStart = newValue
                            sharedPreferences.edit().putBoolean("auto_start", newValue).apply()
                        }
                    )
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Sensitivity: ${sensitivity.toInt()}ms", style = MaterialTheme.typography.bodyMedium)
                Slider(
                    value = sensitivity,
                    onValueChange = { newValue ->
                        sensitivity = newValue
                        sharedPreferences.edit().putInt("sensitivity", newValue.toInt()).apply()
                    },
                    valueRange = 100f..3000f,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Button(
            onClick = {
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Open Accessibility Settings")
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            "Privacy: InstantCopy operates completely offline with no network access or data collection.",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun InstantCopyTheme(content: @Composable () -> Unit) {
    MaterialTheme(content = content)
}
