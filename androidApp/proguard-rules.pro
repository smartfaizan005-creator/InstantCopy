# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Keep KMP shared classes
-keep class com.kmpbootstrap.shared.** { *; }
-keep class * implements com.kmpbootstrap.shared.SettingsRepository { *; }
-keep class * implements com.kmpbootstrap.shared.ClipboardRepository { *; }
-keep class * implements com.kmpbootstrap.shared.PlatformServiceRepository { *; }

# Keep data classes
-keep class com.kmpbootstrap.shared.SettingsState { *; }
-keep class com.kmpbootstrap.shared.ClipboardContent { *; }
-keep class com.kmpbootstrap.shared.SystemInfo { *; }

# Keep enum classes
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep sealed classes
-keep class com.kmpbootstrap.shared.PlatformAction { *; }
-keep class com.kmpbootstrap.shared.PlatformResult { *; }

# Keep use cases
-keep class com.kmpbootstrap.shared.SettingsUseCase { *; }
-keep class com.kmpbootstrap.shared.ClipboardUseCase { *; }
-keep class com.kmpbootstrap.shared.PlatformServiceUseCase { *; }

# Android specific
-keep public class * extends android.app.Application
-keep public class * extends android.app.Activity
-keep public class * extends androidx.fragment.app.Fragment

# Kotlin
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn kotlin.Unit

# Remove logging in release builds
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}