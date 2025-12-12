package com.example.textselection.service

import android.accessibilityservice.AccessibilityService
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.accessibility.AccessibilityEvent
import com.example.textselection.clipboard.ClipboardManager
import com.example.textselection.settings.PreferencesManager

class TextSelectionAccessibilityService : AccessibilityService() {

    private lateinit var preferencesManager: PreferencesManager
    private lateinit var clipboardManager: ClipboardManager
    private var lastSelectionTime: Long = 0
    private var lastSelectedText: String = ""
    private var isSelectionActive: Boolean = false
    private var copyTask: Runnable? = null
    private val handler = Handler(Looper.getMainLooper())

    override fun onServiceConnected() {
        super.onServiceConnected()
        preferencesManager = PreferencesManager(this)
        clipboardManager = ClipboardManager(this)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null || !preferencesManager.isServiceEnabled()) {
            return
        }

        when (event.eventType) {
            AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED -> {
                handleTextSelectionChanged(event)
            }
            AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED -> {
                handleTextChanged(event)
            }
        }
    }

    override fun onInterrupt() {
        // Service was interrupted, clean up if necessary
        handler.removeCallbacks(copyTask ?: return)
    }

    private fun handleTextSelectionChanged(event: AccessibilityEvent) {
        try {
            // Cancel any pending copy operation
            copyTask?.let { handler.removeCallbacks(it) }

            val source = event.source ?: return

            // Get the selected text from the event properties
            val fromIndex = event.fromIndex
            val toIndex = event.toIndex

            if (fromIndex >= 0 && toIndex > fromIndex) {
                val nodeText = source.text?.toString() ?: return
                val selectedText = try {
                    nodeText.substring(fromIndex, minOf(toIndex, nodeText.length))
                } catch (e: Exception) {
                    source.recycle()
                    return
                }

                if (selectedText.isNotEmpty()) {
                    lastSelectedText = selectedText
                    lastSelectionTime = SystemClock.uptimeMillis()
                    isSelectionActive = true
                }
            }

            source.recycle()
        } catch (e: Exception) {
            // Silently handle accessibility exceptions
        }
    }

    private fun handleTextChanged(event: AccessibilityEvent) {
        try {
            if (!isSelectionActive || lastSelectedText.isEmpty()) {
                return
            }

            // Schedule a copy operation after sensitivity threshold
            // This ensures we only copy after finger lift with proper timing
            val sensitivityThreshold = preferencesManager.getSensitivityThreshold()
            val timeSinceSelection = SystemClock.uptimeMillis() - lastSelectionTime

            val delayMs = if (timeSinceSelection < sensitivityThreshold) {
                sensitivityThreshold - timeSinceSelection
            } else {
                0L
            }

            val textToCopy = lastSelectedText
            isSelectionActive = false

            copyTask = Runnable {
                // Perform the copy operation
                clipboardManager.copyToClipboard(textToCopy)
                copyTask = null
            }

            handler.postDelayed(copyTask!!, delayMs)
        } catch (e: Exception) {
            // Silently handle exceptions
            isSelectionActive = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(copyTask ?: return)
    }
}
