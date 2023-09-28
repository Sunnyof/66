package org.tank.gem;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

public class TankAd {
    public static void tankAd(String string) {
        EventBus.getDefault().post("tankAd");
    }

    public static void tankAd() {
        EventBus.getDefault().post("tankAd");
    }

    public static void tankAd(int string) {
        EventBus.getDefault().post("tankAd");
    }

    public static void tankAd(byte string) {
        EventBus.getDefault().post("tankAd");
    }
}
