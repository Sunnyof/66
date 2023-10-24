package com.cocos.lib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.colorful.interconnects.BaseApplication;


public class CocosNetworkUtil {

    /**
     * 获取当前网络连接状态
     *
     * @return
     */
    public static boolean getNetworkConnectState() {
        //获取系统的网络服务
        ConnectivityManager connManager = (ConnectivityManager) BaseApplication.application.getSystemService(Context.CONNECTIVITY_SERVICE);
        //如果当前没有网络
        if (null == connManager) return false;
        //获取当前网络类型，如果为空，返回无网络
        @SuppressLint("MissingPermission") NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();
        if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
            return false;
        }
        // 判断是不是连接的是不是wifi
        @SuppressLint("MissingPermission") NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (null != wifiInfo) {
            NetworkInfo.State state = wifiInfo.getState();
            if (null != state)
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    return true;
                }
        }
        // 如果不是wifi，则判断当前连接的是运营商的哪种网络2g、3g、4g等
        @SuppressLint("MissingPermission") NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (null != networkInfo) {
            NetworkInfo.State state = networkInfo.getState();
            if (null != state)
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    return true;
                }
        }
        return false;
    }

    /**
     * 获取当前网络类型
     *
     * @return
     */
    public static String getNetWorkType() {
        //获取系统的网络服务
        ConnectivityManager connManager = (ConnectivityManager) BaseApplication.application.getSystemService(Context.CONNECTIVITY_SERVICE);
        //如果当前没有网络
        if (null == connManager) return "";
        //获取当前网络类型，如果为空，返回无网络
        NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();
        if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
            return "";
        }
        // 判断是不是连接的是不是wifi
        NetworkInfo wifiInfo = connManager.getActiveNetworkInfo();
        if (null != wifiInfo) {
            int type = wifiInfo.getType();
            if (type == connManager.TYPE_WIFI) {
                return "WiFi";
            } else {
                return "Cell";
            }
        }
        return "";
    }
}


