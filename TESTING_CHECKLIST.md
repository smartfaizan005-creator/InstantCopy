# Text Selection Accessibility Service - Manual Testing Checklist

## Overview
This document provides a comprehensive manual testing checklist for the Text Selection Accessibility Service that captures highlighted text and copies it to the clipboard.

## Prerequisites
- Android device or emulator running API 24 (Android 7.0) or higher
- App installed and debuggable
- Accessibility service permission granted in Settings > Accessibility > Text Selection Service

## Test Setup
1. Install the app on the test device
2. Navigate to Settings > Accessibility
3. Enable "Text Selection Service"
4. Grant all required permissions
5. Return to home screen

---

## Section 1: Basic Functionality Tests

### Test 1.1: Text Selection - Single Word
**Steps:**
1. Open any text app (Notes, Email, Browser)
2. Slowly select a single word by long-pressing and dragging
3. Release finger
4. Open another app or clipboard history

**Expected Result:**
- The selected word is copied to clipboard
- No UI prompt appears (silent operation)

### Test 1.2: Text Selection - Multiple Words
**Steps:**
1. Open a text app
2. Select multiple words (e.g., "Hello world test")
3. Release finger

**Expected Result:**
- All selected words are copied to clipboard intact

### Test 1.3: Text Selection - Long Text
**Steps:**
1. Open a document or article
2. Select a paragraph (multiple sentences)
3. Release finger

**Expected Result:**
- Entire selected paragraph is copied
- Text formatting is preserved (spaces, line breaks)

### Test 1.4: Multiple Sequential Selections
**Steps:**
1. Open a text app
2. Select word "First"
3. Release, wait 500ms
4. Select word "Second"
5. Release, wait 500ms
6. Select word "Third"

**Expected Result:**
- Each selection overwrites the previous one in clipboard
- Final clipboard contains "Third"

---

## Section 2: Sensitivity Threshold Tests

### Test 2.1: Sensitivity - Short Press (Below Threshold)
**Prerequisites:** Sensitivity threshold set to 300ms (default)

**Steps:**
1. Quickly tap and select text in under 300ms
2. Release

**Expected Result:**
- Text may or may not be copied (short press filtered)
- Behavior depends on exact implementation of timing

### Test 2.2: Sensitivity - Long Press (Above Threshold)
**Steps:**
1. Slowly select text taking more than 300ms
2. Release

**Expected Result:**
- Text is reliably copied to clipboard

### Test 2.3: Sensitivity Threshold Configuration
**Steps:**
1. Open app settings
2. Set sensitivity threshold to 500ms
3. Perform quick text selection (< 300ms)
4. Verify not copied
5. Perform slow selection (> 500ms)
6. Verify copied

**Expected Result:**
- Service respects the configured threshold
- Selections below threshold are filtered

### Test 2.4: Sensitivity - Edge Case (Exactly at Threshold)
**Steps:**
1. Use precise timing tool
2. Select text for exactly 300ms (or configured threshold)
3. Release

**Expected Result:**
- Selection is processed (threshold is typically >= not >)

---

## Section 3: Popular App Compatibility Tests

### Test 3.1: Chrome/Mobile Browser
**Steps:**
1. Open browser
2. Navigate to an article
3. Select text from article
4. Check clipboard

**Expected Result:**
- Text copied successfully

### Test 3.2: Gmail
**Steps:**
1. Open Gmail app
2. Open an email
3. Select text from email body
4. Check clipboard

**Expected Result:**
- Email text copied to clipboard

### Test 3.3: Notes/Keep App
**Steps:**
1. Open Notes or Google Keep
2. Create/open a note with text
3. Select text
4. Verify clipboard

**Expected Result:**
- Note text copied successfully

### Test 3.4: WhatsApp/Messaging App
**Steps:**
1. Open messaging app
2. Select text from conversation
3. Check clipboard

**Expected Result:**
- Message text copied to clipboard

### Test 3.5: Twitter/X
**Steps:**
1. Open Twitter/X app
2. Select tweet text
3. Verify clipboard

**Expected Result:**
- Tweet text copied successfully

### Test 3.6: Facebook
**Steps:**
1. Open Facebook app
2. Select text from posts or comments
3. Check clipboard

**Expected Result:**
- Post text copied to clipboard

### Test 3.7: Reddit
**Steps:**
1. Open Reddit app
2. Select text from posts or comments
3. Verify clipboard

**Expected Result:**
- Reddit text copied successfully

### Test 3.8: PDF Reader (Adobe Acrobat/Other)
**Steps:**
1. Open PDF in reader app
2. Select text from PDF
3. Check clipboard

**Expected Result:**
- PDF text copied to clipboard

### Test 3.9: System Notes/Memo App
**Steps:**
1. Open system notes app
2. Select existing text
3. Verify clipboard

**Expected Result:**
- Notes text copied

### Test 3.10: Web Search Results
**Steps:**
1. Open browser
2. Perform search
3. Select text from search results
4. Check clipboard

**Expected Result:**
- Search result text copied

---

## Section 4: Edge Cases and Special Characters

### Test 4.1: Unicode Characters (Emoji)
**Steps:**
1. Open app with emoji content
2. Select text with emoji (e.g., "Hello 🎉 World")
3. Check clipboard

**Expected Result:**
- Emoji preserved in clipboard

### Test 4.2: Multiple Languages
**Steps:**
1. Open app with multi-language content
2. Select text: "Hello 你好 مرحبا"
3. Verify clipboard

**Expected Result:**
- All languages copied correctly

### Test 4.3: Numbers and Special Symbols
**Steps:**
1. Select text: "Price: $99.99 (50% off) #deal"
2. Check clipboard

**Expected Result:**
- Symbols and special characters preserved

### Test 4.4: URL Selection
**Steps:**
1. Open browser
2. Select a URL or link text
3. Check clipboard

**Expected Result:**
- URL copied in full

### Test 4.5: Email Address Selection
**Steps:**
1. Select email address from text
2. Check clipboard

**Expected Result:**
- Email address copied completely

### Test 4.6: Phone Number Selection
**Steps:**
1. Select phone number from text
2. Verify clipboard

**Expected Result:**
- Phone number copied exactly

### Test 4.7: Code/Programming Text
**Steps:**
1. Open code editor or view code
2. Select code snippet (e.g., function definition)
3. Check clipboard

**Expected Result:**
- Code copied with proper formatting

### Test 4.8: Mixed Whitespace
**Steps:**
1. Select text with multiple spaces/tabs: "word    word"
2. Verify clipboard

**Expected Result:**
- Whitespace preserved exactly

### Test 4.9: Multiline Text with Line Breaks
**Steps:**
1. Select text across multiple lines:
   ```
   Line 1
   Line 2
   Line 3
   ```
2. Check clipboard

**Expected Result:**
- All lines copied with breaks preserved

### Test 4.10: HTML Entities (in web context)
**Steps:**
1. Open webpage with HTML entities
2. Select text like "&nbsp;" or "&copy;"
3. Check clipboard

**Expected Result:**
- Text copied as displayed (or as-is depending on context)

---

## Section 5: Service Lifecycle Tests

### Test 5.1: Service Disabled/Enabled Toggle
**Steps:**
1. Go to Settings > Accessibility
2. Disable Text Selection Service
3. Try selecting text
4. Re-enable service
5. Try selecting text again

**Expected Result:**
- No copy when disabled
- Copy works when re-enabled

### Test 5.2: Service Survive Process Kill
**Steps:**
1. Force stop app via Settings > Apps
2. Select text in another app

**Expected Result:**
- Service restarts automatically (if enabled)
- Text copying resumes

### Test 5.3: Service Boot Auto-Start
**Steps:**
1. Enable service
2. Reboot device
3. Select text in any app (without manually starting service)

**Expected Result:**
- Service auto-starts after boot
- Text selection works without user intervention

### Test 5.4: Device Sleep and Wake
**Steps:**
1. Enable service
2. Lock device (sleep)
3. Unlock device
4. Select text

**Expected Result:**
- Service continues to work after wake
- Text copying works normally

### Test 5.5: Service During Low Battery Mode
**Steps:**
1. Enable Low Battery/Battery Saver mode
2. Select text in apps

**Expected Result:**
- Service continues to function
- Text selection works even in power saving mode

---

## Section 6: Clipboard Integration Tests

### Test 6.1: Clipboard Overwrite
**Steps:**
1. Copy some text manually (e.g., "Original")
2. Select different text using accessibility service
3. Check clipboard

**Expected Result:**
- New selection overwrites clipboard
- Clipboard now contains newly selected text

### Test 6.2: Clipboard Paste After Selection
**Steps:**
1. Select text with service
2. Open another app
3. Long-press to paste

**Expected Result:**
- Selected text pastes correctly

### Test 6.3: Clipboard History (if supported)
**Steps:**
1. Make multiple selections using service
2. Open clipboard history/manager
3. Check history

**Expected Result:**
- All selections appear in history (if device supports it)

### Test 6.4: Clipboard Conflict Resolution
**Steps:**
1. Manually copy text (e.g., "Manual")
2. Select text with service before clearing clipboard
3. Paste multiple times

**Expected Result:**
- Most recent action takes precedence
- Service copy overwrites manual copy

---

## Section 7: Permission and Security Tests

### Test 7.1: Accessibility Service Permission
**Steps:**
1. Install app
2. Go to Settings > Accessibility
3. Check if service appears in list

**Expected Result:**
- Service appears as available option

### Test 7.2: Permission Grant Flow
**Steps:**
1. Uninstall app
2. Reinstall app
3. Go to Settings > Accessibility
4. Enable service

**Expected Result:**
- Permission flow works smoothly
- Service enables without errors

### Test 7.3: No Sensitive Data Logging
**Steps:**
1. Enable service
2. Select banking info or passwords (in a safe test environment)
3. Check logs with: `adb logcat | grep textselection`

**Expected Result:**
- No sensitive data in logs
- Service operates silently

---

## Section 8: Performance and Battery Tests

### Test 8.1: Memory Usage
**Steps:**
1. Open Settings > Developer Options
2. Monitor Memory usage
3. Select text repeatedly for 5 minutes
4. Check memory consumption

**Expected Result:**
- Memory usage remains stable
- No memory leaks observed

### Test 8.2: CPU Usage
**Steps:**
1. Monitor CPU in developer options
2. Use service for 10 minutes
3. Check CPU impact

**Expected Result:**
- Minimal CPU usage when idle
- Short CPU spikes only during actual selection

### Test 8.3: Battery Impact - 1 Hour
**Steps:**
1. Disable all other background services
2. Repeatedly select text for 1 hour
3. Check battery drain

**Expected Result:**
- Reasonable battery consumption
- No excessive drain from service

### Test 8.4: Service Responsiveness
**Steps:**
1. Select text with service enabled
2. Measure time from release to clipboard update

**Expected Result:**
- Clipboard updated within 100ms
- User perceives instant copy

---

## Section 9: Regression Tests

### Test 9.1: Service Doesn't Interfere With Normal Selection
**Steps:**
1. Enable service
2. Use text selection for normal copy-paste workflow
3. Manually copy with context menu
4. Use system clipboard for multiple operations

**Expected Result:**
- Service doesn't interfere
- Normal text selection workflows unaffected

### Test 9.2: Third-Party Apps Unaffected
**Steps:**
1. Test apps with custom text selection (Slack, Discord, etc.)
2. Verify their custom selection mechanisms still work
3. Select text normally in these apps

**Expected Result:**
- Third-party selection mechanisms work as expected

### Test 9.3: Accessibility Features Not Broken
**Steps:**
1. Enable service
2. Test TalkBack (screen reader)
3. Test Font Size accessibility
4. Test High Contrast accessibility

**Expected Result:**
- Service doesn't break other accessibility features
- All features work together harmoniously

---

## Section 10: Stress and Boundary Tests

### Test 10.1: Very Long Text Selection
**Steps:**
1. Open large document
2. Select entire document (10,000+ characters)
3. Check clipboard

**Expected Result:**
- Large text copied successfully
- No crashes or truncation

### Test 10.2: Rapid Selections
**Steps:**
1. Rapidly select and deselect text 50+ times
2. Check stability

**Expected Result:**
- Service remains stable
- No crashes or memory issues

### Test 10.3: Selection in Overlapping Windows
**Steps:**
1. Have multiple windows/apps open
2. Select text in foreground app
3. Check clipboard

**Expected Result:**
- Correct text from foreground app is copied
- No interference from background apps

### Test 10.4: Selection During App Transitions
**Steps:**
1. Select text
2. Immediately switch to another app
3. Check clipboard

**Expected Result:**
- Correct text captured before switch
- Clipboard contains expected text

---

## Recording Results

For each test, record:
- ✓ = Pass
- ✗ = Fail
- ~ = Partial/Needs Investigation
- N/A = Not applicable to test device

Example format:
```
Test 1.1: Text Selection - Single Word  ✓ (Passed on Pixel 6, Android 13)
Test 1.2: Text Selection - Multiple Words  ✓ (Passed on Galaxy S20, Android 12)
Test 3.1: Chrome/Mobile Browser  ✓ (Passed)
Test 3.5: Twitter/X  ~ (Partial - emoji selections work, URLs have issues)
```

---

## Known Issues and Workarounds

Document any known issues discovered during testing:

Example:
```
Issue: Service doesn't capture text in React Native apps
Workaround: Use web version or copy-paste manually
Device: OnePlus 8, Android 11
```

---

## Test Summary Template

```
Testing Date: [Date]
Tester Name: [Name]
Device Model: [Model]
Android Version: [Version]
App Version: [Version]

Total Tests: [Count]
Passed: [Count]
Failed: [Count]
Partial: [Count]

Critical Issues: [Count]
Major Issues: [Count]
Minor Issues: [Count]

Approved for Release: [ ] Yes [ ] No
```

---

## Continuous Integration / Automated Testing

### Instrumentation Test Coverage
- Clipboard write operations
- Preference read/write operations
- Accessibility event handling
- Service lifecycle management
- Permission handling

### Unit Test Coverage
- Sensitivity threshold calculation
- Clipboard copy success/failure cases
- Preference manager storage
- Boot receiver behavior

All tests should achieve minimum 80% code coverage.

---

## Conclusion

This comprehensive testing checklist ensures the Text Selection Accessibility Service functions correctly across various scenarios, devices, and applications. Regular testing using this checklist helps maintain service quality and reliability.
