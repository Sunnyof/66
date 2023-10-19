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
-keep public class com.cocos.lib.CocosHelper { *; }
-dontwarn com.cocos.**
#-keep public class com.cocos.lib.CocosHttpURLConnection { *; }
#-dontwarn com.cocos.**
-keep public class com.cocos.lib.CanvasRenderingContext2DImpl { *; }
-dontwarn com.cocos.**
#-keep public class com.cocos.lib.websocket.CocosWebSocket { *; }
#-dontwarn com.cocos.**
-keep public class com.cocos.lib.CocosLocalStorage { *; }
-dontwarn com.cocos.**
#-keep public class com.cocos.lib.CocosDownloader { *; }
-dontwarn com.cocos.**

-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
