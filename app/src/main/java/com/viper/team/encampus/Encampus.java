package com.viper.team.encampus;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class Encampus extends Application
{
    private static final String CHANNEL_1_ID="EncampusNotifictionChannel";
    private static final String CHANNEL_2_ID="EncampusNotifictionReminderChannel";
    @Override
    public void onCreate()
    {
        super.onCreate();
        createChannel();

    }
    private void createChannel()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel channel1=new NotificationChannel(CHANNEL_1_ID,"Encampus",NotificationManager.IMPORTANCE_DEFAULT);
            channel1.setDescription("For Encampus Stuff");
            NotificationChannel channel2=new NotificationChannel(CHANNEL_2_ID,"Encampus",NotificationManager.IMPORTANCE_DEFAULT);
            channel1.setDescription("For Encampus Stuff");
            NotificationManager theMan= getSystemService(NotificationManager.class);
            theMan.createNotificationChannel(channel1);
            theMan.createNotificationChannel(channel2);
        }
    }

}
