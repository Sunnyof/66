package org.game.frog;

import android.app.Application;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

public class FrogApplication extends Application {
    private static FrogApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        initFirebase();
        application = this;
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
    public static FrogApplication getInstance() {
        return application;
    }

}
