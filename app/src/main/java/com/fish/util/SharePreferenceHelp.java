package com.fish.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceHelp {
    private static SharePreferenceHelp sharePreferenceHelp = new SharePreferenceHelp();
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private SharePreferenceHelp() {

    }

    public void init(Context context) {
        mSharedPreferences = context.getSharedPreferences("game", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public static SharePreferenceHelp instance() {
        return sharePreferenceHelp;
    }

    public void pushString(String key, String message) {
        if (null == mEditor) {
            return;
        }
        mEditor.putString(key, message);
        mEditor.apply();
    }

    public String popString(String key) {
        if (null == mSharedPreferences) {
            return "";
        }
        return mSharedPreferences.getString(key, "");
    }


    public void putInt(String key, int value) {
        if (null == mEditor) {
            return;
        }
        mEditor.putInt(key, value);
        mEditor.apply();
    }

    public int popInt(String key) {
        if (null == mSharedPreferences) {
            return -1;
        }
        return mSharedPreferences.getInt(key, -1);
    }

    public void putBoolean(String key, boolean flag) {
        if (null == mEditor) {
            return;
        }
        mEditor.putBoolean(key, flag);
        mEditor.apply();
    }

    public boolean popBoolean(String key) {
        if (null == mSharedPreferences) {
            return false;
        }
        return mSharedPreferences.getBoolean(key, false);
    }


}
