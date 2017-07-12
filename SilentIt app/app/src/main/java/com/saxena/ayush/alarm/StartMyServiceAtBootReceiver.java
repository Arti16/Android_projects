package com.saxena.ayush.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by Ayush on 1/30/2017.
 */

public class StartMyServiceAtBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent i=new Intent(context,SetIt.class);
            i.setData(Uri.parse("http://www.mysite.net/LEDstate.txt"));
            i.putExtra("urlpath", "http://www.mysite.net/LEDstate.txt");
            context.startService(i);
        }
    }
}