# iOS App Placeholder
# 
# This is a placeholder for the iOS app. In a real KMP project:
# - iOS app is built using Xcode
# - Gradle tasks handle the compilation of the shared framework
# - Xcode project references the built framework from shared/build
#
# To build the iOS app:
# 1. Run `./gradlew :shared:build` to compile the shared framework
# 2. Open iosApp.xcodeproj in Xcode
# 3. Build and run in Xcode
#
# The shared framework will be available at:
# shared/build/cframeworks/Shared.framework

This directory should contain an Xcode project (iosApp.xcodeproj) 
and Swift/Objective-C source files that import and use the shared 
Kotlin Multiplatform framework.