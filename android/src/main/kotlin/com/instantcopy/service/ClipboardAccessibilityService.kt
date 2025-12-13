package com.instantcopy.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class ClipboardAccessibilityService : AccessibilityService() {
    private lateinit var clipboardManager: ClipboardManager
    private var isEnabled = true
    private var lastCopiedText = ""
    private var sensitivity = 500L

    override fun onServiceConnected() {
        super.onServiceConnected()
        clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        val info = AccessibilityServiceInfo()
        info.eventTypes = AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED or
                AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
        info.notificationTimeout = sensitivity
        serviceInfo = info

        loadSettings()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (!isEnabled || event == null) return

        when (event.eventType) {
            AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED,
            AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED -> {
                extractAndCopyText(event)
            }
        }
    }

    override fun onInterrupt() {
        // Called when system wants to interrupt the service
    }

    private fun extractAndCopyText(event: AccessibilityEvent) {
        try {
            val source = event.source ?: return
            val text = extractTextFromNode(source)
            
            if (text.isNotEmpty() && text != lastCopiedText) {
                lastCopiedText = text
                val clip = ClipData.newPlainText("InstantCopy", text)
                clipboardManager.setPrimaryClip(clip)
            }
        } catch (e: Exception) {
            // Silently handle exceptions to avoid service crashes
        }
    }

    private fun extractTextFromNode(node: AccessibilityNodeInfo): String {
        val text = node.text?.toString() ?: ""
        return text.trim()
    }

    private fun loadSettings() {
        val sharedPreferences = getSharedPreferences("instantcopy_prefs", Context.MODE_PRIVATE)
        isEnabled = sharedPreferences.getBoolean("enabled", true)
        sensitivity = sharedPreferences.getInt("sensitivity", 500).toLong()

        val info = serviceInfo
        info?.notificationTimeout = sensitivity
        serviceInfo = info
    }
}
