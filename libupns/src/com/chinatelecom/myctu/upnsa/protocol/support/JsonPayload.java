package com.chinatelecom.myctu.upnsa.protocol.support;

import com.chinatelecom.myctu.upnsa.core.utils.JsonUtils;
import com.chinatelecom.myctu.upnsa.exception.PacketMarshallerException;
import com.chinatelecom.myctu.upnsa.protocol.Payload;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * JsonPayload
 * <p/>
 * User: snowway
 * Date: 10/23/13
 * Time: 5:31 PM
 */
public class JsonPayload implements Payload {

    public static final String EMPTY_STRING = "";

    private JSONObject root;

    public JsonPayload() {
        this.root = new JSONObject();
    }

    public JsonPayload(JSONObject root) {
        this.root = root;
    }

    public static JsonPayload from(byte[] content) {
        String json = new String(content);
        try {
            JSONObject object = new JSONObject(json);
            return new JsonPayload(object);
        } catch (JSONException ex) {
            throw new IllegalArgumentException(String.format("内容:%s不是合法的JSON格式", json));
        }
    }


    private PacketMarshallerException createGetException(String key, JSONException cause) {
        return new PacketMarshallerException("不能获取key:" + key + "对应的内容", cause);
    }

    private PacketMarshallerException createSetException(String key, JSONException cause) {
        return new PacketMarshallerException("不能设置key:" + key + "对应的内容", cause);
    }

    @Override
    public String getString(final String key) {
        try {
            return root.has(key) ? root.getString(key) : EMPTY_STRING;
        } catch (JSONException ex) {
            throw createGetException(key, ex);
        }
    }


    @Override
    public boolean getBoolean(String key) {
        try {
            return root.has(key) && root.getBoolean(key);
        } catch (JSONException ex) {
            throw createGetException(key, ex);
        }
    }

    @Override
    public int getInt(String key) {
        try {
            return root.has(key) ? root.getInt(key) : 0;
        } catch (JSONException ex) {
            throw createGetException(key, ex);
        }
    }

    @Override
    public Map getMap(String key) {
        try {
            return root.has(key) ? JsonUtils.toMap(root.getJSONObject(key)) : null;
        } catch (JSONException ex) {
            throw createGetException(key, ex);
        }
    }

    @Override
    public long getLong(String key) {
        try {
            return root.has(key) ? root.getLong(key) : 0L;
        } catch (JSONException ex) {
            throw createGetException(key, ex);
        }
    }

    @Override
    public void put(String key, String value) {
        try {
            root.put(key, value);
        } catch (JSONException ex) {
            throw createSetException(key, ex);
        }
    }

    @Override
    public void put(String key, boolean value) {
        try {
            root.put(key, value);
        } catch (JSONException ex) {
            throw createSetException(key, ex);
        }
    }

    @Override
    public void put(String key, int value) {
        try {
            root.put(key, value);
        } catch (JSONException ex) {
            throw createSetException(key, ex);
        }
    }

    @Override
    public void put(String key, long value) {
        try {
            root.put(key, value);
        } catch (JSONException ex) {
            throw createSetException(key, ex);
        }
    }

    @Override
    public byte[] toByteArray() {
        return this.root.toString().getBytes();
    }

    @Override
    public List<Payload> getPayloadList(String key) {
        if (!this.root.has(key)) {
            return Arrays.asList();
        }
        List<Payload> marshallers = new ArrayList<Payload>();
        try {
            JSONArray array = root.getJSONArray(key);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                marshallers.add(new JsonPayload(object));
            }
            return marshallers;
        } catch (JSONException ex) {
            throw createGetException(key, ex);
        }
    }
}
