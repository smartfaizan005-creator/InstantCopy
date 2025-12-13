package com.kmpbootstrap.shared

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Android implementation of ClipboardRepository
 */
class AndroidClipboardRepository(private val context: Context) : ClipboardRepository {
    
    private val clipboardManager: ClipboardManager by lazy {
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }
    
    override suspend fun copyText(text: String): Boolean = withContext(Dispatchers.Main) {
        return@withContext try {
            val clipData = ClipData.newPlainText("KMPBootstrap", text)
            clipboardManager.setPrimaryClip(clipData)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    override suspend fun pasteText(): String? = withContext(Dispatchers.Main) {
        return@withContext try {
            val clipData = clipboardManager.primaryClip
            if (clipData != null && clipData.itemCount > 0) {
                clipData.getItemAt(0).text?.toString()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    override suspend fun getClipboardContent(): ClipboardContent? = withContext(Dispatchers.Main) {
        return@withContext try {
            val clipData = clipboardManager.primaryClip
            if (clipData != null && clipData.itemCount > 0) {
                val text = clipData.getItemAt(0).text?.toString()
                val timestamp = clipboardManager.primaryClipDescription?.timestamp ?: System.currentTimeMillis()
                
                if (!text.isNullOrBlank()) {
                    ClipboardContent(text, timestamp, ClipboardType.TEXT)
                } else {
                    null
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    override suspend fun clearClipboard(): Boolean = withContext(Dispatchers.Main) {
        return@withContext try {
            val clipData = ClipData.newPlainText("", "")
            clipboardManager.setPrimaryClip(clipData)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    override suspend fun hasContent(): Boolean = withContext(Dispatchers.Main) {
        return@withContext try {
            val clipData = clipboardManager.primaryClip
            clipData != null && clipData.itemCount > 0 && 
            !clipData.getItemAt(0).text.isNullOrBlank()
        } catch (e: Exception) {
            false
        }
    }
}