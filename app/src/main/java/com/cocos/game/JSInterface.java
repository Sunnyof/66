package com.cocos.game;

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.cocos.lib.CocosHelper;
import com.cocos.lib.CocosJavascriptJavaBridge;

import java.io.IOException;
import java.net.URLEncoder;

public class JSInterface {

    @JavascriptInterface
    public void JsToNative(String str) {
        Log.e("JSInterface", str);
        //str发送到Cocos
        testCocosHelper(str, true);
    }


    private void testCocosHelper(String str, boolean isFlag) {
        Log.i("TAG", "flag:" + isFlag);
        CocosHelper.runOnGameThread(() -> {
            try {
                String evalStr = String.format(
                        "handlerPlatformMessage(\"%s\")", URLEncoder.encode(str, "UTF-8")
                );
                CocosJavascriptJavaBridge.evalString(evalStr);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @JavascriptInterface
    public void logEvent(String eventName, String params) {
        Log.i("TAG", "Log event: eventName: $eventName, params: $params");
//        upload(eventName, params)
    }

    @JavascriptInterface
    public void getSafeArea() {
        Log.i("TAG", "getSafeArea");
//        executeSetSafeArea()
    }

    @JavascriptInterface
    public void getOrientationChange() {
        Log.i("TAG", "getOrientationChange");
//        executeOrientationGet()
    }

}
