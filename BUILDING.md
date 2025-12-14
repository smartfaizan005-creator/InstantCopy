# Building InstantCopy

This guide explains how to build InstantCopy from source using the Kotlin Multiplatform Mobile (KMP) setup.

## Prerequisites

### For Both Platforms
- **Gradle**: 8.1.1 or later
- **Kotlin**: 1.9.20 or later
- **Java**: JDK 11 or later

### For Android
- **Android SDK**: API level 21+ (minSdk)
- **Android Gradle Plugin**: 8.1.2 or later
- **Android Studio** (optional, recommended)

### For iOS
- **Xcode**: 14.0 or later
- **iOS Deployment Target**: iOS 14.0+
- **CocoaPods** (for dependency management)

## Project Structure

```
instantcopy/
├── shared/                 # Kotlin Multiplatform shared code
│   └── src/
│       ├── commonMain/    # Common code for all platforms
│       ├── androidMain/   # Android-specific code
│       └── iosMain/       # iOS-specific code
├── android/               # Android app module
│   └── src/
│       └── main/
│           ├── kotlin/    # Kotlin source code
│           └── res/       # Android resources
├── ios/                   # iOS app files
├── build.gradle.kts       # Root build configuration
└── settings.gradle.kts    # Project settings
```

## Building

### Android APK Release Build

```bash
# Build release APK
./gradlew clean :android:assembleRelease

# Output location
# android/build/outputs/apk/release/app-release.apk
```

### Release Build Verification

```bash
# Check APK exists and get its size
ls -lh android/build/outputs/apk/release/

# Verify APK is under 3MB target
stat -c %s android/build/outputs/apk/release/app-release.apk

# Expected output format
# android/build/outputs/apk/release/app-release.apk (typically 2.5-2.8MB)
```

### Android App Bundle (for Play Store)

```bash
# Build release app bundle
./gradlew :android:bundleRelease

# Output location
# build/app/outputs/bundle/release/app-release.aab
```

### Debug APK

```bash
./gradlew :android:assembleDebug
```

### Clean Build

```bash
./gradlew clean
./gradlew :android:assembleRelease
```

## Size Optimization

The project is configured for minimal size with the following optimizations:

### 1. Code Shrinking (ProGuard/R8)
- Enabled in release builds
- Removes unused code and resources
- Configuration in `android/proguard-rules.pro`

### 2. Resource Shrinking
- Removes unused resources
- Enabled in `android/build.gradle.kts`

### 3. Kotlin Optimization
- Minimal stdlib usage
- No unnecessary dependencies
- Kotlin-only, no Java interop

### 4. Build Configuration
In `android/build.gradle.kts`:
```kotlin
buildTypes {
    release {
        isMinifyEnabled = true      // Code shrinking
        isShrinkResources = true    // Resource shrinking
        proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
        )
    }
}
```

## Verifying Binary Size

### Check APK Size
```bash
# After building, check the release APK size
ls -lh android/build/outputs/apk/release/app-release.apk

# Target: < 3MB (3,145,728 bytes)
# Typical: 2.5-2.8MB (2,621,440 - 2,938,496 bytes)
```

### Analyze APK Contents
```bash
# Extract and analyze the APK
unzip -l android/build/outputs/apk/release/app-release.apk | tail -1
```

## Development Build

For faster development iterations:

```bash
# Install debug APK on connected device
./gradlew :android:installDebug

# Launch debug app on device
./gradlew :android:installDebugAndRunDebugTests
```

## Testing

```bash
# Run unit tests
./gradlew :android:test

# Run instrumented tests (requires device/emulator)
./gradlew :android:connectedAndroidTest
```

## Troubleshooting

### Gradle Errors

**"Could not find com.android.tools.build:gradle"**
- Ensure Android SDK is installed
- Update SDK Manager in Android Studio
- Check internet connection for Maven repositories

**"Variant 'release' not found"**
- Clean build cache: `./gradlew clean`
- Rebuild from scratch

### Build Failures

**"Unable to find Kotlin stdlib"**
```bash
# Clear Gradle cache and rebuild
rm -rf ~/.gradle/caches/
./gradlew clean
./gradlew :android:assembleRelease
```

**"SDK not found"**
- Set `ANDROID_HOME` environment variable
- Or create `local.properties` with Android SDK path:
  ```
  sdk.dir=/path/to/android/sdk
  ```

## CI/CD Integration

### GitHub Automated Release

This project uses GitHub Actions for automated release builds:

**Trigger**: 
- Push to `main` branch
- Manual workflow dispatch

**Process**:
1. Build release APK using `./gradlew :android:assembleRelease`
2. Determine version from `android/build.gradle.kts`
3. Create GitHub Release with APK attached
4. Upload APK artifact for 90 days retention

**Output**:
- GitHub Release: `v{version}` (e.g., v1.0.0)
- APK artifact: `instantcopy-{version}-apk`

### Manual Release Process

For local release builds:

```bash
# Build and sign APK (requires keystore)
./gradlew clean :android:assembleRelease \
  -Pandroid.injected.signing.store.file=keystore.jks \
  -Pandroid.injected.signing.store.password=$KEYSTORE_PASS \
  -Pandroid.injected.signing.key.alias=$KEY_ALIAS \
  -Pandroid.injected.signing.key.password=$KEY_PASS

# Verify signed APK
jarsigner -verify android/build/outputs/apk/release/app-release.apk
```

## Performance Notes

- **Build Time**: ~2-3 minutes for release build on typical hardware
- **APK Size**: Target < 3MB with all optimizations enabled
- **Runtime**: Minimal memory footprint, optimized for background operation

## Additional Resources

- [Kotlin Multiplatform Documentation](https://kotlinlang.org/docs/multiplatform.html)
- [Android Gradle Plugin Documentation](https://developer.android.com/studio/releases/gradle-plugin)
- [ProGuard Documentation](https://www.guardsquare.com/en/proguard)

## Support

For build issues, please check:
1. This BUILDING.md file
2. [docs/accessibility-troubleshooting.md](docs/accessibility-troubleshooting.md)
3. GitHub Issues: https://github.com/your-org/instantcopy/issues
