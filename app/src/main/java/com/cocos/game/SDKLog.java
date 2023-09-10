package com.cocos.game;


import android.util.Log;
import com.game.ad.GameGoogleAd;

public class SDKLog {
    private static String TAG = SDKLog.class.getName();

    public static void showAd(String tr) {
        Log.i(TAG, "showAdmob:" + tr);
        GameGoogleAd.instance().showAd("showAdmob");
    }

    public static void showAd() {
        Log.i(TAG, "showAdmob:2");
        GameGoogleAd.instance().showAd("showAdmob");
    }

}