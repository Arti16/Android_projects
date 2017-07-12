package com.saxena.ayush.alarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Ayush Saxena on 1/20/2017.
 */

public class SetAlarm extends BroadcastReceiver {
    String day;
    SQLiteDatabase db;
    @Override
    public void onReceive(Context context, Intent intent) {
            Intent i=new Intent(context,SetIt.class);
                i.setData(Uri.parse("http://www.mysite.net/LEDstate.txt"));
            i.putExtra("urlpath", "http://www.mysite.net/LEDstate.txt");
            context.startService(i);
        Log.d("YUUUU","Settetetet");
        }
    int tm=101;
    private void generateNotification(Context context,String Text) {
        String message =Text;
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),R.drawable.ringing);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setContentTitle("Title").setContentText(message)
                .setSmallIcon(R.drawable.ringing)
                .setLargeIcon(largeIcon)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message));

        Notification notification = builder.build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(tm++, notification);
    }
    }

