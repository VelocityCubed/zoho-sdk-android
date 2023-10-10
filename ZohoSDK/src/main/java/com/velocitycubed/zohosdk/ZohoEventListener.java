package com.velocitycubed.zohosdk;

import org.json.JSONObject;

public interface ZohoEventListener<T> {
    void onEvent(String eventType, T data);
}

