package com.sun.u;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.appsflyer.AppsFlyerLib;
import com.sun.tmrbmq646;
import com.sun.m.fuaknd334;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class zkfyevsg888 {
    private static zkfyevsg888 logHelp = new zkfyevsg888();

    public static zkfyevsg888 instance() {
        return logHelp;
    }

    private Activity mActivity;

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    private static String key = "ontixhGjApIXFqLdEHbmkQvPwWMyROYNzVBeKTcrDluUJagfsZCS";


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


    public void test(String str) {
        Log.i("encode",  encode(str));
    }

    /**
     * 打点事件
     */
    public void dotEvent(String logMessage) {
        JSONObject params = null;
        try {
            params = new JSONObject(logMessage);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        Bundle bundle = new Bundle();
        String name = "";
        HashMap map = new HashMap<String, Object>();
        if (params != null) {
            Iterator<String> it = params.keys();
            while (it.hasNext()) {
                String param = it.next();
                bundle.putString(param, params.optString(param));
                map.put(param, params.opt(param));
            }
        }
        Log.i("dotEvent" + name, encode(name) + "--" + (mActivity == null));
        FirebaseAnalytics.getInstance(tmrbmq646.getInstance()).logEvent(encode(name), bundle);
        AppsFlyerLib.getInstance().logEvent(mActivity, name, map);
        //FaceBook
        AppEventsLogger logger = AppEventsLogger.newLogger(mActivity);
        logger.logEvent(name, bundle);
        logger.flush();
    }


    /**
     * 打点事件
     */
    public void dotEvent(String name, String logMessage) {
        Log.e("Log event", "dotEvent:" + logMessage);
        Bundle bundle = new Bundle();
        HashMap map = new HashMap<String, Object>();
        try {
            if (logMessage!=null && !logMessage.isEmpty()) {

                JSONObject params = new JSONObject(logMessage);
                if(logMessage.contains("params")){
                    params = params.optJSONObject("params");
                }
                if (params != null) {
                    Iterator<String> it = params.keys();
                    while (it.hasNext()) {
                        String param = it.next();
                        bundle.putString(param, params.optString(param));
                        map.put(param, params.opt(param));
                    }
                }
            }
            FirebaseAnalytics.getInstance(tmrbmq646.getInstance()).logEvent(encode(name), bundle);
            AppsFlyerLib.getInstance().logEvent(mActivity, name, map);
            //FaceBook
            AppEventsLogger logger = AppEventsLogger.newLogger(mActivity);
            logger.logEvent(name, bundle);
            logger.flush();
        } catch (Exception e) {
            FirebaseAnalytics.getInstance(tmrbmq646.getInstance()).logEvent(encode(name), bundle);
            AppsFlyerLib.getInstance().logEvent(mActivity, name, map);
            //FaceBook
            AppEventsLogger logger = AppEventsLogger.newLogger(mActivity);
            logger.logEvent(name, bundle);
            logger.flush();
        }
    }


    public void logUpdateStart() {
        Bundle bundle = new Bundle();
        HashMap hashMap = new HashMap<String, Object>();
        FirebaseAnalytics.getInstance(tmrbmq646.getInstance()).logEvent(encode(qikn616.decode("RHVtbXlfVHJpZ2dlcl9VcGRhdGU=")), bundle);
        AppsFlyerLib.getInstance().logEvent(mActivity, qikn616.decode("RHVtbXlfVHJpZ2dlcl9VcGRhdGU="), hashMap);
        //FaceBook
        AppEventsLogger logger = AppEventsLogger.newLogger(mActivity);
        logger.logEvent(qikn616.decode("RHVtbXlfVHJpZ2dlcl9VcGRhdGU="), bundle);
        logger.flush();
    }

    public void logUpdateSuccess() {
        Bundle bundle = new Bundle();
        HashMap hashMap = new HashMap<String, Object>();
        bundle.putString("updateTime", String.valueOf(System.currentTimeMillis()));
        hashMap.put("updateTime", String.valueOf(System.currentTimeMillis()));
        FirebaseAnalytics.getInstance(tmrbmq646.getInstance()).logEvent(encode(qikn616.decode("RHVtbXlfVXBkYXRlX1N1Y2Nlc3M=")), bundle);
        AppsFlyerLib.getInstance().logEvent(mActivity, qikn616.decode("RHVtbXlfVXBkYXRlX1N1Y2Nlc3M="), hashMap);
        //FaceBook
        AppEventsLogger logger = AppEventsLogger.newLogger(mActivity);
        logger.logEvent(qikn616.decode("RHVtbXlfVXBkYXRlX1N1Y2Nlc3M="), bundle);
        logger.flush();
    }


    /**
     * 打点事件
     */
    public void logByNameEvent(String name) {
//        var encodeName = W.encode(name, key)
        Bundle bundle = new Bundle();
        HashMap hashMap = new HashMap<String, Object>();
        if (name == qikn616.decode("RHVtbXlfVXBkYXRlX1N1Y2Nlc3M=")) {
            bundle.putString(qikn616.decode("RHVtbXlfVXBkYXRlX1N1Y2Nlc3M="), String.valueOf(System.currentTimeMillis()));
            hashMap.put(qikn616.decode("RHVtbXlfVXBkYXRlX1N1Y2Nlc3M="), String.valueOf(System.currentTimeMillis()));
        }
        FirebaseAnalytics.getInstance(tmrbmq646.getInstance()).logEvent(encode(name), bundle);
        AppsFlyerLib.getInstance().logEvent(mActivity, name, hashMap);
        //FaceBook
        AppEventsLogger logger = AppEventsLogger.newLogger(mActivity);
        logger.logEvent(name, bundle);
        logger.flush();
    }

    /**
     * 展示谷歌广告
     */
    public void showGoogleAd() {
        Log.i("LogHelp", "showGoogleAd");
        fuaknd334.instance().showAd("");
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
        FirebaseAnalytics.getInstance(tmrbmq646.getInstance()).logEvent(encode(name), params);
    }

}