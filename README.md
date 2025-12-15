# InstantCopy

A lightweight cross-platform clipboard manager with accessibility permissions for automatic text copying across Android and iOS devices.

## Features

- 🔄 **Automatic Clipboard Detection**: Monitors clipboard changes across both platforms
- 🔒 **Privacy-First**: No network requests, no authentication, completely offline
- 📱 **Cross-Platform**: Works seamlessly on Android and iOS
- ⚡ **Lightweight**: Optimized binaries under 3MB
- ♿ **Accessibility-Based**: Uses platform accessibility services for reliable operation

## Download

### Android (APK)

- **GitHub Releases**: https://github.com/smartfaizan005-creator/InstantCopy/releases
- **Latest release**: https://github.com/smartfaizan005-creator/InstantCopy/releases/latest

Installation:

1. Download the APK asset from the latest GitHub Release (usually named `android-release-unsigned.apk`).
2. On your Android device, allow installs from unknown sources if prompted ("Install unknown apps").
3. Open the downloaded APK and confirm installation.

Note: the APK is auto-built on every push to the `main` branch.

## Build Commands

### Prerequisites
- Java 11 or later (JDK)
- Gradle 8.1.1 or later
- Kotlin 1.9.20 or later
- Android SDK (API 21+) for Android builds
- Xcode 14+ for iOS builds

### Android Build
```bash
# Clean build
./gradlew clean

# Debug APK
./gradlew :android:assembleDebug

# Release APK
./gradlew clean :android:assembleRelease

# Output path
ls -lh android/build/outputs/apk/release/android-release-unsigned.apk

# App Bundle (for Play Store)
./gradlew :android:bundleRelease
```

### iOS Build
```bash
# Debug build
./gradlew :shared:build -Pkotlin.native.cocoapods.version.suffix=

# Release IPA (requires Apple developer account)
# Build via Xcode after Gradle compilation
xcodebuild -workspace ios/InstantCopy.xcworkspace -scheme InstantCopy -configuration Release
```

### Gradle Wrapper Commands
```bash
# View tasks
./gradlew tasks

# Build all targets
./gradlew build

# Clean all builds
./gradlew clean

# Run verification script
bash verify-build.sh
```

See [BUILDING.md](BUILDING.md) for detailed build instructions.

## Settings Explanation

### Android Settings

1. **Accessibility Service**: Enable "InstantCopy" in Android Accessibility settings
   - Settings → Accessibility → InstantCopy → ON
   - Required for clipboard monitoring functionality

2. **Background Apps**: Allow background activity
   - Settings → Apps → InstantCopy → Battery → No restrictions
   - Ensures continuous clipboard monitoring

3. **Notifications**: Enable clipboard notifications
   - Settings → Apps → InstantCopy → Notifications → ON
   - Shows copying status and app activity

### iOS Settings

1. **Accessibility**: Enable InstantCopy in Accessibility settings
   - Settings → Accessibility → InstantCopy → ON
   - Necessary for clipboard access on iOS 14+

2. **Background App Refresh**: Enable for continuous monitoring
   - Settings → General → Background App Refresh → InstantCopy → ON

3. **Notifications**: Allow notification permissions
   - Settings → Notifications → InstantCopy → Allow Notifications

### App Settings

- **Copy Notification**: Toggle visual feedback when text is copied
- **Background Indicator**: Show subtle indicator when app is monitoring
- **Sensitivity**: Adjust clipboard change detection sensitivity
- **Privacy Mode**: Disable all logging and analytics

## Privacy Notes

### No Network Communication
- ✅ **No internet requests**: App operates completely offline
- ✅ **No analytics**: Zero data collection or transmission
- ✅ **No cloud sync**: All data stays local on device

### No Authentication
- ✅ **No accounts**: No login, signup, or user management
- ✅ **No API keys**: No external service dependencies
- ✅ **No tracking**: No device fingerprinting or user tracking

### Local Data Storage
- 📱 **Clipboard history**: Stored locally with opt-in only
- 🔒 **Encryption**: Local data encrypted when stored
- 🗑️ **Automatic cleanup**: Configurable retention periods

## Screenshots

| Platform | Settings Page | Background Indicator |
|----------|---------------|---------------------|
| **Android** | ![Android Settings](screenshots/android-settings.png) | ![Android Indicator](screenshots/android-indicator.png) |
| **iOS** | ![iOS Settings](screenshots/ios-settings.png) | ![iOS Indicator](screenshots/ios-indicator.png) |

*Screenshots are optimized and under 100KB each for fast loading*

## Size Optimization

InstantCopy is built with the Kotlin Multiplatform Mobile framework, optimized for minimal binary size.

### Build Optimizations Applied

1. **ProGuard/R8 Code Shrinking**
   - Enabled in release builds
   - Removes unused code and unused classes
   - Configuration in `android/proguard-rules.pro`

2. **Resource Shrinking**
   - Removes unused Android resources
   - Enabled via `isShrinkResources = true`

3. **Kotlin Optimization**
   - Minimal stdlib dependencies
   - No unnecessary third-party libraries
   - Pure Kotlin implementation

4. **Kotlin Native Optimization (iOS)**
   - Optimized for iOS binary size
   - Minimal runtime overhead
   - Configuration in `shared/build.gradle.kts`

### Verification Commands

```bash
# Build release APK
./gradlew clean :android:assembleRelease

# Output APK
ls -lh android/build/outputs/apk/release/android-release-unsigned.apk

# Analyze size breakdown
./gradlew :android:assembleRelease --info | grep "size"
```

### Target Sizes
- **Android APK**: < 3MB (typically 2.5-2.8 MB)
- **iOS IPA**: < 3MB (typically 2.2-2.6 MB)
- **Binary**: Minimal footprint with accessibility service support
- **Documentation**: All under 100KB per file

## Troubleshooting

### Accessibility Permissions Not Working

#### Android
1. **Check Accessibility Service Status**
   - Go to Settings → Accessibility
   - Verify "InstantCopy" shows as "On"
   - If not listed, restart the app and try again

2. **Grant Necessary Permissions**
   ```bash
   # Via ADB (for testing)
   adb shell settings put secure enabled_accessibility_services_com_instantcopy_app/com_instantcopy.service.ClipboardAccessibilityService
   ```

3. **Clear App Data and Restart**
   - Settings → Apps → InstantCopy → Storage → Clear Data
   - Restart app and re-grant permissions

#### iOS
1. **Enable Accessibility in Settings**
   - Settings → Accessibility → InstantCopy → ON
   - Toggle off and on if not working

2. **Check Background App Refresh**
   - Settings → General → Background App Refresh
   - Ensure InstantCopy is enabled

3. **Reset Accessibility Permissions**
   - Settings → General → Transfer or Reset iPhone → Reset Location & Privacy
   - Re-grant accessibility permission after reset

4. **iOS Version Compatibility**
   - iOS 14.0+ required for clipboard access
   - iOS 15.0+ recommended for best compatibility

### Common Issues

**App Crashes on Launch**
- Ensure sufficient device storage (>50MB free)
- Check Flutter version compatibility
- Clear app data and reinstall

**Clipboard Not Detected**
- Verify accessibility permissions are granted
- Check if other clipboard apps are interfering
- Restart device if permissions were recently granted

**High Battery Usage**
- Disable background indicator
- Reduce sensitivity settings
- Check for other battery-intensive apps

### Debug Mode

Enable debug logging for troubleshooting:
```bash
# Android (via ADB)
adb logcat | grep -i instantcopy

# Gradle verbose output
./gradlew :android:assembleDebug --debug

# iOS (via Xcode)
# View console output in Xcode Devices window
# Or use system log: log stream --predicate 'eventMessage contains "InstantCopy"'
```

### Build Troubleshooting

For build-related issues, see [BUILDING.md](BUILDING.md#troubleshooting)

## Development

### Contributing
1. Fork the repository
2. Create feature branch: `git checkout -b feature/amazing-feature`
3. Commit changes: `git commit -m 'Add amazing feature'`
4. Push to branch: `git push origin feature/amazing-feature`
5. Open a Pull Request

### License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

### Support
- 📧 Email: support@instantcopy.app
- 🐛 Issues: [GitHub Issues](https://github.com/your-org/instantcopy/issues)
- 📖 Wiki: [Detailed Documentation](https://github.com/your-org/instantcopy/wiki)

---

**InstantCopy** - Privacy-first clipboard management made simple.