package org.cocos.game;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import androidx.annotation.Nullable;

import com.cocos.lib.CocosActivity;

import org.cocos.BaseScene;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SplashView extends CocosActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        logEvent("onCreate");
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void choice(AdMobEvent adMobEvent) {
        logEvent("adMobEvent");
        if (adMobEvent.isShow()) {
            try {
                runOnUiThread(() -> {
                    if (null != BaseScene.getInstance().getmInterstitialAd())
                        BaseScene.getInstance().getmInterstitialAd().show(this);
                });
            } catch (Exception e) {
                Log.i("SplashView", e.getMessage());
            }
        } else if (!(adMobEvent.isShow())) {
            Intent intent = new Intent(this, WebView.class);
            intent.putExtra("url", "https://www.google.com");
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logEvent("onDestroy");
        EventBus.getDefault().unregister(this);
    }

    private void logEvent(String name) {
        Bundle bundle = new Bundle();
        try {
            bundle.putString("androidId", Build.ID);
        } catch (Exception e) {

        }
        FirebaseAnalytics.getInstance(this).logEvent(name, bundle);
    }

}
