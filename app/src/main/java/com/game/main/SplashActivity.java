package com.game.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.lifecycle.ViewModelProvider;

import com.game.d.BaseActivity;
import com.game.d.RequestHelp;

import com.game.util.DialogUtil;
import com.game.util.LogHelp;
import com.game.util.NetworkUtil;
import com.game.util.SharePreferenceHelp;
import com.game.viewmodel.SplashViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.util.Locale;

import game.crossingthe.greattrench.R;
import game.crossingthe.greattrench.databinding.ActivitySplashBinding;


public class SplashActivity extends BaseActivity {

    private ActivitySplashBinding splashBinding;
    private SplashViewModel mSplashViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setTheme(com.cocos.lib.R.style.Theme_AppCompat_Light_NoActionBar);
        Window _window = getWindow();
        WindowManager.LayoutParams params = _window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        _window.setAttributes(params);
        super.onCreate(savedInstanceState);
//        setImmersiveMode();
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
        mSplashViewModel = new ViewModelProvider(this).get(SplashViewModel.class);
        splashBinding.setViewModel(mSplashViewModel);
        initData();
        Log.i("TAG", Locale.getDefault().toLanguageTag());
        LogHelp.instance().fireBaseLog("androidId", Build.ID + "-" + android.os.Build.DISPLAY, 200);
        Log.i("Splash", RequestHelp.requestTime("test", mSplashViewModel) + "-" + NetworkUtil.getNetworkConnectState());
        RequestHelp.checkTime(SharePreferenceHelp.instance().popBoolean("isFirst"), NetworkUtil.getNetworkConnectState(), mSplashViewModel);

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

    private void showDialog() {
        DialogUtil.showErrorDialog(this, mSplashViewModel);
    }

    public void initData() {
        mSplashViewModel.issuccess.observe(this, step -> {
            switch (step) {
                case -2:
                    Log.i("Splash", "step:" + step);
                    break;
                case 0:
                    Log.i("Splash", "step1:" + step);
                    break;
                case 20:
                    Log.i("Splash", "step2:" + step);
                    break;
                case 3:
                    Log.i("Splash", "step3:" + step);
                    break;
                case 9999:
                    Log.i("Splash", "step9999:" + step);
                    toNext();
                    break;
                case -9999:
                    Log.i("Splash", "step-9999:" + step);
                    toSecond();
                    break;
            }
        });
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

    @Override
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
