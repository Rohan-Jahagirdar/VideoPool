package com.rohanjahagirdar.videopool.Notifications;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmListenerService;
import com.rohanjahagirdar.videopool.MainActivity;
import com.rohanjahagirdar.videopool.R;


/**
 * Created by Rohan Jahagirdar
 */
public class GcmMessageHandler extends GcmListenerService {
    public static final int MESSAGE_NOTIFICATION_ID = 9999;
    private SharedPreferences prefs;
    String PREFS_NAME = "VideoPoolTheApp";


    @Override
    public void onMessageReceived(String from, Bundle data) {
        createNotification(data);
    }

    // Creates notification based on title and body received
    private void createNotification(Bundle data) {
        Context context = getBaseContext();
        prefs = getSharedPreferences(PREFS_NAME, 0);

        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent intent = PendingIntent.getActivity(context, 0, resultIntent, 0);

        GcmMessageHandler.this.startService(resultIntent);

        String message = data.getString("message");
        String source = data.getString("source");
        final String user = data.getString("user");
        String title = data.getString("title");
        final String server_url = data.getString("server");


        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                prefs.edit().putString("server", server_url).putString("user", user).apply();
            }
        });


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_add_to_queue_white)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setContentIntent(intent);

        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());

    }
}
