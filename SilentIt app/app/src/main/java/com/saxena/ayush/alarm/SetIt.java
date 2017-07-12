package com.saxena.ayush.alarm;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Ayush Saxena on 1/20/2017.
 */

public class SetIt extends IntentService {


    String day;
    SqlDatabase db;



    public SetIt() {
        super("SetIt");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        db=new SqlDatabase(getApplicationContext());
        Context context = getApplicationContext();
        Calendar cal = Calendar.getInstance();
        switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                day = "monday";
                break;
            case Calendar.TUESDAY:
                day = "tuesday";
                break;
            case Calendar.WEDNESDAY:
                day = "wednesday";
                break;
            case Calendar.THURSDAY:
                day = "thursday";
                break;
            case Calendar.FRIDAY:
                day = "friday";
                break;
            case Calendar.SATURDAY:
                day = "saturday";
                break;
            case Calendar.SUNDAY:
                day = "sunday";
                break;
        }
        final AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent ringIntent = new Intent(context, SetNormal.class);

        Intent silent = new Intent(context, Silent.class);
        db.open();
        Cursor cur = db.getCursor(null);
        cur.moveToFirst();
        int start = 0, end;
        while (!cur.isAfterLast()) {
            if (start == 0 && cur.getInt(cur.getColumnIndex(day)) == 1) {
                start = cur.getInt(cur.getColumnIndex("time"));
                //Toast.makeText(context, "start   " + start, Toast.LENGTH_LONG).show();
            }
            if (start != 0 && cur.getInt(cur.getColumnIndex(day)) == 0) {
                end = cur.getInt(cur.getColumnIndex("time"));
                //Toast.makeText(context,start+ "  - " +end, Toast.LENGTH_LONG).show();
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, start);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                 PendingIntent ringPending = PendingIntent.getBroadcast(context, end, ringIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                 PendingIntent silentPending = PendingIntent.getBroadcast(context, start, silent, PendingIntent.FLAG_UPDATE_CURRENT);
                long ii=calendar.getTimeInMillis()-System.currentTimeMillis();
                //generateNotification(context, "Set at \n"+calendar.get(Calendar.HOUR_OF_DAY)+"");
                alarmMgr.set(AlarmManager.RTC, calendar.getTimeInMillis(), silentPending);
                calendar.set(Calendar.HOUR_OF_DAY, end);
                alarmMgr.set(AlarmManager.RTC, calendar.getTimeInMillis(), ringPending);
                //generateNotification(context, "End at \n" + calendar.get(Calendar.HOUR_OF_DAY));
                start = 0;
            }
            cur.moveToNext();
        }
        cur.close();

        db.closeTable();
        generateNotification(context,"Silent Times are set now.");
    }
    int tm=101;
    private void generateNotification(Context context,String Text) {
        String message =Text;
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),R.drawable.ringing);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setContentTitle("Set for "+day).setContentText(message)
                .setSmallIcon(R.drawable.transnormal)
                .setLargeIcon(largeIcon)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message));

        Notification notification = builder.build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(tm++, notification);
    }
}
