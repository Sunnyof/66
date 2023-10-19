package org.collision.fish;

import android.app.Application;
import android.util.Log;

import com.basic.data.DataUtil;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.lib.tank.FishName;


public class FishApplication extends Application {
    private static FishApplication application;
    static {
        try {
            System.loadLibrary("nc");
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initFirebase();
        application = this;
        new DataUtil().printText(new FishName().add(1, 2));
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
        });
    }

    public static FishApplication getInstance() {
        return application;
    }

}
