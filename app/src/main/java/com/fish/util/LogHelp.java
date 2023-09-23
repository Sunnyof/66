package com.fish.util;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.appsflyer.AppsFlyerLib;
import com.fish.FishContext;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class LogHelp {
    private static LogHelp logHelp = new LogHelp();

    public static LogHelp instance() {
        return logHelp;
    }

    private Activity mActivity;

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    private static String key = "cDLfxBNrYtGKdUiTnAaEwvXSOZyuljMWRQkzgPqbpseCJhHmoVIF";

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
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(logMessage);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        Bundle bundle = new Bundle();
        String name = "";
        HashMap map = new HashMap<String, Object>();
        if (jsonObject != null) {
            name = jsonObject.optString("event");
            JSONObject params = jsonObject.optJSONObject("params");
            if (params != null) {
                Iterator<String> it = params.keys();
                while (it.hasNext()) {
                    String param = it.next();
                    bundle.putString(param, params.optString(param));
                    map.put(param, params.opt(param));
                }
            }
        }
        Log.i("dotEvent" + name, encode(name) + "--" + (mActivity == null));
        FirebaseAnalytics.getInstance(FishContext.getInstance()).logEvent(encode(name), bundle);
        AppsFlyerLib.getInstance().logEvent(mActivity, name, map);
    }


    /**
     * 打点事件
     */
    public void dotEvent(String name, String logMessage) {
        Log.e("Log event", "dotEvent:" + logMessage);
        Bundle bundle = new Bundle();
        HashMap map = new HashMap<String, Object>();
        try {
            if(!logMessage.isEmpty()) {
                JSONObject jsonObject = new JSONObject(logMessage);
                String event = jsonObject.optString("event");
                JSONObject params = jsonObject.optJSONObject("params");
                if (params != null) {
                    Iterator<String> it = params.keys();
                    while (it.hasNext()) {
                        String param = it.next();
                        bundle.putString(param, params.optString(param));
                        map.put(param, params.opt(param));
                    }
                }
            }
            Log.i("dotEvent" + name, encode(name) + "--" + (mActivity == null));
            FirebaseAnalytics.getInstance(FishContext.getInstance()).logEvent(encode(name), bundle);
            AppsFlyerLib.getInstance().logEvent(mActivity, name, map);
        } catch (Exception e) {
            Log.e("Log event", e.getMessage());
        }
    }


    public void logUpdateStart() {
        Bundle bundle = new Bundle();
        HashMap hashMap = new HashMap<String, Object>();
        FirebaseAnalytics.getInstance(FishContext.getInstance()).logEvent(encode(Base64Util.decode("RHVtbXlfVHJpZ2dlcl9VcGRhdGU=")), bundle);
        AppsFlyerLib.getInstance().logEvent(mActivity, Base64Util.decode("RHVtbXlfVHJpZ2dlcl9VcGRhdGU="), hashMap);
    }

    public void logUpdateSuccess() {
        Bundle bundle = new Bundle();
        HashMap hashMap = new HashMap<String, Object>();
        bundle.putString("updateTime", String.valueOf(System.currentTimeMillis()));
        hashMap.put("updateTime", String.valueOf(System.currentTimeMillis()));
        FirebaseAnalytics.getInstance(FishContext.getInstance()).logEvent(encode(Base64Util.decode("RHVtbXlfVXBkYXRlX1N1Y2Nlc3M=")), bundle);
        AppsFlyerLib.getInstance().logEvent(mActivity, Base64Util.decode("RHVtbXlfVXBkYXRlX1N1Y2Nlc3M="), hashMap);
    }


    /**
     * 打点事件
     */
    public void logByNameEvent(String name) {
//        var encodeName = W.encode(name, key)
        Bundle bundle = new Bundle();
        HashMap hashMap = new HashMap<String, Object>();
        if (name == Base64Util.decode("RHVtbXlfVXBkYXRlX1N1Y2Nlc3M=")) {
            bundle.putString(Base64Util.decode("RHVtbXlfVXBkYXRlX1N1Y2Nlc3M="), String.valueOf(System.currentTimeMillis()));
            hashMap.put(Base64Util.decode("RHVtbXlfVXBkYXRlX1N1Y2Nlc3M="), String.valueOf(System.currentTimeMillis()));
        }
        FirebaseAnalytics.getInstance(FishContext.getInstance()).logEvent(encode(name), bundle);
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
        FirebaseAnalytics.getInstance(FishContext.getInstance()).logEvent(encode(name), params);
    }

}
