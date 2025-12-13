package com.instantcopy.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.instantcopy.MainActivity

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            val sharedPreferences = context?.getSharedPreferences("instantcopy_prefs", Context.MODE_PRIVATE)
            val autoStart = sharedPreferences?.getBoolean("auto_start", true) ?: true

            if (autoStart && context != null) {
                val mainActivityIntent = Intent(context, MainActivity::class.java)
                mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(mainActivityIntent)
            }
        }
    }
}
