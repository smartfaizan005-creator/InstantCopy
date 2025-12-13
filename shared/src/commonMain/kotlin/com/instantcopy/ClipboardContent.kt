package com.instantcopy

data class ClipboardContent(
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)
