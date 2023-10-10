package org.cocos;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

public class BaseScene extends Application {

    private static BaseScene application;

    private String TAG = BaseScene.class.getSimpleName();

    private InterstitialAd mInterstitialAd;

    public InterstitialAd getmInterstitialAd() {
        return mInterstitialAd;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        initFirebase();
        initAdWithContext();
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
            // Get new FCM registration token
            String token = task.getResult();
        });
    }

    public static BaseScene getInstance() {
        return application;
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
        InterstitialAd.load(BaseScene.getInstance(), "ca-app-pub-7895073050394223/2612398110", new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
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

}
