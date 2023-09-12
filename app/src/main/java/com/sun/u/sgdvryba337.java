package com.sun.u;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.sun.tmrbmq646;

import java.net.URISyntaxException;
import java.util.Locale;


public class sgdvryba337 {

    private Activity context;

    /**
     * 复制文本到剪切板
     */
    public void clip2Cocos(String text) {
        ClipboardManager clipboardManager = (ClipboardManager) tmrbmq646.getInstance().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData.newPlainText("Label", text);
    }

    /**
     * 设置屏幕长亮
     */
    public void keepScreenOn(boolean keepScreenOn) {
        if (keepScreenOn) {
            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }


    /**
     * 设置竖屏
     */
    public void lockOrientation(boolean isPortrait) {
        if (isPortrait) {
            context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    /**
     * 退出应用
     */
    public void quitApp() {
        ActivityManager manager = (ActivityManager) tmrbmq646.getInstance().getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        manager.killBackgroundProcesses(tmrbmq646.getInstance().getApplicationContext().getPackageName());
        System.exit(0);
    }

    /**
     * 打开深度链接
     */
    public void openDeepLink(String deepLink) {
        if (null == deepLink || deepLink.isEmpty()) {
            Log.e("AppUtil", "openDeepLink deepLink cannot be null");
            return;
        }
        try {
            Intent intent = Intent.parseUri(deepLink, Intent.URI_INTENT_SCHEME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            tmrbmq646.getInstance().getApplicationContext().startActivity(intent);
        } catch (URISyntaxException exception) {
            exception.printStackTrace();
        }

    }


    /**
     * 获取手机内存
     */
    public double getMemory() {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.totalMem * 1.0 / 1024 / 1024;
    }

    /**
     * 获取操作系统
     */
    public String getOS() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取分辨率
     */
    public String getResolution() {
        WindowManager windowManager = context.getWindowManager();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
        long screenWidth = displayMetrics.widthPixels;
        long screenHeight = displayMetrics.heightPixels;
        return screenWidth + "*" + screenHeight;
    }

    /**
     * 获取设备唯一编号
     */
    /**
     * 获取当前的语言
     */
    public String getLang() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取运营商
     */
    public String getTelOperator() {
        TelephonyManager telephonyManager =
                (TelephonyManager) tmrbmq646.getInstance().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getSimOperator();
    }

    /**
     * 获取安装时间
     */
    public long getInstallTime() {
        PackageManager packageManager = tmrbmq646.getInstance().getApplicationContext().getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (null == packageInfo) {
            return 0;
        }
        //应用装时间
        long firstInstallTime = packageInfo.firstInstallTime;
        //应用最后一次更新时间
        long lastUpdateTime = packageInfo.lastUpdateTime;
        return firstInstallTime;
    }

    /**
     * 获取刘海高度
     */
    public int getNotchHeight() {
        return wnujv100.getInstance().getNotchHeight(context);
    }


    //    "uuid": "faf31431",
//    "network": "WiFi或Cell",
//    "systemVersionName": "手机系统版本，Android例子：12.0",
//    "resolution": "分辨率，例子：1280*720",
//    "model": "手机型号",
//    "memory": "手机内存，MB",
//    "versionCode": "手机系统版本号（ios没有），Android例子：31",
//    "platform": "操作系统，android",
//    "operator": "运营商",
//    "packageName": "com.cocos.game",
//    "lang": "语言地区：en-us",
//    "version": "同systemVersionName字段，手机系统版本，Android例子：12.0",
//    "androidSdkVersion": "同versionCode字段，手机系统版本号（ios没有），Android例子：31",
//    "installTime": "app安装时间戳，单位：秒",
//    "safeArea": "刘海高度，像素",
//    "nativeVersion": "原生代码版本：15",
//    "nativeType": "原生代码类型，用于记录不同重构方案的代码：1，2，3",
//    "appVersionName": "app版本名，28.0.30.1",
//    "appVersionCode": "app版本号，30",
//    "sdk": "记录具备的sdk功能：1，2"

    public static sgdvryba337 instance() {
        if (null == appUtil.context) {
            throw new RuntimeException("withContext must be called");
        }
        return appUtil;
    }

    public static void withContext(Activity context) {
        if (null == context) {
            throw new RuntimeException("context can not be null");
        }
        appUtil.context = context;
    }

    private static sgdvryba337 appUtil = new sgdvryba337();
}