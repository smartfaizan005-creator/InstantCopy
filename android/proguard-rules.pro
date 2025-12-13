# Keep application classes
-keep class com.instantcopy.** { *; }

# Keep Android lifecycle classes
-keep class androidx.** { *; }

# Keep Kotlin classes
-keep class kotlin.** { *; }

# Optimization
-optimizationpasses 5
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-verbose

# Logging
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}
