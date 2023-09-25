package com.crab.shooter;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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


    private  String map2Json(Map<String, Object> map) {
        Iterator<String> iterator = map.keySet().iterator();
        JSONObject jsonObject = new JSONObject();
        while (iterator.hasNext()) {
            String key = iterator.next();
            try {
                jsonObject.put(key, map.get(key));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return jsonObject.toString();
    }
}
