# Consumer ProGuard rules for the Text Selection Service library

-keep public class com.example.textselection.** {
    public protected <methods>;
}

-keep class com.example.textselection.service.TextSelectionAccessibilityService {
    public <init>();
    public <methods>;
}

-keep class com.example.textselection.receiver.BootCompletedReceiver {
    public <init>();
    public <methods>;
}
