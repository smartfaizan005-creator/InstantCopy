# Documentation

This directory contains detailed documentation for InstantCopy.

## Available Documentation

### Core Documentation
- **[README.md](../README.md)** - Main project documentation with installation, build commands, settings, and privacy information

### Technical Guides

#### Size Optimization
- **[size-optimization.md](size-optimization.md)** - Comprehensive guide to keeping InstantCopy binaries under 3MB
  - Build optimization techniques
  - Asset compression strategies  
  - Verification procedures
  - Continuous monitoring setup

#### Accessibility Troubleshooting
- **[accessibility-troubleshooting.md](accessibility-troubleshooting.md)** - Detailed guide for resolving permission issues
  - Android accessibility setup and troubleshooting
  - iOS accessibility configuration
  - Common issues and solutions
  - Advanced diagnostic techniques

### Quick Reference

#### Build Commands
```bash
# Android optimized build
flutter build apk --release --tree-shake-icons --split-debug-info=build/debug-info/

# iOS release build
flutter build ios --release

# Analyze build size
flutter build apk --release --analyze-size
```

#### Permission Setup
**Android:**
- Settings → Accessibility → InstantCopy → ON
- Settings → Apps → InstantCopy → Battery → Don't optimize

**iOS:**
- Settings → Accessibility → InstantCopy → ON  
- Settings → General → Background App Refresh → InstantCopy → ON

#### Size Verification
```bash
# Check APK size (should be < 3MB)
ls -lh build/app/outputs/flutter-apk/app-release.apk

# Check component sizes
flutter build apk --release --analyze-size
```

## Documentation Structure

```
docs/
├── README.md                    # This file
├── size-optimization.md         # Binary size optimization guide
└── accessibility-troubleshooting.md # Permission troubleshooting

../screenshots/
├── android-settings.svg         # Android settings mockup
├── ios-settings.svg            # iOS settings mockup  
├── android-indicator.svg       # Android background indicator
└── ios-indicator.svg           # iOS background indicator
```

## Contributing to Documentation

When updating documentation:

1. **Maintain consistency** - Use the same formatting and terminology
2. **Include practical examples** - Add real commands and paths where possible
3. **Cross-reference** - Link related sections and files
4. **Update screenshots** - Keep mockups current with latest UI changes
5. **Test instructions** - Verify all commands work in current environment

## File Sizes

All screenshots are optimized SVG files:
- **Individual screenshots**: < 10KB each
- **Total documentation**: < 50KB total
- **Binary target**: < 3MB per platform

## Questions or Issues?

- **Documentation issues**: Create a [GitHub issue](https://github.com/your-org/instantcopy/issues)
- **Technical support**: Email support@instantcopy.app
- **Feature requests**: Open a [feature request](https://github.com/your-org/instantcopy/issues/new)

---

Last updated: December 2024