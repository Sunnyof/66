package com.game.ad;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.game.BaseApplication;
import com.game.BaseConstant;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import game.crossingthe.greattrench.BuildConfig;

public class GameGoogleAd {
    private static String TAG = GameGoogleAd.class.getSimpleName();
    private static GameGoogleAd gameGoogleAd = new GameGoogleAd();

    private InterstitialAd mInterstitialAd;

    public static GameGoogleAd getInstance() {
        return gameGoogleAd;
    }

    private Activity mContext;

    public void initAdContext(Activity context) {
        this.mContext = context;
        initAdWithContext();
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
        InterstitialAd.load(BaseApplication.getInstance(), getAdUnitId(), new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
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
     *
     */
    public void showAd() {
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
            return BaseConstant.game_ca_beo66_DEBUG_AD_UNIT_ID;
        }
        return BaseConstant.game_ca_beo66_AD_UNIT_ID;
    }
}
