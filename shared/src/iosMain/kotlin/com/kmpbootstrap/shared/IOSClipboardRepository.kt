package com.kmpbootstrap.shared

import platform.UIKit.UIPasteboard
import platform.UIKit.UIApplication
import kotlinx.coroutines.*
import platform.Foundation.NSString

/**
 * iOS implementation of ClipboardRepository using UIPasteboard
 */
class IOSClipboardRepository : ClipboardRepository {
    
    private val pasteboard = UIPasteboard.generalPasteboard
    
    override suspend fun copyText(text: String): Boolean = withContext(Dispatchers.Main) {
        return@withContext try {
            pasteboard.string = text
            true
        } catch (e: Exception) {
            false
        }
    }
    
    override suspend fun pasteText(): String? = withContext(Dispatchers.Main) {
        return@withContext try {
            pasteboard.string
        } catch (e: Exception) {
            null
        }
    }
    
    override suspend fun getClipboardContent(): ClipboardContent? = withContext(Dispatchers.Main) {
        return@withContext try {
            val text = pasteboard.string
            if (!text.isNullOrBlank()) {
                ClipboardContent(text, System.currentTimeMillis(), ClipboardType.TEXT)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    override suspend fun clearClipboard(): Boolean = withContext(Dispatchers.Main) {
        return@withContext try {
            pasteboard.string = ""
            true
        } catch (e: Exception) {
            false
        }
    }
    
    override suspend fun hasContent(): Boolean = withContext(Dispatchers.Main) {
        return@withContext try {
            val text = pasteboard.string
            !text.isNullOrBlank()
        } catch (e: Exception) {
            false
        }
    }
}