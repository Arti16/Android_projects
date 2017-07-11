package com.secure.google.googlesecure;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by ayush on 1/7/17.
 */

public class LocationReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();
            Location location = (Location)b.get(LocationManager.KEY_LOCATION_CHANGED);
            if(location==null)
                return;
            String lat=location.getLatitude()+"";
            String longi=location.getLongitude()+"";
            String accuracy=location.getAccuracy()+"";
            Bundle bun=new Bundle();
            bun.putString("lat",lat);
            bun.putString("long",longi);
            bun.putString("acc",accuracy);
            bun.putString("provider",location.getProvider());
        Intent i=new Intent(context,UpdateOnlineService.class);
        i.putExtra("data",bun);
        context.startService(i);

        //Toast.makeText(context, loc.toString(), Toast.LENGTH_SHORT).show();
    }
}
