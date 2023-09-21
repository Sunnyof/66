package com.game;

import android.app.Application;

import com.game.ad.GameGoogleAd;

public class GoogleAdApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initThirdSdk();
    }

    private void initThirdSdk(){
        GameGoogleAd.getInstance().initAdContext(null);
    }
}
