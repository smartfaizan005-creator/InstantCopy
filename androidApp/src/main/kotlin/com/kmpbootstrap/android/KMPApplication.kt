package com.kmpbootstrap.android

import android.app.Application
import com.kmpbootstrap.shared.AndroidSettingsRepository
import com.kmpbootstrap.shared.AndroidClipboardRepository
import com.kmpbootstrap.shared.AndroidPlatformServiceRepository

/**
 * Android Application class
 */
class KMPApplication : Application() {
    
    lateinit var settingsRepository: AndroidSettingsRepository
        private set
    
    lateinit var clipboardRepository: AndroidClipboardRepository
        private set
    
    lateinit var platformServiceRepository: AndroidPlatformServiceRepository
        private set
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize repositories
        settingsRepository = AndroidSettingsRepository(this)
        clipboardRepository = AndroidClipboardRepository(this)
        platformServiceRepository = AndroidPlatformServiceRepository(this)
    }
}