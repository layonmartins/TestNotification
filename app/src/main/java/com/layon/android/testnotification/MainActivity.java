package com.layon.android.testnotification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import static androidx.core.app.NotificationCompat.*;

public class MainActivity extends AppCompatActivity {

    private final static String Channel_ID = "TestNotification2";
    private final static int Notification_ID = 1241;

    BroadcastReceiver broadcastReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if(action.equals("android.os.action.POWER_SAVE_MODE_CHANGED")) {
                    postNotification();
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.os.action.POWER_SAVE_MODE_CHANGED");
        registerReceiver(broadcastReceiver, intentFilter);
    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel_name";
            String description = "channel_description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(Channel_ID, name, importance);
            channel.setDescription(description);
            channel.enableVibration(false);
            //channel.enableLights(true);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void notify(View view) {
        postNotification();
    }

    public void postNotification(){
        // create a channel
        createNotificationChannel();

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, IncommingCall.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // create a notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, Channel_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Title")
                .setContentText("ContentText")
                .setPriority(PRIORITY_DEFAULT);
                // set the intent that will fire when the user taps the notification
                //.addAction(R.drawable.dismiss, "DISMISS", pendingIntent)
                //.addAction(R.drawable.answer, "ANSWER", pendingIntent)
                //.setContentIntent(pendingIntent)
                //.setOnlyAlertOnce(true)
                //.setAutoCancel(true);

        // show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(Notification_ID, builder.build());
    }
}

