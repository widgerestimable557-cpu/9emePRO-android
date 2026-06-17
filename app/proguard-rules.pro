# Regles ProGuard pour 9emePRO
# WebView avec JavaScript - conserver les interfaces
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
-keepattributes JavascriptInterface
