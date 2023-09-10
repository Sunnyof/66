package com.cocos.game;

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.sun.util.zkfyevsg888;

public class GameJScript {

    @JavascriptInterface
    public void logEvent(String eventName, String params) {
        Log.i("JSInterface", "Log event: eventName: " + eventName + " params: $params");
        zkfyevsg888.instance().dotEvent(eventName, params);
    }

    @JavascriptInterface
    public void getSafeArea() {
        Log.i("JSInterface", "getSafeArea");
//        executeSetSafeArea()
    }

    @JavascriptInterface
    public void getOrientationChange() {
        Log.i("JSInterface", "getOrientationChange");
//        executeOrientationGet()
    }

}