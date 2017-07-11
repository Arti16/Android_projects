package com.secure.google.googlesecure;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.LongSparseArray;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ayush on 2/7/17.
 */

public class UpdateOnlineService extends IntentService {
    FirebaseDatabase db= FirebaseDatabase.getInstance();
    DatabaseReference db_location=db.getReference().child("Location");
    DatabaseReference db_request=db.getReference().child("Request");
    String name;
    String TAG="UPDATEONLINE";
    public UpdateOnlineService() {
        super("UpdateOnlineService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                "GoogleSecure", Context.MODE_PRIVATE);
        name = sharedPref.getString("username", null);
        String s= DateFormat.format("dd MMM HH:mm:ss", new Date().getTime()).toString();
        try {
            Bundle b = intent.getExtras().getBundle("data");
            if (b != null) {
                String lat = b.getString("lat");
                String longi = b.getString("long");
                String accuracy =b.getString("acc");
                String provider=b.getString("provider");
                Log.d(TAG, "onHandleIntent:"+provider);
                if(Float.parseFloat(accuracy)>100f)
                {
                    String xx=sharedPref.getString("last",null);
                    if(xx!=null)
                    {
                        long last= Long.parseLong(xx);
                        if(System.currentTimeMillis()-last<5*60*1000)
                        {
                            return;
                        }
                    }

                }
                Map<String, String> obj = new HashMap<>();

                obj.put("lat", lat);
                obj.put("long", longi);
                obj.put("accuracy", accuracy);
                obj.put("time",s);
                db_location.child(name).setValue(obj);
                SharedPreferences.Editor editor = getSharedPreferences("GoogleSecure", Context.MODE_PRIVATE).edit();
                editor.putString("last",System.currentTimeMillis()+"");
                editor.apply();
                Log.d("aaaaaaaaaaaaaaaaaaaaa", "onStartCommand: "+s);
                stopSelf();
            }
        }
        catch(Exception ee)
        {
            Log.e(TAG, "onReceive: " + ee.getMessage());
        }

    }
}
