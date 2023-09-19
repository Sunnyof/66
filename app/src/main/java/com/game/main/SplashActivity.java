package com.game.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


import com.game.util.LogHelp;
import com.game.util.SharePreferenceHelp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.Locale;

import crossingthe.greattrench.game.R;
import crossingthe.greattrench.game.databinding.ActivitySplashBinding;


public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding splashBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(lp);
        }
        View decorView = getWindow().getDecorView();
        int option =
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(option);
        LogHelp.instance().setActivity(this);
        EventBus.getDefault().register(this);
        splashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        LogHelp.instance().fireBaseLog("androidId", Build.ID + "-" + android.os.Build.DISPLAY, 200);
        new Handler(Looper.getMainLooper()).postDelayed(() -> toSecond(),1000);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void choice(String str) {
        Log.i("Splash", "choice:" + str);
        switch (str) {
            case "start":
                Log.i("Splash", "start:" + str);
                break;
            case "onDone":
                toNext();
                break;
            case "showDialog":
                showDialog();
                break;
            case "next":
                toSecond();
                break;
            case "what":
                break;
        }
    }

    int i =0;
    private void showDialog() {
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void toSecond() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("screen", "sensorPortrait");
        startActivity(intent);
        this.finish();
    }

    public void toNext() {
        if (!SharePreferenceHelp.instance().popBoolean("download")) {
            LogHelp.instance().logUpdateStart();
        }
        Intent intent = new Intent(this, LandGameActivity.class);
        intent.putExtra("screen", "sensorLandscape");
        startActivity(intent);
        this.finish();
    }
}
