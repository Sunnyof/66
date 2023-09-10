package com.sun.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

public class dzovvg912 {
    public static String map2Json(Map<String, Object> map) {
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