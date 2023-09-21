package com.cocos.game;


import android.util.Log;

import com.game.ad.GameGoogleAd;
import com.game.util.LogHelp;
import com.game.viewmodel.JsListener;


public class SDKLog {

    private static JsListener listener;

    private static String TAG = SDKLog.class.getName();

    public static void setListener(JsListener jsListener) {
        listener = jsListener;
    }

    public static void adShow(String str) {
        Log.e("TAG", "showGoodleAd");
        GameGoogleAd.getInstance().showAd();
    }

    public static void adShow() {
        Log.e("TAG", "showGoodleAd");
        GameGoogleAd.getInstance().showAd();
    }

    public static void getFcmCustomData() {
        Log.i(TAG, "getFcmCustomData");
        listener.callFcmCustomDataCallBack();
    }


    public static void logEvent(String args) {
        Log.i(TAG, "logEvent" + args);
        LogHelp.instance().dotEvent(args);
    }


    public static void getUpdateInfo() {
        Log.i(TAG, "getUpdateInfo");
        listener.callUpdateInfoCallback();
    }

    public static void getFcmToken() {
        listener.callFcmTokenCallBack();
    }


    public static void showAd(String tr) {
        Log.i(TAG, "showAdmob:" + tr);
        GameGoogleAd.getInstance().showAd();
    }

    public static void showAd() {
        Log.i(TAG, "showAdmob:2");
        GameGoogleAd.getInstance().showAd();
    }

}