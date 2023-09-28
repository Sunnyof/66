package org.tank.gem;

import android.app.Application;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.lib.tank.TankHelper;


public class TankGem extends Application {
    private static TankGem application;

    @Override
    public void onCreate() {
        super.onCreate();
        TankHelper.add(1, 2);
        initFirebase("str");
        application = this;
    }

    private void initFirebase(String str) {
        FirebaseApp.initializeApp(this);
        //Firebase 获取token
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.i("TAG", "Fetching FCM FirebaseMessaging token failed:" + task.getException());
                return;
            }
            Log.i("TAG", "Fetching FCM FirebaseMessaging token:" + task.getResult() + str);
        });
    }

    public static TankGem getInstance() {
        return application;
    }

}
