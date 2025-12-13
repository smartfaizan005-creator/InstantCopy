#!/bin/bash

# InstantCopy Build Verification Script
# Verifies KMP project structure and build requirements

set -e

echo "🔍 InstantCopy KMP Build Verification"
echo "====================================="

# Check if we're in the right directory
if [ ! -f "README.md" ] || [ ! -d "screenshots" ]; then
    echo "❌ Error: Please run this script from the project root directory"
    exit 1
fi

# Verify directory structure
echo ""
echo "📁 Checking Project Structure..."

REQUIRED_DIRS=(
    "shared"
    "android"
    "ios"
    "docs"
    "screenshots"
)

for dir in "${REQUIRED_DIRS[@]}"; do
    if [ -d "$dir" ]; then
        echo "✅ $dir/ directory exists"
    else
        echo "❌ $dir/ directory missing"
        exit 1
    fi
done

# Verify build files
echo ""
echo "📋 Checking Build Configuration..."

BUILD_FILES=(
    "build.gradle.kts"
    "settings.gradle.kts"
    "gradle.properties"
    "android/build.gradle.kts"
    "shared/build.gradle.kts"
)

for file in "${BUILD_FILES[@]}"; do
    if [ -f "$file" ]; then
        echo "✅ $file exists"
    else
        echo "❌ $file missing"
        exit 1
    fi
done

# Verify source code
echo ""
echo "🔧 Checking Source Code..."

SOURCE_FILES=(
    "shared/src/commonMain/kotlin/com/instantcopy/ClipboardContent.kt"
    "shared/src/commonMain/kotlin/com/instantcopy/SettingsState.kt"
    "shared/src/commonMain/kotlin/com/instantcopy/PlatformService.kt"
    "android/src/main/kotlin/com/instantcopy/MainActivity.kt"
    "android/src/main/kotlin/com/instantcopy/service/ClipboardAccessibilityService.kt"
    "android/src/main/kotlin/com/instantcopy/receiver/BootCompletedReceiver.kt"
    "ios/PlatformServiceIOS.kt"
    "ios/AppDelegate.swift"
)

for file in "${SOURCE_FILES[@]}"; do
    if [ -f "$file" ]; then
        echo "✅ $file exists"
    else
        echo "❌ $file missing"
        exit 1
    fi
done

# Verify documentation completeness
echo ""
echo "📚 Checking Documentation..."

if [ -f "README.md" ]; then
    echo "✅ README.md exists"
    SIZE=$(wc -c < README.md)
    echo "   Size: $SIZE bytes"
else
    echo "❌ README.md missing"
    exit 1
fi

if [ -d "docs" ]; then
    echo "✅ docs/ directory exists"
    DOC_COUNT=$(find docs -name "*.md" | wc -l)
    echo "   Documentation files: $DOC_COUNT"
else
    echo "❌ docs/ directory missing"
    exit 1
fi

# Verify screenshots
echo ""
echo "📸 Checking Screenshots..."

SCREENSHOT_FILES=(
    "screenshots/android-settings.svg"
    "screenshots/ios-settings.svg" 
    "screenshots/android-indicator.svg"
    "screenshots/ios-indicator.svg"
)

TOTAL_SCREENSHOT_SIZE=0
for file in "${SCREENSHOT_FILES[@]}"; do
    if [ -f "$file" ]; then
        SIZE=$(wc -c < "$file")
        echo "✅ $file ($SIZE bytes)"
        TOTAL_SCREENSHOT_SIZE=$((TOTAL_SCREENSHOT_SIZE + SIZE))
        if [ $SIZE -gt 100000 ]; then
            echo "   ⚠️  Warning: File larger than 100KB"
        fi
    else
        echo "❌ $file missing"
        exit 1
    fi
done

echo "📊 Total screenshots size: $TOTAL_SCREENSHOT_SIZE bytes"

# Verify documentation sections
echo ""
echo "📋 Checking Documentation Content..."

REQUIRED_SECTIONS=(
    "Installation"
    "Build Commands" 
    "Settings"
    "Privacy"
    "Size Optimization"
    "Troubleshooting"
)

for section in "${REQUIRED_SECTIONS[@]}"; do
    if grep -qi "$section" README.md; then
        echo "✅ README.md contains '$section' section"
    else
        echo "❌ README.md missing '$section' section"
    fi
done

# Check accessibility troubleshooting
if [ -f "docs/accessibility-troubleshooting.md" ] && grep -qi "Accessibility" docs/accessibility-troubleshooting.md; then
    echo "✅ Accessibility troubleshooting documentation exists"
else
    echo "❌ Accessibility troubleshooting missing"
fi

# Check privacy documentation
if grep -qi "privacy\|network\|auth" README.md; then
    echo "✅ README.md includes privacy documentation"
else
    echo "❌ README.md missing privacy documentation"
fi

# Summary
echo ""
echo "📊 Verification Summary"
echo "======================"

TOTAL_DOCS=$(find . -name "*.md" | wc -l)
TOTAL_SCREENSHOTS=$(find screenshots -name "*.svg" 2>/dev/null | wc -l)

echo "Documentation files: $TOTAL_DOCS"
echo "Screenshot files: $TOTAL_SCREENSHOTS"
echo "Total screenshot size: $TOTAL_SCREENSHOT_SIZE bytes"

# Size optimization summary
if [ $TOTAL_SCREENSHOT_SIZE -lt 500000 ]; then
    echo "🎉 Screenshots are well optimized (< 500KB total)"
else
    echo "⚠️  Screenshots could be further optimized"
fi

echo ""
echo "✅ Project structure verification complete!"
echo ""
echo "Next steps for actual build verification:"
echo "1. Run: ./gradlew build"
echo "2. Run: ./gradlew assembleRelease (for Android APK)"
echo "3. Check: build/app/outputs/apk/release/app-release.apk"
echo ""
echo "All should be under 3MB (3145728 bytes) for release builds."
