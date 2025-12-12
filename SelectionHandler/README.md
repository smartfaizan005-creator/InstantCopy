# iOS Selection Handler

A background-capable iOS service that automatically detects text selections system-wide and copies them to the UIPasteboard. The handler leverages UIAccessibility APIs and text interaction notifications to provide seamless text selection detection across all iOS apps including Safari, Notes, and Mail.

## Features

### Core Functionality
- **System-wide text selection detection** using UIAccessibility APIs
- **Background processing** with proper background modes configuration
- **Sensitivity threshold** to avoid accidental triggers
- **App extension** for enhanced detection capabilities
- **Real-time pasteboard monitoring** and management

### Supported Apps
- Safari (web content selection)
- Notes (note-taking selection)
- Mail (email content selection)
- Messages (text messaging selection)
- Twitter/X (social media content selection)
- Any other iOS app with text selection

### Technical Features
- **Accessibility integration** using UIAccessibility notifications
- **Background app refresh** and processing modes
- **Keep-alive mechanisms** for continuous operation
- **Notification support** for user feedback
- **Performance optimization** with configurable thresholds

## Project Structure

```
SelectionHandler/
├── SelectionHandler/           # Main iOS App
│   ├── AppDelegate.swift       # App lifecycle and background setup
│   ├── ViewController.swift    # Primary app interface
│   ├── SelectionHandlerDemoViewController.swift
│   ├── SelectionDetector.swift # Core selection detection logic
│   ├── PasteboardManager.swift # Pasteboard management
│   ├── Info.plist             # App configuration
│   └── Base.lproj/            # Storyboard files
├── SelectionHandlerExtension/  # Background app extension
│   ├── SelectionHandlerExtension.swift
│   └── Info.plist             # Extension configuration
├── Tests/                      # Integration and demo tests
│   ├── SelectionHandlerIntegrationTests.swift
│   └── SelectionHandlerDemoTests.swift
└── SelectionHandler.xcodeproj # Xcode project file
```

## Architecture

### SelectionDetector
Core class responsible for detecting text selections using:
- UIAccessibility notifications (`UIAccessibility.textSelectionChangedNotification`)
- Focused element monitoring
- First responder text extraction
- WebView selection detection (Safari integration)

Key methods:
- `startBackgroundDetection()` - Begins system-wide monitoring
- `stopBackgroundDetection()` - Stops all monitoring
- `performBackgroundRefresh()` - Background app refresh handler
- `updateSensitivityThreshold(_:)` - Configures trigger sensitivity

### BackgroundSelectionDetector
Extension-based detector for enhanced background capabilities:
- Periodic system checks
- Heuristic selection validation
- Keep-alive mechanisms
- Silent operation mode

### PasteboardManager
Manages system-wide pasteboard operations:
- Content monitoring and change detection
- Automatic text copying with validation
- Pasteboard change notifications
- Content validation and filtering

### SelectionHandlerExtension
UI Services extension providing:
- Enhanced background processing
- System-wide accessibility access
- Keep-alive timer implementation
- Silent notification support

## Configuration

### Background Modes
The app enables multiple background modes in `Info.plist`:
- `background-processing` - Core background execution
- `background-app-refresh` - Periodic background updates

### Required Permissions
- **Notifications** - User feedback for selection events
- **Accessibility** - System-wide text selection access

### Sensitivity Configuration
Default sensitivity threshold is 3 characters. Users can adjust this via:
- App slider control (1-20 characters)
- Programmatic configuration
- Background mode specific thresholds

## Usage

### Basic Setup

1. **Initialize the detector**:
```swift
let selectionDetector = SelectionDetector()
let pasteboardManager = PasteboardManager()
```

2. **Start monitoring**:
```swift
selectionDetector.startBackgroundDetection()
pasteboardManager.startMonitoring()
```

3. **Configure sensitivity**:
```swift
selectionDetector.updateSensitivityThreshold(5) // 5 character minimum
```

### Background Operation

The system automatically starts background detection when the app enters background:

```swift
func applicationDidEnterBackground(_ application: UIApplication) {
    selectionDetector.startBackgroundDetection()
}
```

### Notification Handling

Listen for selection events:

```swift
NotificationCenter.default.addObserver(
    self,
    selector: #selector(selectionCopied(_:)),
    name: .selectionCopiedToPasteboard,
    object: nil
)
```

## Integration Testing

### Test Coverage

The project includes comprehensive integration tests for:

#### Safari Integration
- Website content selection detection
- Long paragraph selections
- URL text selections
- Multi-element page selections

#### Notes Integration
- Single word selections
- Paragraph selections
- Multiple paragraph selections
- List item selections

#### Mail Integration
- Sender name selections
- Subject line selections
- Email body selections
- Contact information selections

### Running Tests

```bash
# Run all tests
xcodebuild test -project SelectionHandler.xcodeproj -scheme SelectionHandlerTests

# Run specific test class
xcodebuild test -project SelectionHandler.xcodeproj -scheme SelectionHandlerTests -only-selection-handler.SelectionHandlerIntegrationTests

# Run with detailed output
xcodebuild test -project SelectionHandler.xcodeproj -scheme SelectionHandlerTests -enableCodeCoverage YES
```

### Demo Harness

The project includes a comprehensive demo harness (`SelectionHandlerDemoViewController`) that provides:

- Real-time selection monitoring interface
- Multi-app selection simulation
- Background mode testing
- Log visualization and debugging

## Platform Limitations

### iOS Restrictions
- **Accessibility permissions** required for system-wide access
- **Background execution limits** may affect continuous operation
- **App extension constraints** limit direct UI access
- **Privacy considerations** require user consent

### System-wide Detection Challenges
- Sandboxed app limitations prevent direct access to other app content
- Accessibility APIs provide the primary mechanism for selection detection
- Some apps may have custom text selection implementations
- System security features may limit background processing

## Security & Privacy

### Data Handling
- Selected text is processed locally only
- No external data transmission
- Pasteboard access follows iOS security guidelines
- User consent required for accessibility features

### Privacy Protection
- Temporary storage of selected text
- Automatic clearing of sensitive content
- User-configurable sensitivity thresholds
- Transparent operation with notification feedback

## Performance Considerations

### Optimization Strategies
- **Periodic checking** instead of continuous monitoring
- **Heuristic filtering** to reduce false positives
- **Threshold-based validation** to minimize processing
- **Background mode optimization** for battery efficiency

### Resource Management
- Proper timer management and cleanup
- Memory-efficient text processing
- Background thread operations for UI responsiveness
- Automatic detection pausing when inactive

## Deployment Requirements

### Development
- Xcode 15.0+
- iOS 17.0+ deployment target
- Swift 5.0+
- Accessibility framework access

### Production
- App Store submission considerations
- Accessibility permission explanation in app review
- Privacy policy updates for system-wide access
- Background modes entitlement approval

## Future Enhancements

### Potential Improvements
- **Machine learning** for better selection classification
- **User learning** for personalized thresholds
- **Cloud sync** for cross-device pasteboard sharing
- **Smart filtering** for unwanted content detection
- **Advanced app integration** with popular third-party apps

### Technical Roadmap
- Support for iOS 18+ features
- Enhanced accessibility API integration
- Improved battery optimization
- Advanced notification management
- Cross-platform compatibility considerations

## Troubleshooting

### Common Issues
- **No selections detected**: Check accessibility permissions
- **False positives**: Increase sensitivity threshold
- **Background stoppage**: Verify background modes configuration
- **Performance issues**: Review periodic checking intervals

### Debug Mode
Enable debug logging in development:
```swift
#if DEBUG
print("Selection detected: \(text)")
#endif
```

### Validation Tools
- Use demo harness for testing
- Monitor system logs for background activity
- Check notification delivery in settings
- Validate pasteboard changes in real-time

---

*This implementation provides a robust foundation for system-wide text selection detection on iOS, with comprehensive testing and demonstration capabilities for validation across multiple apps and use cases.*