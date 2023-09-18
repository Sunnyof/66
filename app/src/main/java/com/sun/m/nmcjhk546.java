package com.sun.m;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sun.u.wovg23;

import com.crossingthe.greattrench.R;

public class nmcjhk546 extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ihnsiu292);
        initFirebase();
    }

    public void initFirebase(){
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
}