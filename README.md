# Press Detector App

A Kotlin Multiplatform Mobile (KMM) application with a settings screen for managing press detection functionality.

## Features

- **Master Enable Toggle**: Enable/disable press detection functionality
- **Sensitivity Slider**: Adjust press duration threshold (0.1s - 2.0s)
- **Auto-Start**: Start automatically when device boots (platform-specific)
- **Background Service**: Keep running in background (platform-specific)

## Architecture

### Shared Module
- **Settings Repository**: Common business logic for settings management
- **Settings Storage**: Platform-agnostic storage interface
- **Multiplatform Persistence**: Settings persist across app restarts using:
  - Android: SharedPreferences
  - iOS: NSUserDefaults

### Android App
- Native UI using Jetpack Compose
- Material 3 Design components
- Accessibility labels for all interactive elements
- Boot receiver for auto-start functionality

### iOS App
- Native UI using SwiftUI
- Native iOS design patterns
- VoiceOver accessibility support
- Background mode support

## Settings Persistence

Settings are automatically saved when changed and loaded on app start. All settings survive app and device restarts.

## Accessibility

All UI elements include proper accessibility labels:
- Master Enable toggle
- Press duration threshold slider with value announcement
- Auto-Start toggle
- Background Service toggle

## Build

### Android
```bash
./gradlew :androidApp:assembleDebug
```

### iOS
Open `iosApp/iosApp.xcodeproj` in Xcode and build.

## Requirements

- Android: API 24+ (Android 7.0)
- iOS: iOS 14.1+
- Kotlin: 1.9.21
- Gradle: 8.1.4
