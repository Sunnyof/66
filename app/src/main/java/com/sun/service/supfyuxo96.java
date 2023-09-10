package com.sun.service;

import androidx.annotation.NonNull;

import com.sun.util.dzovvg912;
import com.sun.util.wovg23;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;

public class supfyuxo96 extends FirebaseMessagingService {
    private HashMap fcmMap = new HashMap<String, Object>();

    public void onMessageReceived(RemoteMessage message) {
        super.onMessageReceived(message);
        if (message.getData() != null) {
            fcmMap.putAll(message.getData());
        }
        fcmMap.put("from", message.getFrom());
        fcmMap.put("google.collapseKey:", message.getCollapseKey());
        fcmMap.put("google.originalPriority:", (message.getOriginalPriority() == RemoteMessage.PRIORITY_HIGH) ? "high" : "normal");
        fcmMap.put("messageId:", message.getMessageId());
        fcmMap.put("priority:", (message.getPriority() == RemoteMessage.PRIORITY_HIGH) ? "high" : "normal");
        wovg23.instance().pushString("fcmData", dzovvg912.map2Json(fcmMap));
    }

    @Override
    public void onMessageSent(@NonNull String msgId) {
        super.onMessageSent(msgId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }
}