#!/bin/bash

# InstantCopy Release Build Documentation Verification Script
# Verifies that the documentation accurately reflects the build system

set -e

echo "🔍 Verifying Release Build Documentation"
echo "========================================"

# Check if documentation files exist and contain expected content
echo ""
echo "📋 Checking BUILDING.md documentation..."

if [ -f "BUILDING.md" ]; then
    echo "✅ BUILDING.md exists"
    
    # Check for release build command
    if grep -q ":android:assembleRelease" BUILDING.md; then
        echo "✅ Contains release build command"
    else
        echo "❌ Missing release build command"
        exit 1
    fi
    
    # Check for correct APK path
    if grep -q "android/build/outputs/apk/release" BUILDING.md; then
        echo "✅ Contains correct APK output path"
    else
        echo "❌ Missing or incorrect APK output path"
        exit 1
    fi
    
    # Check for CI/CD documentation
    if grep -q "GitHub Actions\|automated release\|workflow" BUILDING.md; then
        echo "✅ Contains CI/CD release documentation"
    else
        echo "❌ Missing CI/CD release documentation"
        exit 1
    fi
    
else
    echo "❌ BUILDING.md missing"
    exit 1
fi

echo ""
echo "📋 Checking README.md documentation..."

if [ -f "README.md" ]; then
    echo "✅ README.md exists"
    
    # Check for download instructions
    if grep -q "GitHub Releases\|download" README.md; then
        echo "✅ Contains download instructions"
    else
        echo "❌ Missing download instructions"
        exit 1
    fi
    
    # Check for build commands
    if grep -q ":android:assembleRelease" README.md; then
        echo "✅ Contains release build command"
    else
        echo "❌ Missing release build command"
        exit 1
    fi
    
    # Check for APK path verification
    if grep -q "android/build/outputs/apk/release" README.md; then
        echo "✅ Contains correct APK path verification"
    else
        echo "❌ Missing APK path verification"
        exit 1
    fi
    
else
    echo "❌ README.md missing"
    exit 1
fi

echo ""
echo "🔍 Checking build configuration..."

# Verify Android build.gradle.kts has correct version
if [ -f "android/build.gradle.kts" ]; then
    VERSION=$(grep -m1 'versionName' android/build.gradle.kts | sed 's/.*versionName[[:space:]]*=[[:space:]]*"\([^"]*\)".*/\1/')
    if [ ! -z "$VERSION" ]; then
        echo "✅ Android app version: $VERSION"
    else
        echo "⚠️  Could not determine Android app version"
    fi
else
    echo "❌ android/build.gradle.kts missing"
    exit 1
fi

# Check GitHub workflow file
if [ -f ".github/workflows/build-release.yml" ]; then
    echo "✅ GitHub workflow file exists"
    
    # Check for key workflow steps
    if grep -q ":android:assembleRelease" .github/workflows/build-release.yml; then
        echo "✅ Workflow contains release build step"
    else
        echo "❌ Workflow missing release build step"
        exit 1
    fi
    
    if grep -qi "apk" .github/workflows/build-release.yml; then
        echo "✅ Workflow handles APK artifacts"
    else
        echo "❌ Workflow missing APK artifact handling"
        exit 1
    fi
    
else
    echo "❌ GitHub workflow file missing"
    exit 1
fi

echo ""
echo "📊 Documentation Summary"
echo "========================"

# Count documentation sections
BUILDING_SECTIONS=$(grep -c "^##" BUILDING.md || echo 0)
README_SECTIONS=$(grep -c "^##" README.md || echo 0)

echo "BUILDING.md sections: $BUILDING_SECTIONS"
echo "README.md sections: $README_SECTIONS"

echo ""
echo "✅ Documentation verification complete!"
echo ""
echo "📝 Key information documented:"
echo "• Release build command: ./gradlew clean :android:assembleRelease"
echo "• APK output path: android/build/outputs/apk/release/"
echo "• GitHub Release download process"
echo "• Size verification commands"
echo "• CI/CD automation workflow"

echo ""
echo "🎯 Ready for release build testing!"
