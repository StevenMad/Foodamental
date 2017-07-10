package com.eatelligent.activity;

import android.app.Application;
import android.content.Context;

/**
 * Created by YOUSSEF on 04/11/2016.
 */
public class Eatelligent extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        Eatelligent.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return Eatelligent.context;
    }
}
