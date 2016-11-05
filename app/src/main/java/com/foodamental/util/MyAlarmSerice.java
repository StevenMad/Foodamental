package com.foodamental.util;

/**
 * Created by YOUSSEF on 31/10/2016.
 */

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.foodamental.R;
import com.foodamental.activity.Courses;

public class MyAlarmSerice extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MyAlarmSerice(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Intent intent2 = new Intent(this, Courses.class);
        long[] pattern = {0, 300, 0};
        PendingIntent pi = PendingIntent.getActivity(this, 01234, intent, 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                //.setSmallIcon(R.)
                .setContentTitle("Take Questionnaire")
                .setContentText("Take questionnaire for Duke Mood Study.")
                .setVibrate(pattern)
                .setAutoCancel(true);

        mBuilder.setContentIntent(pi);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(01234, mBuilder.build());


    }
}