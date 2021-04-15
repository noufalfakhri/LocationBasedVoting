package com.example.location_basedvotingapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {

    public static final String Channel_ID = "channelid" ;
    public static final String channel_name = "channel " ;

    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            createchannels();
        }
    }

    public void createchannels(){

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(Channel_ID,channel_name, NotificationManager. IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLightColor(R.color.design_default_color_on_primary);

            getManager().createNotificationChannel(channel);

        }


        ////////////////////////////////
        System.out.println("created notficiation");


    }

    public NotificationManager getManager(){

        if(mManager==null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager ;
    }

    public NotificationCompat.Builder getChannelNotification(String title, String message, int id ){



        Intent notificationIntent = new Intent(getApplicationContext() ,  votePoll. class ) ;
        notificationIntent.putExtra( "id" , id ) ;
        Bundle dataBundle = new Bundle();
        dataBundle.putInt("id", id);
        System.out.println("here in notification intent its: " + id );
        notificationIntent.putExtras(dataBundle);
        notificationIntent.putExtra("id",id);

        notificationIntent.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
        PendingIntent pendingIntent = PendingIntent. getActivity ( this, 0 , notificationIntent ,PendingIntent.FLAG_UPDATE_CURRENT ) ;

        return new NotificationCompat.Builder(getApplicationContext(),Channel_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_one);

    }


}
