package com.fish;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.fish.service.GameGoogleService;
import com.fish.util.LogHelp;
import com.fish.util.SharePreferenceHelp;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import java.util.Map;

public class FishContext extends Application {

    private static FishContext application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        initFirebase();
        initAppsFlyer();
        initThirdSdk();
    }

    private void initThirdSdk(){
        GameGoogleService.getInstance().initAdWithContext();
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
            SharePreferenceHelp.instance().pushString("fcmToken", token);
        });
    }

    private void initAppsFlyer() {
        AppsFlyerLib.getInstance().init("CTFnECiUuRRfra6G7uMo2T", new AppsFlyerConversionListener() {
            @Override
            public void onConversionDataSuccess(Map<String, Object> map) {
                String status = (String) map.get("af_status");
                Log.i("initAppsFlyer", "onConversionDataSuccess" + status);
                SharePreferenceHelp.instance().pushString("installReferrer", status);
            }

            @Override
            public void onConversionDataFail(String s) {
                Log.i("initAppsFlyer", "onConversionDataFail" + s);
                Bundle params = new Bundle();
                params.putString("msg", s);
                LogHelp.instance().fireBaseLog("non_organic_check_fail", s, 400);
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
        AppsFlyerLib.getInstance().setDebugLog(true);
        LogHelp.instance().fireBaseLog("non_organic_check", null, 200);
    }

    public static FishContext getInstance() {
        return application;
    }

}
