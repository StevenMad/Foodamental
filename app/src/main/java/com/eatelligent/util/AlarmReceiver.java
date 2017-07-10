package com.eatelligent.util;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.eatelligent.R;
import com.eatelligent.activity.Courses;
import com.eatelligent.activity.Recipes;
import com.eatelligent.dao.dbimpl.FrigoDB;
import com.eatelligent.dao.model.FrigoObject;

import java.util.List;


public class AlarmReceiver extends BroadcastReceiver {

    int MID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean perso = sharedPreferences.getBoolean("buttonPerso", false);
        Boolean auto = sharedPreferences.getBoolean("buttonAuto", false);
        FrigoDB frigo;

        if (perso) {
            showNotif(context);
        } else if (auto) {
            frigo = new FrigoDB();
            List<FrigoObject> list = frigo.getAllProduct(7);
            if (list.size() > 0)
                showNotif(context);

        }


    }


    private void showNotif(Context context) {

        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, Courses.class);
        Intent notificationIntent2 = new Intent(context, Recipes.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 0,
                notificationIntent2, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                context)
                .setSmallIcon(R.drawable.ic_launcher1)
                .setContentTitle("Attention vos produits vont périmés!!")
                .setContentText("Choisir un des deux menus").setSound(alarmSound)
                .addAction(R.drawable.common_google_signin_btn_icon_light, "Frigo", pendingIntent).setAutoCancel(true)
                .addAction(R.drawable.common_google_signin_btn_icon_light, "Recettes", pendingIntent2).setAutoCancel(true)
                .setAutoCancel(true).setWhen(when)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        notificationManager.notify(MID, mNotifyBuilder.build());
    }
}

