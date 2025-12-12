# Accessibility Service ProGuard rules
-keep class com.example.textselection.service.TextSelectionAccessibilityService {
    public <methods>;
}

-keep class com.example.textselection.receiver.BootCompletedReceiver {
    public <methods>;
}

-keep class com.example.textselection.settings.PreferencesManager {
    public <methods>;
}

-keep class com.example.textselection.clipboard.ClipboardManager {
    public <methods>;
}

# Keep all Android framework classes
-dontwarn android.**
-keep public class android.** { public protected *; }

# Keep AndroidX classes
-dontwarn androidx.**
-keep public class androidx.** { public protected *; }

# Keep Kotlin classes
-dontwarn kotlin.**
-keep public class kotlin.** { public protected *; }

# Keep enum classes
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
