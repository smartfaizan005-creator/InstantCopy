# InstantCopy iOS Settings

A lightweight iOS settings screen for the InstantCopy app that allows users to configure copy gesture behavior with real-time background service updates.

## Features

### Settings Controls
- **Master Enable Toggle**: Turn InstantCopy functionality on/off
- **Sensitivity Slider**: Adjust press duration threshold (100ms - 2000ms)
- **Background Mode Toggle**: Enable/disable background processing
- **Real-time Configuration**: Immediate updates to background service

### Implementation Details

#### Native iOS Architecture
- **Framework**: SwiftUI for modern, declarative UI
- **Language**: Swift 5.0+
- **iOS Deployment Target**: 17.0+

#### Settings Management
- **Persistence**: All settings stored in `UserDefaults`
- **Real-time Updates**: Settings changes immediately apply to background service
- **Validation**: Sensitivity range enforcement (100ms - 2000ms)
- **Default Values**: Sensible defaults for all settings

#### Accessibility
- **VoiceOver Support**: All controls have accessibility labels
- **Clear Descriptions**: Helpful context for each setting
- **Semantic Hints**: Users understand what each control does

## Project Structure

```
InstantCopy/
├── InstantCopyApp.swift          # Main app entry point
├── ContentView.swift             # Main view with tappable app icon
├── SettingsView.swift            # Settings UI implementation
├── SettingsManager.swift         # Settings persistence & real-time updates
├── BackgroundService.swift       # Background processing management
├── SettingsTests.swift           # Unit tests for settings
├── Info.plist                    # App configuration
├── Assets.xcassets/              # App icons and colors
└── Base.lproj/                   # Localization resources
```

## Settings Details

### Master Enable Toggle
- **Key**: `isInstantCopyEnabled`
- **Type**: Bool
- **Default**: false
- **Effect**: Enables/disables all InstantCopy functionality

### Sensitivity Slider
- **Key**: `instantCopySensitivity`
- **Type**: Double (milliseconds)
- **Range**: 100ms - 2000ms
- **Default**: 500ms
- **Step**: 50ms increments
- **Effect**: Controls how long user must press to trigger copy gesture

### Background Mode Toggle
- **Key**: `instantCopyBackgroundMode`
- **Type**: Bool
- **Default**: false
- **Effect**: Keeps helper running when app is backgrounded

## Real-time Configuration

### Settings Changes
When any setting is modified:
1. Value is immediately persisted to `UserDefaults`
2. Settings manager posts `instantCopySettingsChanged` notification
3. Background service receives notification and updates configuration
4. UI reflects current state with visual indicators

### Background Service Integration
The `BackgroundService` class:
- Subscribes to settings change notifications
- Manages background tasks when appropriate
- Responds to app lifecycle events
- Provides real-time copy gesture detection simulation

## Testing

### Unit Tests
- Settings persistence verification
- Range validation testing
- Real-time update notification testing
- Default value initialization testing

### Manual Testing Checklist
- [ ] Settings persist across app restarts
- [ ] Sensitivity changes apply immediately
- [ ] Background mode works correctly
- [ ] Master toggle enables/disables functionality
- [ ] Accessibility labels work with VoiceOver
- [ ] Settings sheet dismisses properly

## Usage

### Opening Settings
1. Tap the app icon on the main screen
2. Settings sheet appears with current configuration
3. Adjust settings as needed
4. Changes are applied immediately
5. Tap "Done" to close settings

### Testing Settings Persistence
1. Open settings and modify values
2. Force close the app (swipe up from bottom)
3. Reopen the app
4. Open settings - values should persist

## Dependencies

- **UIKit**: For background task management
- **Foundation**: UserDefaults and notification system
- **SwiftUI**: All user interface components
- **XCTest**: Unit testing framework

## Development

### Building the Project
1. Open `InstantCopy.xcodeproj` in Xcode 15+
2. Select target device (iPhone/iPad simulator or device)
3. Build and run (Cmd+R)

### Running Tests
1. Open the test navigator (Cmd+6)
2. Select `SettingsTests` test plan
3. Run tests (Cmd+U)

## Technical Considerations

### iOS Compatibility
- Requires iOS 17.0 or later
- Uses modern SwiftUI features (iOS 15+)
- Supports both iPhone and iPad form factors

### Performance
- Settings changes are processed on main thread
- Background tasks are properly managed
- No memory leaks or retain cycles

### Security & Privacy
- Uses standard UserDefaults (not for sensitive data)
- Background processing follows iOS guidelines
- Proper background task cleanup

## Future Enhancements

Potential improvements:
- Settings export/import functionality
- More granular sensitivity presets
- Custom themes and appearance options
- Advanced gesture recognition tuning
- Performance analytics and monitoring