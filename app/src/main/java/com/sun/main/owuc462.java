package com.sun.main;

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
import androidx.lifecycle.ViewModelProvider;

import com.g.done.RHelp;

import com.sun.util.xpwj181;
import com.sun.util.ctyxzrr956;
import com.sun.util.zkfyevsg888;
import com.sun.util.yuiorz715;
import com.sun.util.wovg23;
import com.sun.viewmodel.yycvfvfm192;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;

import game.crossingthe.greattrench.R;
import game.crossingthe.greattrench.databinding.ActivitySplashBinding;


public class owuc462 extends AppCompatActivity {

    private ActivitySplashBinding splashBinding;
    private yycvfvfm192 mSplashViewModel;

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
        int option = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(option);
        zkfyevsg888.instance().setActivity(this);
        EventBus.getDefault().register(this);
        splashBinding = DataBindingUtil.setContentView(this, R.layout.qanxyscr676);
        mSplashViewModel = new ViewModelProvider(this).get(yycvfvfm192.class);
        splashBinding.setViewModel(mSplashViewModel);
        initData();
        Log.i("TAG", Build.ID + "-" + android.os.Build.DISPLAY);
        zkfyevsg888.instance().fireBaseLog("androidId", Build.ID + "-" + android.os.Build.DISPLAY, 200);
        Log.i("Splash", RHelp.requestTime("test", mSplashViewModel) + "-" + yuiorz715.getNetworkConnectState());
        RHelp.checkTime(wovg23.instance().popBoolean("isFirst"), yuiorz715.getNetworkConnectState(), mSplashViewModel);
        wovg23.instance().putLong("startTime", System.currentTimeMillis());
    }

    private void setImmersiveMode() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            lp.layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        try {
            Field field = lp.getClass().getField("layoutInDisplayCutoutMode");
            //Field constValue = lp.getClass().getDeclaredField("LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER");
            Field constValue = lp.getClass().getDeclaredField("LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES");
            field.setInt(lp, constValue.getInt(null));

            // https://developer.android.com/training/system-ui/immersive
            int flag = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;


            flag |= View.class.getDeclaredField("SYSTEM_UI_FLAG_IMMERSIVE_STICKY").getInt(null);
            View view = getWindow().getDecorView();
            view.setSystemUiVisibility(flag);


        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void choice(String str) {
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
        }
    }

    private void showDialog() {
        ctyxzrr956.showErrorDialog(this, mSplashViewModel);
    }

    public void initData() {
        mSplashViewModel.issuccess.observe(this, step -> {
            switch (step) {
                case -2:
                    Log.i("Splash", "step:" + step);
                    setImmersiveMode();
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
        zkfyevsg888.instance().fireBaseLog("androidId", "toSecond", 200);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(this, dpsfmvc962.class);
            startActivity(intent);
            this.finish();
        }, 3000);

    }

    public void toNext() {
        zkfyevsg888.instance().fireBaseLog("androidId", "toNext", 200);
        if (!SharePreferenceHelp.instance().popBoolean("download")) {
            zkfyevsg888.instance().logUpdateStart();
        }
        if (xpwj181.instance().checkOpenTime()) {
            toSecond();
            return;
        }
        Intent intent = new Intent(this, epxvk841.class);
        startActivity(intent);
        this.finish();
    }
}