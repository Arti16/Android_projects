package com.secure.google.googlesecure;

import android.*;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;

/**
 * Created by ayush on 2/7/17.
 */

public class GetLocationService  extends Service{

    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    LocationManager locationManager;
    Location location;
    int interval=1000*60*5;
    String TAG="GetLOcationService";


    PendingIntent pendingIntent;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        fn_getlocation();


    }

    private void fn_getlocation(){
        Log.d("GOOGLELOCSERVICE","getlocation()");
        locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnable && !isNetworkEnable){

        }else {

            if ( isNetworkEnable){
                Log.d("GoogleLocService","Network enabled##############");
                location = null;
                if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
                //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,0,this);
                {
                    Intent intent = new Intent("LOCATION_READY");
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                            1, intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, interval,0, pendingIntent);
                    locationManager.removeUpdates(pendingIntent);
                    locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER,pendingIntent);
                }

            }
            if (isGPSEnable){
                Log.d("GPS","using gps now#################");
                location = null;
                //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0,this);
                {
                    Intent intent = new Intent("LOCATION_READY");
                     pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                            0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, interval,0, pendingIntent);
                    locationManager.removeUpdates(pendingIntent);
                    locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,pendingIntent);
                }

            }


        }
        stopSelf();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
}
