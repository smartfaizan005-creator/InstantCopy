package com.example.textselection.clipboard

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ClipboardManagerTest {

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockSystemClipboardManager: ClipboardManager

    private lateinit var clipboardManager: ClipboardManager

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        whenever(mockContext.getSystemService(Context.CLIPBOARD_SERVICE))
            .thenReturn(mockSystemClipboardManager)
        clipboardManager = ClipboardManager(mockContext)
    }

    @Test
    fun testCopyToClipboard_SuccessfullyCopies() {
        val testText = "Test selected text"
        val result = clipboardManager.copyToClipboard(testText)

        assert(result)
        verify(mockSystemClipboardManager).setPrimaryClip(any<ClipData>())
    }

    @Test
    fun testCopyToClipboard_WithCustomLabel() {
        val testText = "Test selected text"
        val customLabel = "Custom Label"
        val result = clipboardManager.copyToClipboard(testText, customLabel)

        assert(result)
        verify(mockSystemClipboardManager).setPrimaryClip(any<ClipData>())
    }

    @Test
    fun testCopyToClipboard_WithEmptyText() {
        val testText = ""
        val result = clipboardManager.copyToClipboard(testText)

        // Should still return true as the operation was successful
        assert(result)
        verify(mockSystemClipboardManager).setPrimaryClip(any<ClipData>())
    }

    @Test
    fun testCopyToClipboard_WithLongText() {
        val testText = "A".repeat(1000) + " Long text content"
        val result = clipboardManager.copyToClipboard(testText)

        assert(result)
        verify(mockSystemClipboardManager).setPrimaryClip(any<ClipData>())
    }

    @Test
    fun testCopyToClipboard_WithSpecialCharacters() {
        val testText = "Text with symbols: !@#$%^&*(){}[]|\\:;\"'<>,.?/"
        val result = clipboardManager.copyToClipboard(testText)

        assert(result)
        verify(mockSystemClipboardManager).setPrimaryClip(any<ClipData>())
    }

    @Test
    fun testCopyToClipboard_WithMultilineText() {
        val testText = "Line 1\nLine 2\nLine 3"
        val result = clipboardManager.copyToClipboard(testText)

        assert(result)
        verify(mockSystemClipboardManager).setPrimaryClip(any<ClipData>())
    }

    @Test
    fun testCopyToClipboard_WithUnicodeCharacters() {
        val testText = "Unicode: 中文 العربية 日本語 한글 🎉🎊"
        val result = clipboardManager.copyToClipboard(testText)

        assert(result)
        verify(mockSystemClipboardManager).setPrimaryClip(any<ClipData>())
    }
}
