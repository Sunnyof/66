package org.colorful.interconnects.value;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.appsflyer.AppsFlyerLib;
import com.cocos.lib.CocosBase64Util;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.colorful.interconnects.BaseApplication;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class EventHelp {
    private static EventHelp logHelp = new EventHelp();

    public static EventHelp instance() {
        return logHelp;
    }

    private Activity mActivity;

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    private static String key = "wvNIrgFMjuVpKDbiaTtAcxOlkHhBPWmXGqEULfCQoyRdznYsSZJe";


//    fun encode(text: String, key: String): String {
//        val stringBuilder = StringBuilder()
//        for (String element : text) {
//            int index = key.indexOf(element);
//            if (index != -1) {
//                index++;
//                if (index >= key.length()) {
//                    index = 0;
//                }
//                stringBuilder.append(key[index]);
//            } else {
//                stringBuilder.append(element);
//            }
//        }
//        return stringBuilder.toString();
//    }

    private String encode(String text) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char element = text.charAt(i);
            int index = key.indexOf(element);
            if (index != -1) {
                index++;
                if (index >= key.length()) {
                    index = 0;
                }
                stringBuilder.append(key.charAt(index));
            } else {
                stringBuilder.append(element);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 打点事件
     */
    public void dotEvent(String logMessage) {
        try {
            JSONObject jsonObject = new JSONObject(logMessage);
            String name = jsonObject.optString("event");
            JSONObject params = jsonObject.optJSONObject("params");
            Bundle bundle = new Bundle();
            HashMap map = new HashMap<String, Object>();
            if (params != null) {
                Iterator<String> it = params.keys();
                while (it.hasNext()) {
                    String param = it.next();
                    bundle.putString(param, params.optString(param));
                    map.put(param, params.opt(param));
                }
            }
            Log.i("dotEvent" + name, encode(name)+"--"+(mActivity == null));
            FirebaseAnalytics.getInstance(BaseApplication.application).logEvent(encode(name), bundle);
            AppsFlyerLib.getInstance().logEvent(mActivity, name, map);

        } catch (Exception e) {

        }
    }


    public void logUpdateStart() {
        Bundle bundle = new Bundle();
        HashMap hashMap = new HashMap<String, Object>();
        FirebaseAnalytics.getInstance(BaseApplication.application).logEvent(encode(CocosBase64Util.decode("RHVtbXlfVHJpZ2dlcl9VcGRhdGU=")), bundle);
        AppsFlyerLib.getInstance().logEvent(mActivity, CocosBase64Util.decode("RHVtbXlfVHJpZ2dlcl9VcGRhdGU="), hashMap);
    }

    public void logUpdateSuccess() {
        Bundle bundle = new Bundle();
        HashMap hashMap = new HashMap<String, Object>();
        bundle.putString("updateTime", String.valueOf(System.currentTimeMillis()));
        hashMap.put("updateTime", String.valueOf(System.currentTimeMillis()));
        FirebaseAnalytics.getInstance(BaseApplication.application).logEvent(encode(CocosBase64Util.decode("RHVtbXlfVXBkYXRlX1N1Y2Nlc3M=")), bundle);
        AppsFlyerLib.getInstance().logEvent(mActivity, CocosBase64Util.decode("RHVtbXlfVXBkYXRlX1N1Y2Nlc3M="), hashMap);

    }


    /**
     * 打点事件
     */
    public void logByNameEvent(String name) {
//        var encodeName = W.encode(name, key)
        Bundle bundle = new Bundle();
        HashMap hashMap = new HashMap<String, Object>();
        if (name == CocosBase64Util.decode("RHVtbXlfVXBkYXRlX1N1Y2Nlc3M=")) {
            bundle.putString(CocosBase64Util.decode("RHVtbXlfVXBkYXRlX1N1Y2Nlc3M="), String.valueOf(System.currentTimeMillis()));
            hashMap.put(CocosBase64Util.decode("RHVtbXlfVXBkYXRlX1N1Y2Nlc3M="), String.valueOf(System.currentTimeMillis()));
        }
        FirebaseAnalytics.getInstance(BaseApplication.application).logEvent(encode(name), bundle);
        AppsFlyerLib.getInstance().logEvent(mActivity, name, hashMap);
    }



    /**
     * 发送firebase日志
     */
    public void fireBaseLog(String name, String message, int code) {
        Bundle params = new Bundle();
        if (null != message && !message.isEmpty()) {
            params.putString("code", String.valueOf(code));
            params.putString("msg", message);
        }
        FirebaseAnalytics.getInstance(BaseApplication.application).logEvent(encode(name), params);
    }

}
