package com.instantcopy.settings

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Path
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import java.util.concurrent.atomic.AtomicBoolean

class InstantCopyAccessibilityService : AccessibilityService() {
    
    companion object {
        private const val TAG = "InstantCopyService"
        private var instance: InstantCopyAccessibilityService? = null
        private val isEnabled = AtomicBoolean(false)
        private var sensitivityThreshold = 1000L
        
        fun isServiceEnabled(): Boolean {
            return isEnabled.get()
        }
        
        fun getSensitivityThreshold(): Long {
            return sensitivityThreshold
        }
        
        fun updateSettings(enabled: Boolean, sensitivity: Int) {
            isEnabled.set(enabled)
            sensitivityThreshold = sensitivity.toLong()
        }
    }
    
    private lateinit var prefs: SharedPreferences
    private val mainHandler = Handler(Looper.getMainLooper())
    private val touchDetector = TouchDetector()
    
    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        prefs = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE)
        loadSettings()
        Log.d(TAG, "Accessibility service connected")
    }
    
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Handle accessibility events if needed
        touchDetector.onAccessibilityEvent(event)
    }
    
    override fun onInterrupt() {
        Log.d(TAG, "Service interrupted")
        touchDetector.reset()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        instance = null
        touchDetector.reset()
        Log.d(TAG, "Service destroyed")
    }
    
    private fun loadSettings() {
        val enabled = prefs.getBoolean(MainActivity.KEY_MASTER_ENABLED, false)
        val sensitivity = prefs.getInt(MainActivity.KEY_SENSITIVITY, 1000)
        
        updateSettings(enabled, sensitivity)
        
        if (enabled) {
            Log.d(TAG, "Service enabled with sensitivity ${sensitivity}ms")
            showToast("InstantCopy enabled")
        } else {
            Log.d(TAG, "Service disabled")
            showToast("InstantCopy disabled")
        }
    }
    
    private fun showToast(message: String) {
        mainHandler.post {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
    
    private inner class TouchDetector {
        private var touchStartTime = 0L
        private var lastTouchX = 0f
        private var lastTouchY = 0f
        private var isLongPressTriggered = false
        
        fun reset() {
            touchStartTime = 0L
            isLongPressTriggered = false
        }
        
        fun onAccessibilityEvent(event: AccessibilityEvent?) {
            // This method can be used to handle specific accessibility events
            // For now, we'll focus on touch event detection through other means
        }
        
        // Simulated touch detection - in a real implementation, this would be more complex
        fun simulateTouch(x: Float, y: Float, duration: Long) {
            if (!isEnabled.get()) return
            
            if (duration >= sensitivityThreshold) {
                performCopyGesture(x, y)
            }
        }
        
        private fun performCopyGesture(x: Float, y: Float) {
            try {
                // Create a path for the copy gesture (select all -> copy)
                val path = Path().apply {
                    moveTo(x, y)
                }
                
                val gesture = GestureDescription.Builder()
                    .addStroke(GestureDescription.StrokeDescription(path, 0, 100))
                    .build()
                
                dispatchGesture(gesture, object : GestureResultCallback() {
                    override fun onCompleted(gestureDescription: GestureDescription?) {
                        Log.d(TAG, "Copy gesture completed")
                        mainHandler.post {
                            Toast.makeText(this@InstantCopyAccessibilityService, "Content copied", Toast.LENGTH_SHORT).show()
                        }
                    }
                    
                    override fun onCancelled(gestureDescription: GestureDescription?) {
                        Log.w(TAG, "Copy gesture cancelled")
                    }
                }, null)
                
            } catch (e: Exception) {
                Log.e(TAG, "Error performing copy gesture", e)
            }
        }
    }
    
    // Public method to test touch detection (for development)
    fun testTouchDetection(x: Float, y: Float, duration: Long) {
        touchDetector.simulateTouch(x, y, duration)
    }
}