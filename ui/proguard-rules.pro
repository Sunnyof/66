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
-keep public class com.cocos.lib.CocosHttpURLConnection { *; }
-dontwarn com.cocos.**
-keep public class com.cocos.lib.CanvasRenderingContext2DImpl { *; }
-dontwarn com.cocos.**
-keep public class com.cocos.lib.websocket.CocosWebSocket { *; }
#-dontwarn com.cocos.**
-keep public class com.cocos.lib.CocosLocalStorage { *; }
-dontwarn com.cocos.**
-keep public class com.cocos.lib.CocosDownloader { *; }
-dontwarn com.cocos.**
-keep public class com.cocos.lib.CocosEditBoxActivity { *; }
-dontwarn com.cocos.**
-keep public class com.cocos.game.** { *; }
-dontwarn com.cocos.**

-dontwarn com.google.ads.**
-keep public class com.google.ads.**{
	public protected *;
}

-dontwarn com.google.gms.**
-keep public class com.google.gms.**{
	public protected *;
}

-keep class com.appsflyer.** { *; }
-keep public class com.android.installreferrer.** { *; }

-keep class com.google.firebase.** {*;}
-keep interface com.google.firebase.** {*;}
-keep enum com.google.firebase.** {*;}

-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**

# OkHttp3
# https://github.com/square/okhttp
# okhttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.* { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**

# okhttp 3
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

# Okio
-dontwarn com.squareup.**
-dontwarn okio.**
-keep public class org.codehaus.* { *; }
-keep public class java.nio.* { *; }

-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
# Most of volatile fields are updated with AFU and should not be mangled
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}
-keepattributes Signature
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.**{*; }

-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
}
# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String);
}
#在app中与HTML5的JavaScript的交互进行特殊处理
#我们需要确保这些js要调用的原生方法不能够被混淆，于是我们需要做如下处理：
-keepclassmembers class com.cocos.game.JSInterface {
    <methods>;
}
