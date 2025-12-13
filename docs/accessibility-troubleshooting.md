# Accessibility Troubleshooting Guide

This guide helps resolve common accessibility permission issues on Android and iOS platforms for InstantCopy.

## Overview

InstantCopy requires accessibility permissions to monitor clipboard changes across both Android and iOS. These permissions can sometimes be challenging to enable or may stop working after app updates, device changes, or system resets.

## Android Platform

### Prerequisites

- Android 6.0+ (API level 23)
- InstantCopy app installed
- Device administrator or root not required

### Step-by-Step Permission Setup

#### 1. Enable Accessibility Service

1. **Open Settings**
   - Go to Android Settings app
   - Navigate to "Accessibility" (may be under "System" or "Additional settings")

2. **Locate InstantCopy**
   - Scroll through the list of accessibility services
   - Look for "InstantCopy" or "InstantCopy Clipboard Service"

3. **Enable the Service**
   - Tap on "InstantCopy"
   - Toggle "Use service" to ON
   - Grant any additional permissions if prompted

4. **Verification**
   - Service should show "On" status
   - You may see a green indicator next to the service name

#### 2. Grant Additional Permissions

#### Background Activity
```
Settings → Apps → InstantCopy → Battery → Battery optimization → Not optimized
```
This ensures the app can run in the background without being killed by Android's power management.

#### Notification Access
```
Settings → Apps → InstantCopy → Notifications → Allow notifications
```
Enables visual feedback when text is copied.

### Common Android Issues and Solutions

#### Issue: "Accessibility service not found"

**Symptoms:**
- InstantCopy doesn't appear in Accessibility settings
- App shows "permission denied" message

**Solutions:**
1. **Restart the app**
   ```bash
   # Force stop and restart
   adb shell am force-stop com.instantcopy.app
   # Restart app from launcher
   ```

2. **Clear app data and reinstall**
   ```
   Settings → Apps → InstantCopy → Storage → Clear Data
   ```

3. **Check app signature**
   - Ensure you're installing from the same source
   - Accessibility services are tied to app signature

#### Issue: "Service keeps turning off"

**Symptoms:**
- Accessibility service enabled but app doesn't work
- Service status shows as "On" but functionality is broken

**Solutions:**
1. **Disable battery optimization**
   ```
   Settings → Battery → Battery optimization → InstantCopy → Don't optimize
   ```

2. **Check for conflicting apps**
   - Other clipboard managers may conflict
   - System cleaners may disable accessibility services

3. **Reboot device**
   ```bash
   adb reboot
   ```

#### Issue: "Clipboard access denied"

**Symptoms:**
- App shows clipboard permission error
- Text copying doesn't trigger app response

**Solutions:**
1. **Re-enable accessibility service**
   - Turn off, then back on
   - Restart device if needed

2. **Check accessibility focus**
   - Ensure no other accessibility service is interfering
   - Disable other clipboard/accessibility apps temporarily

### Advanced Android Troubleshooting

#### Debug via ADB
```bash
# Check accessibility service status
adb shell settings get secure enabled_accessibility_services

# Enable specific service (development only)
adb shell settings put secure enabled_accessibility_services \
com_instantcopy_app/com_instantcopy.service.ClipboardAccessibilityService

# Check app permissions
adb shell dumpsys package com.instantcopy.app | grep permission
```

#### Log Analysis
```bash
# Filter InstantCopy logs
adb logcat | grep -i instantcopy

# Check accessibility service logs
adb logcat | grep -i accessibility
```

## iOS Platform

### Prerequisites

- iOS 14.0+ (clipboard access requires iOS 14+)
- InstantCopy app installed
- Developer provisioning profile (for sideloaded apps)

### Step-by-Step Permission Setup

#### 1. Enable Accessibility

1. **Open Settings App**
   - Launch iOS Settings application
   - Scroll to and tap "Accessibility"

2. **Find InstantCopy**
   - Scroll through the accessibility services list
   - Look for "InstantCopy" under "Accessibility"

3. **Enable Accessibility**
   - Tap on "InstantCopy"
   - Toggle the switch to ON (green)

4. **Grant Permissions**
   - iOS will prompt for permission confirmation
   - Tap "Allow" to grant accessibility access

#### 2. Configure Background Activity

#### Background App Refresh
```
Settings → General → Background App Refresh → InstantCopy → ON
```
This allows the app to check for clipboard changes when not active.

#### 3. Notification Setup

```
Settings → Notifications → InstantCopy → Allow Notifications
```
Enables copy notifications and status updates.

### Common iOS Issues and Solutions

#### Issue: "InstantCopy not in Accessibility settings"

**Symptoms:**
- App installed but doesn't appear in Accessibility list
- Permission request fails

**Solutions:**
1. **Force close and reopen app**
   - Double-tap Home button (or swipe up from bottom on newer devices)
   - Swipe up on InstantCopy app
   - Reopen app and trigger permission request

2. **Reset Location & Privacy**
   ```
   Settings → General → Transfer or Reset iPhone → Reset → Reset Location & Privacy
   ```
   **Warning:** This will reset ALL app permissions. Use as last resort.

3. **Reinstall the app**
   - Delete InstantCopy completely
   - Restart device
   - Reinstall app with fresh provisioning profile

#### Issue: "Accessibility permission granted but not working"

**Symptoms:**
- App shows permission granted
- Clipboard monitoring not functional
- No response to clipboard changes

**Solutions:**
1. **Toggle accessibility off and on**
   ```
   Settings → Accessibility → InstantCopy → OFF
   # Wait 5 seconds
   Settings → Accessibility → InstantCopy → ON
   ```

2. **Enable Background App Refresh**
   - Ensure background refresh is enabled for InstantCopy
   - Check iOS Battery usage to ensure app isn't being throttled

3. **Check for iOS version compatibility**
   - Ensure iOS 14.0 or later
   - iOS 15.0+ recommended for best compatibility

#### Issue: "App crashes after enabling accessibility"

**Symptoms:**
- App crashes immediately after permission grant
- Accessibility service restarts repeatedly

**Solutions:**
1. **Check device storage**
   - Ensure at least 100MB free space
   - Delete unnecessary apps or files

2. **Reset accessibility service**
   ```
   Settings → Accessibility → InstantCopy → OFF
   # Close Settings
   # Open InstantCopy and re-enable
   ```

3. **Update iOS version**
   - Install latest iOS updates
   - Accessibility features may require current iOS version

### Advanced iOS Troubleshooting

#### Console Logging (Requires Mac with Xcode)
```bash
# View device logs
# Open Xcode → Window → Devices and Simulators
# Select device → View Console Logs
# Filter for "InstantCopy"
```

#### Configuration Profile Check
```bash
# Verify provisioning profile (development builds)
# iOS Settings → General → VPN & Device Management
# Check InstantCopy profile status
```

#### Reset All Settings (Nuclear Option)
```
Settings → General → Transfer or Reset iPhone → Reset → Reset All Settings
```
**Warning:** This will reset ALL device settings including WiFi passwords, wallpapers, etc.

## Cross-Platform Solutions

### When Nothing Else Works

#### 1. Complete Reinstall Process

1. **Backup app data** (if applicable)
2. **Uninstall InstantCopy**
3. **Restart device** (hold power + volume down on Android, power button on iOS)
4. **Clear any app caches** (Android: Settings → Apps → InstantCopy → Clear Cache)
5. **Reinstall fresh version**
6. **Grant permissions immediately**
7. **Test functionality**

#### 2. System Update Verification

- **Android**: Ensure Google Play Services is updated
- **iOS**: Update to latest iOS version available for your device

#### 3. Hardware Compatibility

| Feature | Android Minimum | iOS Minimum |
|---------|----------------|-------------|
| Clipboard Access | Android 6.0+ | iOS 14.0+ |
| Accessibility | Android 4.1+ | All versions |
| Background Activity | Android 6.0+ | iOS 7.0+ |

## Testing Accessibility Functionality

### Android Test Steps

1. **Enable accessibility service**
2. **Open any text-containing app** (notes, messaging, browser)
3. **Copy some text** (select → copy)
4. **Check for InstantCopy notification**
5. **Verify in app that text was detected**

### iOS Test Steps

1. **Enable accessibility in Settings**
2. **Open Safari or Notes app**
3. **Copy a piece of text**
4. **Check for InstantCopy notification**
5. **Verify in app that copy was detected**

## Diagnostic Commands

### Platform Detection Script
```bash
#!/bin/bash
# Save as check_permissions.sh

echo "=== InstantCopy Accessibility Check ==="

# Check if app is installed
if [ "$(uname)" = "Darwin" ]; then
    echo "iOS Device (use iOS Settings for verification)"
    echo "1. Settings → Accessibility → InstantCopy → ON"
    echo "2. Settings → General → Background App Refresh → InstantCopy → ON"
else
    echo "Android Device"
    adb devices > /dev/null 2>&1
    if [ $? -eq 0 ]; then
        echo "Connected Android device found"
        echo "Checking accessibility services..."
        adb shell settings get secure enabled_accessibility_services | grep -i instantcopy
    else
        echo "Manual check required: Settings → Accessibility → InstantCopy → ON"
    fi
fi

echo "=== Permission Status Check Complete ==="
```

## Success Verification

### ✅ Accessibility Working Correctly When:

1. **Android**:
   - Service shows "On" in Accessibility settings
   - No "permission denied" messages in app
   - Clipboard changes trigger app notifications
   - Background monitoring indicator appears when enabled

2. **iOS**:
   - InstantCopy listed as "On" in Accessibility settings
   - App shows successful permission status
   - Clipboard changes trigger visual feedback
   - Background app refresh is enabled

### ❌ Issues Persist When:

- Permission messages continue to appear
- No response to clipboard changes
- App crashes during permission setup
- Other apps cannot access clipboard (system-level conflict)

## Support Contact

If these troubleshooting steps don't resolve your issue:

- **Email**: support@instantcopy.app
- **GitHub Issues**: [Create detailed bug report](https://github.com/your-org/instantcopy/issues)
- **Include device model, OS version, and exact error messages**

---

**Remember**: Accessibility permissions are system-level features that require careful handling. Always follow official platform guidelines when modifying accessibility settings.