package com.example.antismokingassistant;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;

//step1
import android.app.Service;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class MyForegroundService extends Service {
    //step2-1
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //step2-1-1
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            Log.e("Service", "Service is running...");
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        ).start();

        //step6 : 알림
        final String CHANNELID = "Foreground Service ID";
        NotificationChannel channel = new NotificationChannel(
                CHANNELID,
                CHANNELID,
                NotificationManager.IMPORTANCE_LOW
        );

        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder notification = new Notification.Builder(this, CHANNELID)
                .setContentText("Service is running")
                .setContentTitle("Service enabled")
                .setSmallIcon(R.drawable.ic_launcher_background);

        startForeground(1001, notification.build());


        return super.onStartCommand(intent, flags, startId);
    }
    //step2-2
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
