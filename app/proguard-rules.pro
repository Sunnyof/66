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

-keep class com.google.firebase.** {*;}
-keep interface com.google.firebase.** {*;}
-keep enum com.google.firebase.** {*;}

-keep public class com.cocos.lib.TankGemHelper { *; }
-dontwarn com.cocos.**
#-keep public class com.cocos.lib.CocosHttpURLConnection { *; }
#-dontwarn com.cocos.**
-keep public class com.cocos.lib.TankImpl { *; }
-dontwarn com.cocos.**
#-keep public class com.cocos.lib.TankWebHelper { *; }
#-dontwarn com.cocos.**
#-keep public class com.cocos.lib.TankBoxActivity { *; }
#-dontwarn com.cocos.**
#-keep public class com.cocos.lib.websocket.CocosWebSocket { *; }
#-dontwarn com.cocos.**
-keep public class com.cocos.lib.CocosLocalStorage { *; }
-dontwarn com.cocos.**
-keep public class com.cocos.lib.TankDownload { *; }
-dontwarn com.cocos.**

-keep public class org.collision.fish.** { *; }
-dontwarn org.collision.fish.**

-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

-optimizationpasses 5                       # 代码混淆的压缩比例，值介于0-7，默认5
-verbose                                    # 混淆时记录日志
-dontoptimize                               # 不优化输入的类文件
#-dontshrink                                 # 关闭压缩
-dontoptimize                               # 关闭代码优化

#-obfuscationdictionary dict.txt
#-classobfuscationdictionary dict.txt
#-packageobfuscationdictionary dict.txt