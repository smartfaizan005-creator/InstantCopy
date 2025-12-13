# Architecture Overview

## Settings Persistence Implementation

This application implements a shared settings screen with persistent storage using Kotlin Multiplatform.

### Core Components

#### 1. Shared Module (`/shared`)

**AppSettings.kt**
- `AppSettingsData`: Data class representing the settings state
  - `isEnabled`: Master toggle for the entire app
  - `pressDurationThreshold`: Sensitivity slider value (0.1s - 2.0s)
  - `autoStartEnabled`: Platform-specific auto-start on boot
  - `backgroundServiceEnabled`: Platform-specific background service
- `AppSettingsRepository`: Interface for settings management
- `AppSettingsRepositoryImpl`: Implementation with StateFlow for reactive updates

**SettingsStorage.kt**
- Platform-agnostic storage interface
- Supports Boolean, Float, and String values

**Platform Implementations**
- `AndroidSettingsStorage`: Uses Android SharedPreferences
- `IosSettingsStorage`: Uses iOS NSUserDefaults

### 2. Android App (`/androidApp`)

**MainActivity.kt**
- Jetpack Compose UI with Material 3 components
- Native Android design patterns
- Settings screen components:
  - `SettingToggle`: Reusable toggle component with accessibility labels
  - `SensitivitySlider`: Custom slider with value display
- Service management callbacks for:
  - Accessibility service
  - Background service
  - Auto-start configuration

**BootReceiver.kt**
- Handles BOOT_COMPLETED broadcast
- Auto-starts services based on saved settings

### 3. iOS App (`/iosApp`)

**ContentView.swift**
- SwiftUI native UI
- iOS design patterns
- Settings form with:
  - Toggle controls with descriptions
  - Slider with value labels
  - Accessibility labels for VoiceOver

**SettingsViewModel**
- ObservableObject for SwiftUI binding
- Manages settings state and persistence
- Triggers service configuration on changes

## Data Flow

1. **User Interaction**: User changes a setting in the UI
2. **Update Repository**: UI calls repository method (e.g., `setEnabled(true)`)
3. **Persist**: Repository saves to platform-specific storage
4. **Notify**: Repository updates StateFlow with new settings
5. **Reconfigure**: Observers receive update and reconfigure services immediately
6. **Survive Restart**: On app restart, settings are loaded from storage

## Persistence Details

### Android
- Storage: `SharedPreferences` in `MODE_PRIVATE`
- File: `app_settings.xml` in app's private data directory
- Persistence: Survives app kills and device restarts

### iOS
- Storage: `NSUserDefaults.standardUserDefaults`
- Persistence: Survives app kills and device restarts
- Synced automatically with `synchronize()`

## Accessibility

All UI components include proper accessibility metadata:
- **Content Descriptions**: Every interactive element has a descriptive label
- **Value Announcements**: Slider announces current value
- **Toggle States**: Switch states are automatically announced
- **Screen Reader Support**: Full VoiceOver (iOS) and TalkBack (Android) support

## Settings Keys

Settings are stored with the following keys:
- `is_enabled`: Boolean for master enable toggle
- `press_duration_threshold`: Float for sensitivity (seconds)
- `auto_start_enabled`: Boolean for auto-start
- `background_service_enabled`: Boolean for background service

## Future Enhancements

Placeholder methods exist for:
- `startAccessibilityService()`: Initialize press detection
- `stopAccessibilityService()`: Disable press detection
- `startBackgroundService()`: Enable background operation
- `stopBackgroundService()`: Disable background operation
- `updateAutoStart()`: Configure boot receiver state
