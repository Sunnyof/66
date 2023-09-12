package com.cocos.game;


import android.util.Log;
import com.sun.m.fuaknd334;

public class SDKLog {
    private static String TAG = SDKLog.class.getName();

    public static void showAd(String tr) {
        Log.i(TAG, "showAdmob:" + tr);
        fuaknd334.instance().showAd("showAdmob");
    }

    public static void showAd() {
        Log.i(TAG, "showAdmob:2");
        fuaknd334.instance().showAd("showAdmob");
    }

}