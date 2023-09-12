package com.sun;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.sun.m.fuaknd334;
import com.sun.u.zkfyevsg888;
import com.sun.u.wovg23;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

public class tmrbmq646 extends Application {

    private static tmrbmq646 application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        initFacebook();
        initThirdSdk();
        initFirebase();
        initAppsFlyer();
        wovg23.instance().init(this);
    }

    private void initThirdSdk(){
        fuaknd334.instance().withApplication(null);
    }

    private void initFacebook(){
        FacebookSdk.setAutoLogAppEventsEnabled(true);
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        //Firebase 获取token
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.i("TAG", "Fetching FCM FirebaseMessaging token failed:" + task.getException());
                return;
            }
            Log.i("TAG", "Fetching FCM FirebaseMessaging token:" + task.getResult());
            // Get new FCM registration token
            String token = task.getResult();
            wovg23.instance().pushString("fcmToken", token);
        });
    }

    private void initAppsFlyer() {
        AppsFlyerLib.getInstance().init(mdhaat979.game_ca_beo66_TEST_AF_KEY, new AppsFlyerConversionListener() {
            @Override
            public void onConversionDataSuccess(Map<String, Object> map) {
                String status = (String) map.get("af_status");
                Log.i("initAppsFlyer", "onConversionDataSuccess" + status);
                if (status == "Non-organic") {
                    EventBus.getDefault().post("update");
                }
                wovg23.instance().pushString("installReferrer", status);
            }

            @Override
            public void onConversionDataFail(String s) {
                Log.i("initAppsFlyer", "onConversionDataFail" + s);
                Bundle params = new Bundle();
                params.putString("msg", s);
                zkfyevsg888.instance().fireBaseLog("non_organic_check_fail", s, 400);
            }

            @Override
            public void onAppOpenAttribution(Map<String, String> map) {
                Log.i("initAppsFlyer", "onAppOpenAttribution" + map.toString());
            }

            @Override
            public void onAttributionFailure(String s) {
                Log.i("initAppsFlyer", "onAttributionFailure:" + s);
            }
        }, this);
        AppsFlyerLib.getInstance().start(this);
        AppEventsLogger.activateApp(this);
        AppsFlyerLib.getInstance().setDebugLog(true);
        zkfyevsg888.instance().fireBaseLog("non_organic_check", null, 200);
    }

    public static tmrbmq646 getInstance() {
        return application;
    }

}