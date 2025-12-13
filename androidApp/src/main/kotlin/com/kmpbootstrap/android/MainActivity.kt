package com.kmpbootstrap.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.kmpbootstrap.shared.Theme
import com.kmpbootstrap.shared.SettingsState
import com.kmpbootstrap.shared.SettingsUseCase
import com.kmpbootstrap.shared.ClipboardUseCase
import com.kmpbootstrap.shared.PlatformServiceUseCase
import com.kmpbootstrap.shared.AndroidSettingsRepository
import com.kmpbootstrap.shared.AndroidClipboardRepository
import com.kmpbootstrap.shared.AndroidPlatformServiceRepository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    
    private lateinit var settingsUseCase: SettingsUseCase
    private lateinit var clipboardUseCase: ClipboardUseCase
    private lateinit var platformServiceUseCase: PlatformServiceUseCase
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setupUseCases()
        
        setContent {
            MainScreen()
        }
    }
    
    private fun setupUseCases() {
        val settingsRepository = AndroidSettingsRepository(this)
        val clipboardRepository = AndroidClipboardRepository(this)
        val platformServiceRepository = AndroidPlatformServiceRepository(this)
        
        settingsUseCase = SettingsUseCase(settingsRepository)
        clipboardUseCase = ClipboardUseCase(clipboardRepository)
        platformServiceUseCase = PlatformServiceUseCase(platformServiceRepository)
    }
}

@Composable
fun MainScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Text(
            text = "KMP Bootstrap App",
            fontSize = 24.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainScreen()
}