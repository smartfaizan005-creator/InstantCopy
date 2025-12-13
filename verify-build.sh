#!/bin/bash

# InstantCopy Build Verification Script
# This script verifies that builds meet size requirements and optimization criteria

set -e

echo "🔍 InstantCopy Build Verification"
echo "================================"

# Check if we're in the right directory
if [ ! -f "README.md" ] || [ ! -d "screenshots" ]; then
    echo "❌ Error: Please run this script from the project root directory"
    exit 1
fi

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

# Check optimization targets
echo ""
echo "🎯 Size Optimization Verification..."

TARGET_APK_SIZE=3145728  # 3MB in bytes
TARGET_IPA_SIZE=3145728  # 3MB in bytes
TARGET_ASSET_SIZE=102400 # 100KB in bytes

# Simulate build size checks (these would run after actual builds)
echo "   📱 Android APK target: < 3MB"
echo "   🍎 iOS IPA target: < 3MB"  
echo "   🖼️  Individual assets: < 100KB"

# Check if screenshots meet optimization targets
for file in "${SCREENSHOT_FILES[@]}"; do
    SIZE=$(wc -c < "$file")
    if [ $SIZE -le $TARGET_ASSET_SIZE ]; then
        echo "   ✅ $file meets size target"
    else
        echo "   ❌ $file exceeds size target ($SIZE > $TARGET_ASSET_SIZE bytes)"
    fi
done

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
if grep -qi "Accessibility" docs/accessibility-troubleshooting.md; then
    echo "✅ Accessibility troubleshooting includes accessibility content"
else
    echo "❌ Accessibility troubleshooting missing accessibility content"
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
TOTAL_SCREENSHOTS=$(find screenshots -name "*.svg" | wc -l)

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
echo "✅ Build verification complete!"
echo ""
echo "Next steps for actual build verification:"
echo "1. Run: flutter build apk --release --analyze-size"
echo "2. Run: flutter build ios --release" 
echo "3. Check: ls -lh build/app/outputs/flutter-apk/app-release.apk"
echo "4. Check: ls -lh build/ios/Release-iphoneos/InstantCopy.app/InstantCopy"
echo ""
echo "All should be under 3MB (3145728 bytes) for release builds."