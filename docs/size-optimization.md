# Size Optimization Guide

This document outlines the steps taken to ensure InstantCopy binaries stay under 3MB while maintaining full functionality.

## Optimization Techniques Applied

### 1. Flutter Build Optimizations

#### Tree Shaking
```bash
flutter build apk --release --tree-shake-icons --split-debug-info=build/debug-info/
```
- Removes unused Flutter framework code
- Eliminates unused icons from the icon font
- Can reduce APK size by 1-2MB

#### Code Splitting
```bash
--split-debug-info=build/debug-info/
```
- Separates debug symbols from release builds
- Reduces binary size while maintaining debug capability
- Essential for staying under 3MB target

### 2. Android-Specific Optimizations

#### R8/ProGuard Configuration
**android/app/build.gradle:**
```gradle
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

#### ProGuard Rules (android/app/proguard-rules.pro)
```proguard
# Flutter specific rules
-keep class io.flutter.app.** { *; }
-keep class io.flutter.plugin.** { *; }
-keep class io.flutter.util.** { *; }
-keep class io.flutter.view.** { *; }
-keep class io.flutter.** { *; }
-keep class io.flutter.plugins.** { *; }

# Remove logging in release
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}
```

#### APK Splitting
```bash
# Create APK splits for different architectures
flutter build apk --split-per-abi --release
```
This creates smaller APKs for specific architectures:
- app-arm64-v8a-release.apk
- app-armeabi-v7a-release.apk
- app-x86_64-release.apk

### 3. Asset Optimization

#### Image Compression
```bash
# Convert PNG to WebP for better compression
cwebp input.png -q 80 -o output.webp

# Optimize PNG files
optipng -o2 input.png
```

#### Font Optimization
- Use only necessary font weights
- Consider system fonts where possible
- Subset custom fonts to include only required characters

#### Remove Unused Assets
```bash
# Analyze asset usage
flutter build apk --analyze-size
```

### 4. Code Optimization

#### Remove Unused Dependencies
```bash
# Check for unused packages
flutter pub deps
```

#### Minimal Package Set
Core dependencies only:
- `flutter/material.dart` (UI framework)
- `permission_handler` (permissions)
- `shared_preferences` (settings)
- `flutter/services` (clipboard)

#### Avoid Heavy Libraries
- Don't include full featured HTTP libraries if not needed
- Use platform-specific APIs where available
- Prefer lightweight alternatives

### 5. Verification Steps

#### Size Analysis
```bash
# Analyze final APK size
flutter build apk --release --analyze-size

# Check IPA size (macOS only)
ls -lh build/ios/Release-iphoneos/instantcopy.app/InstantCopy
```

#### Binary Analysis
```bash
# Android binary analysis
aapt dump badging build/app/outputs/flutter-apk/app-release.apk

# iOS binary analysis (requires Xcode)
size build/ios/Release-iphoneos/InstantCopy.app/InstantCopy
```

### 6. Target Sizes

| Component | Target Size | Current Implementation |
|-----------|-------------|----------------------|
| **Android APK** | < 3MB | 2.8MB (target) |
| **iOS IPA** | < 3MB | 2.9MB (target) |
| **App Bundle** | < 3MB | 2.6MB (target) |
| **Individual Assets** | < 100KB | < 50KB each |

### 7. Build Commands Reference

#### Development Builds
```bash
# Fast development build
flutter build apk --debug

# Profile build (closest to release performance)
flutter build apk --profile
```

#### Release Builds
```bash
# Standard release APK
flutter build apk --release

# Split APK by architecture
flutter build apk --split-per-abi --release

# App Bundle (Play Store recommended)
flutter build appbundle --release

# iOS Release
flutter build ios --release
```

### 8. Continuous Monitoring

#### Pre-commit Hook
```bash
#!/bin/bash
# .git/hooks/pre-commit
echo "Checking build size..."

cd android
./gradlew assembleRelease

APK_SIZE=$(du -k build/outputs/flutter-apk/app-release.apk | cut -f1)
if [ $APK_SIZE -gt 3000 ]; then
    echo "❌ APK size ($APK_SIZE KB) exceeds 3MB limit"
    exit 1
fi

echo "✅ APK size ($APK_SIZE KB) is under 3MB"
```

#### GitHub Action for Size Validation
```yaml
# .github/workflows/build-size-check.yml
name: Build Size Check
on: [push, pull_request]

jobs:
  size-check:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - uses: subosito/flutter-action@v2
      - run: flutter build apk --release --analyze-size
      - name: Check APK size
        run: |
          APK_SIZE=$(du -k build/app/outputs/flutter-apk/app-release.apk | cut -f1)
          if [ $APK_SIZE -gt 3000 ]; then
            echo "❌ APK size ($APK_SIZE KB) exceeds 3MB limit"
            exit 1
          fi
          echo "✅ APK size ($APK_SIZE KB) is under 3MB"
```

### 9. Emergency Optimization Techniques

If approaching size limit:

1. **Remove debug symbols**: Ensure `--split-debug-info` is used
2. **Disable assert statements**: Release builds should not include assertions
3. **Remove development dependencies**: Check `pubspec.yaml` for dev-only packages
4. **Compress assets further**: Use aggressive WebP compression (quality 70)
5. **Remove native libraries**: Use shared Flutter engine instead of bundling
6. **Strip unused code**: Enable aggressive R8 optimizations

### 10. Success Criteria

✅ **Target Achieved When:**
- APK size < 3MB (3072 KB)
- IPA size < 3MB (3072 KB)
- All functionality preserved
- No performance degradation
- Accessibility features fully functional

---

**Note**: Always test optimized builds thoroughly on real devices to ensure no functionality is broken by optimizations.