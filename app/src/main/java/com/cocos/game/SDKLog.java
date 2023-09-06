package com.cocos.game;


import android.util.Log;

import com.game.ad.GameGoogleAd;
import com.game.util.LogHelp;
import com.game.viewmodel.JsListener;


public class SDKLog {

    private static JsListener listener;

    private static String TAG = SDKLog.class.getName();

    public static void setListener(JsListener jsListener) {
        listener = jsListener;
    }

    public static void adShow(String str) {
        Log.e("TAG", "showGoodleAd");
        GameGoogleAd.getInstance().showAd();
    }

    public static void adShow() {
        Log.e("TAG", "showGoodleAd");
        GameGoogleAd.getInstance().showAd();
    }

    //    ## SDK功能通讯类`SDKLog`
//    #### 包名：`com.cocos.game`
//    #### 类名：`SDKLog`
//    ### 以下是各个函数名：
//    #### 获取Fcm自定义数据`getFcmCustomData()`：
//    #### 回调：
//    ```json
//    {
//        "class": "FirebaseMessaging",
//        "function": "PushMsg",
//        "args": {
//        "google.delivered_priority": "high",
//        "google.original_priority": "high",
//        "from": "210993828399",
//        "google.message_id": "0:1682326003311381%c2bb9ea4c2bb9ea4",
//        "collapse_key": "com.cocos.GoldenDragon.v",
//        "key01": "value01",
//        "key02": "value02"
//    }
//    }
//    ```
//    <br>
    public static void getFcmCustomData() {
        Log.i(TAG, "getFcmCustomData");
        listener.callFcmCustomDataCallBack();
    }

    //    #### 事件打点`logEvent(String args)`：
//    #### 参数args的json格式：
//    ```json
//    {
//        "event": "name",
//        "params": {
//        "key1":"value1",
//        "key2":"value2",
//    }
//    }
//    ```
    public static void logEvent(String args) {
        Log.i(TAG, "logEvent" + args);
        LogHelp.instance().dotEvent(args);
    }

    //    <br>
//
//    #### 获取更新信息`getUpdateInfo`：
//    #### 回调：
//    ```json
//    {
//        "class": "SDKLog",
//        "funtion": "getUpdateInfo",
//        "args": {
//        "installReferrer": "Non-organic",
//        "updateTimestamp": 1677046527000,
//        "vestId":"610037",
//        "channelId":"30",
//        "domian":"https://www.owqhs.com",
//        "code":"1024"
//    }
//    }
//    ```
//    <br>
    public static void getUpdateInfo() {
        Log.i(TAG, "getUpdateInfo");
        listener.callUpdateInfoCallback();
    }

    //    \#### 获取Fcm token \`getFcmToken\`：（旧的马甲包推送方案）
//            \#### 回调：
//            \`\`\`json
//    {
//&#x20; "class": "SDKLog",
//&#x20; "funtion": "getFcmToken",
//&#x20; "args": {
//&#x20;   "token": "fkapnfdafpdjaihijcijaie48327hhjdj28j387"
//                &#x20; }
//    }
//\`\`\`
//        \<br>
    public static void getFcmToken() {
        listener.callFcmTokenCallBack();
    }

//\#### 推送消息，原生主动发送到Cocos的，不需要调用函数（旧的马甲包推送方案）：
//            \`\`\`json
//    {
//&#x20; "class": "Fcm",
//&#x20; "funtion": "onMessageReceive",
//&#x20; "args": {
//&#x20;   "data": ""
//                &#x20; }
//    }
//\`\`\`

    public static void showAdmob(String tr) {
        Log.i(TAG, "showAdmob:" + tr);
        GameGoogleAd.getInstance().showAd();
    }

    public static void showAdmob() {
        GameGoogleAd.getInstance().showAd();
    }

}