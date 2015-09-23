package com.isaac.coolweather.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by IsaacCn on 2015/9/23.
 */
public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
