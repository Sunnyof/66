package org.cocos.game;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

public class Frog {
    public static void viewAd() {
        Log.e("TAG","viewAd");
        EventBus.getDefault().post("viewAd");
    }
}
