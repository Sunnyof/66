package com.cocos.lib;


import android.util.Log;

import org.greenrobot.eventbus.EventBus;

public class SDKLog {
    private static String TAG = SDKLog.class.getName();
    public static void admobView() {
        Log.e(TAG, "admobView");
        EventBus.getDefault().post("adShow");
    }


}