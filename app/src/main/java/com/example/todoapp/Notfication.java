package com.example.todoapp;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;

import com.example.todoapp.model.Tasks;
import com.example.todoapp.model.TasksComparator;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import Database.DatabaseHAndler;

public class Notfication extends JobService {

    NotificationManager mNotifyManager;

    // Notification channel ID.
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";



    DatabaseHAndler db;
    LinkedList<Tasks>list;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {


        db = new DatabaseHAndler(this);
        list = db.getTasks(1);
        Collections.sort(list, new TasksComparator());

        System.out.println(list + "------------------------------------------");

        if (list.size() > 0) {
            mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            createNotificationChannel();
            PendingIntent contentPendingIntent = PendingIntent.getActivity
                    (this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder
                    (this, PRIMARY_CHANNEL_ID)
                    .setContentTitle("Танд хийх ажил байгаа шүү !!!")
                    .setContentText(list.get(0).getTask() + ". дуусах хугацаа " + list.get(0).getDate() + "\n ")
                    .setContentIntent(contentPendingIntent)
                    .setSmallIcon(R.drawable.img)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setAutoCancel(true);

            mNotifyManager.notify(0, builder.build());
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    public void createNotificationChannel() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID, "Job Service notification", NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(false);
            notificationChannel.setDescription("Notifications from Job Service");
            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }
}
