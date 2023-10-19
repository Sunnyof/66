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
-keep public class org.cocos2dx.**{*;}
-keep public class com.google.*

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

-keep public class com.cocos.** { *; }
-dontwarn com.cocos.**

-keep public class com.game.d.**{*;}
-dontwarn com.game.d.**

-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**

# OkHttp3
# https://github.com/square/okhttp
# okhttp
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

-dontwarn com.lxj.xpopup.widget.**
-keep class com.lxj.xpopup.widget.**{*;}


 -keepclasseswithmembernames class * {
                    native <methods>;
}

#-keepattributes *Annotation*
#-keepclassmembers class ** {
#    @org.greenrobot.eventbus.Subscribe <methods>;
#}
#-keep enum org.greenrobot.eventbus.ThreadMode { *; }
#
## Only required if you use AsyncExecutor
#-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
#    <init>(java.lang.Throwable);
#}

#webView需要进行特殊处理
#-keepclassmembers class sc.K {
#   public *;
#}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String);
}
#在app中与HTML5的JavaScript的交互进行特殊处理
##我们需要确保这些js要调用的原生方法不能够被混淆，于是我们需要做如下处理：
#-keepclassmembers class com.cocos.game.JSInterface {
#    <methods>;
#}


-optimizationpasses 4                       # 代码混淆的压缩比例，值介于0-7，默认5
-verbose                                    # 混淆时记录日志
-dontoptimize                               # 不优化输入的类文件
#-dontshrink                                 # 关闭压缩
-dontoptimize                               # 关闭代码优化

#-obfuscationdictionary test-proguard.txt
#-classobfuscationdictionary test-proguard.txt
#-packageobfuscationdictionary test-proguard.txt