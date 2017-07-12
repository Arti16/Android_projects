package com.saxena.ayush.alarm;

/**
 * Created by Ayush Saxena on 1/18/2017.
 */



import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by Ayush Saxena on 1/18/2017.
 */

public class SetNormal extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final AudioManager mode = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mode.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        NotificationManager notificationManager = (NotificationManager)context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(108);
    }
}
