package com.velocitycubed.zohosdk;

import android.app.Application;

public class Host extends Application {
    private static Host instance;

    public static Host getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
