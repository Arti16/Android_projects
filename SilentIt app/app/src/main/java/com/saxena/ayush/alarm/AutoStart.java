package com.saxena.ayush.alarm;

/**
 * Created by Ayush on 1/30/2017.
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.util.Calendar;

public class AutoStart extends BroadcastReceiver{
    Context c;
    @Override
    public void onReceive(Context context, Intent intent) {
        c=context;
        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)){
            //To start new intent when phone starts up
            Intent ii=new Intent(context,SetIt.class);
            ii.setData(Uri.parse("http://www.mysite.net/LEDstate.txt"));
            ii.putExtra("urlpath", "http://www.mysite.net/LEDstate.txt");
            context.startService(ii);
            setAlarm();
        }
        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_REBOOT)){
            //To start new intent when phone starts up
            Intent ii=new Intent(context,SetIt.class);
            ii.setData(Uri.parse("http://www.mysite.net/LEDstate.txt"));
            ii.putExtra("urlpath", "http://www.mysite.net/LEDstate.txt");
            context.startService(ii);
            setAlarm();
        }
        //To Start Application on Phone Start-up - 10/7/2015(END OF VERSION)
    }
    void setAlarm()
    {

        final AlarmManager alarmMgr = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        Intent set = new Intent(c, SetAlarm.class);
        PendingIntent SetPending = PendingIntent.getBroadcast(c, 100, set, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND, 0);
        alarmMgr.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis()+5*60*1000, AlarmManager.INTERVAL_DAY, SetPending);
    }
}
