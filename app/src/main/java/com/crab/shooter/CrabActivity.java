/****************************************************************************
 Copyright (c) 2015-2016 Chukong Technologies Inc.
 Copyright (c) 2017-2018 Xiamen Yaji Software Co., Ltd.

 http://www.cocos2d-x.org

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR O PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 ****************************************************************************/
package com.crab.shooter;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.crab.CrabApplication;
import com.cocos.lib.CocosActivity;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


import java.util.ArrayList;
import java.util.Collections;

import undersea.defense.crab.BuildConfig;


public class CrabActivity extends CocosActivity {
    private InterstitialAd mInterstitialAd;

    private String TAG = "_LandGameActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAdWithContext();
        CrabHelp.instance().setActivity(this);
        EventBus.getDefault().register(this);
        Log.i(TAG,genKey());
    }

    public static String genKey(){
        String key = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        ArrayList<String> keyList = new ArrayList<>();
        for (int i=0;i<key.length();i++){
            keyList.add(String.valueOf(key.charAt(i)));
        }
        Collections.shuffle(keyList);
        StringBuilder result = new StringBuilder();
        for (String i:keyList){
            result.append(i);
        }
        return result.toString();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("GAME", "onDestroy");
        EventBus.getDefault().unregister(this);
        if (!isTaskRoot()) {
            return;
        }
    }


    private void test() {
        CrabHelp.instance().fireBaseLog("test", "activity", 300);
        if (BuildConfig.DEBUG) {
            Log.i("TAG", "debug");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void choice(String str) {
        Log.i("Splash", "choice:" + str);
        switch (str) {
            case "adShow":
                showGoogleAd();
                break;
            case "test":
                CrabHelp.instance().fireBaseLog("test", "application", 300);
                break;
        }
    }

    private FullScreenContentCallback mCallBack = new FullScreenContentCallback() {
        @Override
        public void onAdClicked() {
            super.onAdClicked();
            Log.i(TAG, "onAdClicked");
        }

        @Override
        public void onAdDismissedFullScreenContent() {
            super.onAdDismissedFullScreenContent();
            Log.i(TAG, "onAdDismissedFullScreenContent");
            mInterstitialAd = null;
            initAdWithContext();
        }

        @Override
        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
            super.onAdFailedToShowFullScreenContent(adError);
            Log.i(TAG, "onAdFailedToShowFullScreenContent");
        }

        @Override
        public void onAdImpression() {
            super.onAdImpression();
            Log.i(TAG, "onAdImpression");
        }

        @Override
        public void onAdShowedFullScreenContent() {
            super.onAdShowedFullScreenContent();
            Log.i(TAG, "onAdShowedFullScreenContent");
        }
    };

    /**
     * 初始化Google广告
     */
    private void initAdWithContext() {
        InterstitialAd.load(CrabApplication.getInstance(), getAdUnitId(), new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.i(TAG, "onAdFailedToLoad" + loadAdError.getMessage());
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                Log.i(TAG, "onAdLoaded" + interstitialAd.getAdUnitId());
                mInterstitialAd = interstitialAd;
                interstitialAd.setFullScreenContentCallback(mCallBack);
            }
        });
    }

    /**
     * 展示广告
     */
    public void showGoogleAd() {
        runOnUiThread(() -> {
            if (null != mInterstitialAd) {
                mInterstitialAd.show(this);
            }
        });
    }

    /**
     * 获取广告ID
     *
     * @return
     */
    private String getAdUnitId() {
        if (BuildConfig.DEBUG) {
            return "ca-app-pub-5469681886103176/6003563381";
        }
        return "ca-app-pub-5469681886103176/6003563381";
    }
}
