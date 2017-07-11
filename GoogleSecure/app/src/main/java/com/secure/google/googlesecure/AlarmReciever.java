package com.secure.google.googlesecure;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by ayush on 2/7/17.
 */

public class AlarmReciever extends BroadcastReceiver {
    Context c;
    @Override
    public void onReceive(Context context, Intent intent) {
        c=context;
        Log.d("AlarmReciever", "onReceive: ");
        if(!isMyServiceRunning(GoogleService.class))
        {
            Intent i=new Intent(context,GoogleService.class);
            context.startService(i);
        }
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
