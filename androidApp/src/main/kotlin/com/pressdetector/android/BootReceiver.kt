package com.pressdetector.android

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pressdetector.shared.AndroidSettingsStorage
import com.pressdetector.shared.AppSettingsRepositoryImpl

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val storage = AndroidSettingsStorage(context)
            val repository = AppSettingsRepositoryImpl(storage)
            
            val settings = repository.settings.value
            
            if (settings.autoStartEnabled && settings.isEnabled) {
            }
        }
    }
}
