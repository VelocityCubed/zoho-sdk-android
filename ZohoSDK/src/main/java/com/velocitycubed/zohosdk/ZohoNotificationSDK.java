package com.velocitycubed.zohosdk;

import android.app.Application;
import android.content.res.Resources;
import android.util.Log;

import com.zoho.livechat.android.ZohoLiveChat;
import com.zoho.salesiqembed.ZohoSalesIQ;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ZohoNotificationSDK {

    private String TAG = "ZohoNotificationSDK";

    public void handle(JSONObject data) {
        Map<String, Object> result = null;

        try {
            result = toMap(data.getJSONObject("data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Application application = Host.getInstance();
        ZohoSalesIQ.Notification.handle(application, result);
    }

    public void enablePush(String deviceTokenString, boolean isTestDevice) {
        Log.v(TAG, "enablePush: { isTestDevice: '" + isTestDevice + "', token: '" + deviceTokenString + "' }");
        ZohoLiveChat.Notification.enablePush(deviceTokenString, isTestDevice);
        Resources resources = Host.getInstance().getResources();
        ZohoLiveChat.Notification.setIcon(R.mipmap.ic_launcher);
    }

    public void disablePush(String deviceTokenString, boolean isTestDevice) {
        Log.v(TAG, "disablePush: { isTestDevice: '" + isTestDevice + "', token: '" + deviceTokenString + "' }");
        ZohoLiveChat.Notification.disablePush(deviceTokenString, isTestDevice);
    }

    private Map<String, Object> toMap(JSONObject jsonobj) throws JSONException {
        Map<String, Object> map = new HashMap<>();
        Iterator<String> keys = jsonobj.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = jsonobj.get(key);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    private List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }
}
