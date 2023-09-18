package com.cocos.game;

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.cocos.lib.CocosHelper;
import com.cocos.lib.CocosJavascriptJavaBridge;
import com.game.util.LogHelp;

import java.io.IOException;
import java.net.URLEncoder;

public class JSInterface {

    @JavascriptInterface
    public void logEvent(String eventName) {
        Log.i("JSInterface", "Log event: eventName: "+eventName+" params: $params");
    }

    @JavascriptInterface
    public void getSafeArea() {
        Log.i("JSInterface", "getSafeArea");
    }

    @JavascriptInterface
    public void getOrientationChange() {
        Log.i("JSInterface", "getOrientationChange");
    }

    @JavascriptInterface
    public void JsToNative(String str) {
        Log.e("JSInterface", str);
        //str发送到Cocos
        testCocosHelper(str, true);
    }


    private void testCocosHelper(String str, boolean isFlag) {
        Log.i("JSInterface", "flag:" + isFlag);
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



}
