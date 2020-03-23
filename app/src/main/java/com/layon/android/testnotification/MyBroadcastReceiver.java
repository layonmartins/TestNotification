package com.layon.android.testnotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static androidx.core.app.NotificationCompat.PRIORITY_DEFAULT;
import static androidx.core.content.ContextCompat.getSystemService;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "MyBroadcastReceiver";

    private final static String Channel_ID = "TestNotification";
    private final static int Notification_ID = 1236;

    @Override
    public void onReceive(Context context, Intent intent) {

        StringBuilder sb = new StringBuilder();
        sb.append("Action: " + intent.getAction() + "\n");
        sb.append("URI: " + intent.toUri(Intent.URI_INTENT_SCHEME).toString() + "\n");
        String log = sb.toString();
        Log.d(TAG, log);
        Toast.makeText(context, log, Toast.LENGTH_LONG).show();

        //call IncommingCall activity:
        //Intent intentCall = new Intent(context, IncommingCall.class);
        //intentCall.setClassName("com.layon.android.testnotification", "com.layon.android.testnotification.IncommingCall");
        //intentCall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //context.getApplicationContext().startActivity(intentCall);

        //create an notification
        createNotification(context);
    }

    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel_name";
            String description = "channel_description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(Channel_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void createNotification(Context context) {

        // create a channel
        createNotificationChannel(context);

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(context, IncommingCall.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // create a notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Channel_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Title")
                .setContentText("ContentText")
                .setPriority(PRIORITY_DEFAULT)
                // set the intent that will fire when the user taps the notification
                //.addAction(R.drawable.dismiss, "DISMISS", pendingIntent)
                //.addAction(R.drawable.answer, "ANSWER", pendingIntent)
                .setFullScreenIntent(pendingIntent, true)
                .setAutoCancel(true);

        // show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(Notification_ID, builder.build());
    }
}
