package com.game.service;

import androidx.annotation.NonNull;

import com.game.util.JSONUtil;
import com.game.util.SharePreferenceHelp;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;

public class LocalFirebaseMessagingService extends FirebaseMessagingService {
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
        SharePreferenceHelp.instance().pushString("fcmData", JSONUtil.map2Json(fcmMap));
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
