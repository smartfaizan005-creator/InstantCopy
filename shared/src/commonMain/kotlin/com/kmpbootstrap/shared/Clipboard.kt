package com.kmpbootstrap.shared

/**
 * Data class representing clipboard content
 */
data class ClipboardContent(
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val type: ClipboardType = ClipboardType.TEXT
)

/**
 * Enum representing clipboard content types
 */
enum class ClipboardType {
    TEXT,
    IMAGE,
    URL
}

/**
 * Clipboard repository interface for platform-specific implementations
 */
interface ClipboardRepository {
    suspend fun copyText(text: String): Boolean
    suspend fun pasteText(): String?
    suspend fun getClipboardContent(): ClipboardContent?
    suspend fun clearClipboard(): Boolean
    suspend fun hasContent(): Boolean
}

/**
 * Clipboard use case for business logic
 */
class ClipboardUseCase(private val repository: ClipboardRepository) {
    suspend fun copy(text: String): Boolean {
        return if (text.isNotBlank()) {
            repository.copyText(text.trim())
        } else false
    }
    
    suspend fun paste(): String? = repository.pasteText()
    
    suspend fun getCurrentContent(): ClipboardContent? = repository.getClipboardContent()
    
    suspend fun clear(): Boolean = repository.clearClipboard()
    
    suspend fun isEmpty(): Boolean = !repository.hasContent()
    
    /**
     * Copy text with metadata tracking
     */
    suspend fun copyWithMetadata(text: String): ClipboardContent? {
        return if (copy(text)) {
            ClipboardContent(text)
        } else null
    }
}