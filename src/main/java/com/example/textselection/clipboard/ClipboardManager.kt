package com.example.textselection.clipboard

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

class ClipboardManager(context: Context) {
    private val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager

    fun copyToClipboard(text: String, label: String = "Selected Text"): Boolean {
        return try {
            val clipData = ClipData.newPlainText(label, text)
            clipboardManager?.setPrimaryClip(clipData)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getClipboardText(): String? {
        return try {
            val clipData = clipboardManager?.primaryClip
            if (clipData != null && clipData.itemCount > 0) {
                clipData.getItemAt(0).text?.toString()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
