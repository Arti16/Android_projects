package com.saxena.ayush.alarm;

import android.app.Notification;
import android.app.PendingIntent;
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

public class Silent extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        final AudioManager mode = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        //mode.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        mode.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        generateNotification(context,"Your Phone Is Silent Now.\nClick to cancel");
    }
    private void generateNotification(Context context,String Text) {
        String message =Text;

        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),R.drawable.silent);
        Intent i=new Intent(context,SetNormal.class);

        PendingIntent ringPending = PendingIntent.getBroadcast(context,999,i, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setContentTitle("Silent")
                .setContentText(message)
                .setSmallIcon(R.drawable.transsilent)
                .setLargeIcon(largeIcon)
                .setAutoCancel(false)
                .setOngoing(true)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(ringPending)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message));

        Notification notification = builder.build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(108, notification);
    }
}
