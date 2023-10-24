package com.cocos.game;

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.cocos.lib.CocosHelper;

public class JSInterface {

    @JavascriptInterface
    public void JsToNative(String str) {
        Log.e("JSInterface",str);
        CocosHelper.cocos2Java(str);
        //str发送到Cocos
//        CocosHelper.runOnGameThread(() -> {
//            try {
//                String evalStr = String.format(
//                        "handlerPlatformMessage(\"%s\")", URLEncoder.encode(str, "UTF-8")
//                );
//                CocosJavascriptJavaBridge.evalString(evalStr);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
    }

}
