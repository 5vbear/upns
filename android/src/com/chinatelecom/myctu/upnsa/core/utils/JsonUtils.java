package com.chinatelecom.myctu.upnsa.core.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Utility class for Json
 * User: snowway
 * Date: 11/2/13
 * Time: 10:30 PM
 */
public class JsonUtils {

    /**
     * 将JSONObject对象转换成Map
     *
     * @param object
     * @return
     * @throws JSONException
     */
    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap();
        Iterator keys = object.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            map.put(key, fromJson(object.get(key)));
        }
        return map;
    }

    /**
     * 将JSONArray对象转换成List
     * @param array
     * @return
     * @throws JSONException
     */
    public static List toList(JSONArray array) throws JSONException {
        List list = new ArrayList();
        for (int i = 0; i < array.length(); i++) {
            list.add(fromJson(array.get(i)));
        }
        return list;
    }

    private static Object fromJson(Object json) throws JSONException {
        if (json instanceof JSONObject) {
            return toMap((JSONObject) json);
        } else if (json instanceof JSONArray) {
            return toList((JSONArray) json);
        } else {
            return json;
        }
    }
}
