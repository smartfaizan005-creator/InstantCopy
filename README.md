# InstantCopy Settings

A lightweight Android settings screen for managing InstantCopy accessibility service.

## Features

- **Master Enable Toggle**: Turn InstantCopy functionality on/off
- **Sensitivity Slider**: Adjust press duration threshold (100-3000ms)
- **Auto-start on Boot**: Automatically enable InstantCopy when device boots
- **Real-time Configuration**: Changes immediately apply to the accessibility service
- **Persistent Settings**: All settings saved to SharedPreferences
- **Accessibility Support**: Full accessibility labels for screen readers

## Architecture

### Core Components

1. **MainActivity** (Jetpack Compose): Primary settings screen with modern Material Design 3 UI
2. **SettingsActivity** (XML Layouts): Alternative settings screen using traditional Android layouts
3. **SettingsManager**: Centralized settings management with SharedPreferences integration
4. **InstantCopyAccessibilityService**: Android accessibility service that responds to settings changes
5. **BootReceiver**: Handles auto-start functionality on device boot

### Settings Structure

```kotlin
companion object {
    const val PREFS_NAME = "InstantCopySettings"
    const val KEY_MASTER_ENABLED = "master_enabled"
    const val KEY_SENSITIVITY = "sensitivity"
    const val KEY_AUTO_START = "auto_start"
}
```

### Real-time Updates

The settings immediately apply to the accessibility service through:
- Direct method calls to `InstantCopyAccessibilityService.updateSettings()`
- SharedPreferences change listeners
- Settings.Secure updates for accessibility service registration

## UI Implementation

### Jetpack Compose Implementation

Modern, reactive UI with:
- Material Design 3 components
- Accessibility semantics
- Real-time state management
- Automatic accessibility service updates

### XML Layout Implementation

Traditional Android UI with:
- Card-based layout design
- SeekBar for sensitivity adjustment
- Switch components for toggles
- Accessibility content descriptions

## Accessibility Features

- All interactive elements have content descriptions
- Screen reader compatible
- Proper semantic grouping
- Voice-over friendly navigation

## Installation

1. Open project in Android Studio
2. Sync Gradle files
3. Build and install on Android device
4. Grant accessibility permission when prompted

## Usage

1. Tap app icon to open settings
2. Enable "Master Enable" to activate InstantCopy
3. Adjust sensitivity slider if needed
4. Enable "Auto-start on Boot" for automatic activation
5. Settings persist across app restarts
6. Changes apply immediately to accessibility service

## Testing

The implementation includes:
- Settings persistence testing
- Accessibility service real-time updates
- Boot receiver functionality
- UI accessibility compliance

## Technical Details

- Minimum SDK: 24 (Android 7.0)
- Target SDK: 34 (Android 14)
- Uses Jetpack Compose for modern UI
- Fallback XML layouts for compatibility
- SharedPreferences for data persistence
- AccessibilityService for touch detection
- BroadcastReceiver for boot handling