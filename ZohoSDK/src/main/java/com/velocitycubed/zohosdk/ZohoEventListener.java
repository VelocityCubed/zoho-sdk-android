package com.velocitycubed.zohosdk;

public interface ZohoEventListener<T> {
    void onEvent(String eventType, T data);
}

