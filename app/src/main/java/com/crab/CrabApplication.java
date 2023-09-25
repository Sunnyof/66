package com.crab;

import android.app.Application;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import org.greenrobot.eventbus.EventBus;

public class CrabApplication extends Application {

    private static CrabApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        test();
        initFirebase();
    }


    public void test(){
        Log.i("TAG","application");
        EventBus.getDefault().postSticky("test");
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
        });
    }

    public static CrabApplication getInstance() {
        return application;
    }

}
