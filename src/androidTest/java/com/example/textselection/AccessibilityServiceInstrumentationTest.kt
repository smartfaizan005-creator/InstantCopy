package com.example.textselection

import android.content.ClipboardManager
import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.textselection.clipboard.ClipboardManager as CustomClipboardManager
import com.example.textselection.settings.PreferencesManager
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AccessibilityServiceInstrumentationTest {

    private lateinit var context: Context
    private lateinit var clipboardManager: CustomClipboardManager
    private lateinit var preferencesManager: PreferencesManager
    private lateinit var systemClipboard: ClipboardManager

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        clipboardManager = CustomClipboardManager(context)
        preferencesManager = PreferencesManager(context)
        systemClipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    @Test
    fun testClipboardCopyAndRetrieve() {
        val testText = "Test clipboard content"
        val success = clipboardManager.copyToClipboard(testText)

        assert(success)
        val clipboardText = systemClipboard.primaryClip?.getItemAt(0)?.text?.toString()
        assert(clipboardText == testText)
    }

    @Test
    fun testPreferencesManager_SensitivityThreshold() {
        val testThreshold = 500L
        preferencesManager.setSensitivityThreshold(testThreshold)

        val retrievedThreshold = preferencesManager.getSensitivityThreshold()
        assert(retrievedThreshold == testThreshold)
    }

    @Test
    fun testPreferencesManager_ServiceEnabled() {
        preferencesManager.setServiceEnabled(true)
        assert(preferencesManager.isServiceEnabled())

        preferencesManager.setServiceEnabled(false)
        assert(!preferencesManager.isServiceEnabled())

        preferencesManager.setServiceEnabled(true)
        assert(preferencesManager.isServiceEnabled())
    }

    @Test
    fun testClipboardWithMultipleSelections() {
        val selections = listOf(
            "First selection",
            "Second selection",
            "Third selection"
        )

        for (selection in selections) {
            val success = clipboardManager.copyToClipboard(selection)
            assert(success)

            val clipboardText = systemClipboard.primaryClip?.getItemAt(0)?.text?.toString()
            assert(clipboardText == selection)
        }
    }

    @Test
    fun testClipboardWithUnicodeText() {
        val unicodeText = "Unicode test: 你好 مرحبا 🎉"
        val success = clipboardManager.copyToClipboard(unicodeText)

        assert(success)
        val clipboardText = systemClipboard.primaryClip?.getItemAt(0)?.text?.toString()
        assert(clipboardText == unicodeText)
    }

    @Test
    fun testClipboardWithLongText() {
        val longText = "A".repeat(5000)
        val success = clipboardManager.copyToClipboard(longText)

        assert(success)
        val clipboardText = systemClipboard.primaryClip?.getItemAt(0)?.text?.toString()
        assert(clipboardText?.length == 5000)
    }

    @Test
    fun testClipboardWithSpecialCharacters() {
        val specialText = "Special chars: !@#$%^&*()_+-=[]{}|;:',.<>?/\\"
        val success = clipboardManager.copyToClipboard(specialText)

        assert(success)
        val clipboardText = systemClipboard.primaryClip?.getItemAt(0)?.text?.toString()
        assert(clipboardText == specialText)
    }

    @Test
    fun testClipboardWithNewlines() {
        val multilineText = "Line 1\nLine 2\nLine 3"
        val success = clipboardManager.copyToClipboard(multilineText)

        assert(success)
        val clipboardText = systemClipboard.primaryClip?.getItemAt(0)?.text?.toString()
        assert(clipboardText == multilineText)
    }

    @Test
    fun testPreferencesManager_MultipleUpdates() {
        // Test multiple updates to the same preference
        for (i in 1..10) {
            val threshold = (i * 100).toLong()
            preferencesManager.setSensitivityThreshold(threshold)
            assert(preferencesManager.getSensitivityThreshold() == threshold)
        }
    }

    @Test
    fun testClipboardManager_EmptyText() {
        val emptyText = ""
        val success = clipboardManager.copyToClipboard(emptyText)

        assert(success)
        val clipboardText = systemClipboard.primaryClip?.getItemAt(0)?.text?.toString()
        assert(clipboardText == emptyText)
    }

    @Test
    fun testClipboardManager_Whitespace() {
        val whitespaceText = "   \t  \n  \r  "
        val success = clipboardManager.copyToClipboard(whitespaceText)

        assert(success)
        val clipboardText = systemClipboard.primaryClip?.getItemAt(0)?.text?.toString()
        assert(clipboardText == whitespaceText)
    }
}
