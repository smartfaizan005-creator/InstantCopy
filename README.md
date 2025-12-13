# InstantCopy

A lightweight cross-platform clipboard manager with accessibility permissions for automatic text copying across Android and iOS devices.

## Features

- 🔄 **Automatic Clipboard Detection**: Monitors clipboard changes across both platforms
- 🔒 **Privacy-First**: No network requests, no authentication, completely offline
- 📱 **Cross-Platform**: Works seamlessly on Android and iOS
- ⚡ **Lightweight**: Optimized binaries under 3MB
- ♿ **Accessibility-Based**: Uses platform accessibility services for reliable operation

## One-Line Installation

### Android (Obtainium)
```
https://github.com/your-org/instantcopy/releases/latest/download/instantcopy-android.apk
```

### iOS (GitHub)
```
https://github.com/your-org/instantcopy/releases/latest/download/instantcopy-ios.ipa
```

## Build Commands

### Prerequisites
- Flutter SDK 3.16.0+ 
- Dart SDK 3.2.0+
- Android Studio / Xcode for platform builds

### Android Build
```bash
# Debug build
flutter build apk --debug

# Release APK (optimized)
flutter build apk --release --tree-shake-icons --split-debug-info=build/debug-info/

# App Bundle (recommended for Play Store)
flutter build appbundle --release --tree-shake-icons --split-debug-info=build/debug-info/
```

### iOS Build
```bash
# Debug build
flutter build ios --debug

# Release IPA (requires developer account)
flutter build ios --release --tree-shake-icons --split-debug-info=build/debug-info/
```

### Cross-Platform Commands
```bash
# Clean build
flutter clean && flutter pub get

# Analyze code
flutter analyze

# Run tests
flutter test

# Format code
flutter format .
```

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

### Build Optimizations Applied

1. **Tree Shaking**: Remove unused Flutter framework code
   ```bash
   flutter build apk --release --tree-shake-icons
   ```

2. **Code Splitting**: Split debug information for smaller release builds
   ```bash
   --split-debug-info=build/debug-info/
   ```

3. **ProGuard/R8**: Enable code obfuscation and shrinking
   ```bash
   # android/app/build.gradle
   android {
       buildTypes {
           release {
               minifyEnabled true
               shrinkResources true
               proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
           }
       }
   }
   ```

4. **Asset Optimization**: Compress images and remove unused assets
   - PNG optimization: 24-bit → 8-bit where possible
   - WebP conversion for better compression
   - Remove unused font files

### Verification Commands

```bash
# Check APK size
ls -lh build/app/outputs/flutter-apk/app-release.apk

# Check IPA size (macOS only)
ls -lh build/ios/Release-iphoneos/instantcopy.app

# Analyze binary composition
flutter build apk --release --analyze-size
```

### Target Sizes
- **Android APK**: < 3MB
- **iOS IPA**: < 3MB
- **Individual assets**: < 100KB each

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
adb logcat | grep InstantCopy

# iOS (via Xcode)
# View console output in Xcode Devices window
```

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