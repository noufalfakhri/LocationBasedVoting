package com.example.location_basedvotingapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class NotificationPublisher extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);

        String title =votePoll.getTit() + " poll result! ";
        int priority = 1;

        int id = 1;
        System.out.println("id is"+  id);

        NotificationCompat.Builder nb = notificationHelper.getChannelNotification(title, "high priority",id);
        notificationHelper.getManager().notify(1, nb.build());


    }
}
