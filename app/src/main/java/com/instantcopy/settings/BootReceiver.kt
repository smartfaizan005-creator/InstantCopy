package com.instantcopy.settings

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat.startForegroundService

class BootReceiver : BroadcastReceiver() {
    
    companion object {
        private const val TAG = "BootReceiver"
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        Log.d(TAG, "Received broadcast: $action")
        
        if (Intent.ACTION_BOOT_COMPLETED == action ||
            Intent.ACTION_MY_PACKAGE_REPLACED == action ||
            Intent.ACTION_PACKAGE_REPLACED == action) {
            
            checkAndStartService(context)
        }
    }
    
    private fun checkAndStartService(context: Context) {
        val prefs = context.getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE)
        val autoStartEnabled = prefs.getBoolean(MainActivity.KEY_AUTO_START, false)
        val masterEnabled = prefs.getBoolean(MainActivity.KEY_MASTER_ENABLED, false)
        
        if (autoStartEnabled && masterEnabled) {
            Log.d(TAG, "Auto-start enabled, starting accessibility service")
            startAccessibilityService(context)
        } else {
            Log.d(TAG, "Auto-start disabled or master disabled, not starting service")
        }
    }
    
    private fun startAccessibilityService(context: Context) {
        try {
            val serviceIntent = Intent(context, InstantCopyAccessibilityService::class.java)
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(context, serviceIntent)
            } else {
                context.startService(serviceIntent)
            }
            
            Log.d(TAG, "Accessibility service start requested")
        } catch (e: Exception) {
            Log.e(TAG, "Error starting accessibility service", e)
        }
    }
}