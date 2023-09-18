package com.sun.m;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.sun.tmrbmq646;
import com.sun.mdhaat979;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import com.crossingthe.greattrench.BuildConfig;

public class fuaknd334 {
    private static String TAG = fuaknd334.class.getSimpleName();
    private static fuaknd334 gameGoogleAd;

    private InterstitialAd mInterstitialAd;

    private fuaknd334() {
    }

    public static fuaknd334 instance() {
        if (gameGoogleAd == null) {
            gameGoogleAd = new fuaknd334();
        }
        return gameGoogleAd;
    }

    private Activity mContext;

    public void withApplication(Activity context) {
        this.mContext = context;
        initWithActivity();
    }

    /**
     * 广告全屏回调
     */
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
            initWithActivity();
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
    private void initWithActivity() {
        InterstitialAd.load(tmrbmq646.getInstance(), getAdUnitId(), new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
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
    public void showAd(String name) {
        mContext.runOnUiThread(() -> {
            if (null != mInterstitialAd) {
                mInterstitialAd.show(mContext);
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
            return mdhaat979.game_ca_beo66_DEBUG_AD_UNIT_ID;
        }
        return mdhaat979.game_ca_beo66_AD_UNIT_ID;
    }
}