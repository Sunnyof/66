package com.sun.u;

import android.util.Log;

import game.crossingthe.greattrench.BuildConfig;


public class qtefi494 {
    private static String TAG = "CocosGame";

    public static void logI(String message) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, message);
        }
    }

    public static void logI(String key,String message){
        if (BuildConfig.DEBUG) {
            Log.i(key, message);
        }
    }

    public static void logE(String message) {
        Log.e(TAG, message);
    }
}