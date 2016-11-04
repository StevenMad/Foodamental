package com.foodamental.activity;

import android.app.Application;
import android.content.Context;

/**
 * Created by YOUSSEF on 04/11/2016.
 */
public class Foodamental extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        Foodamental.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return Foodamental.context;
    }
}
