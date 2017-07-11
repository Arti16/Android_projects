package com.secure.google.googlesecure;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.concurrent.Executor;

/**
 * Created by ayush on 29/6/17.
 */

public class GoogleService extends Service {
    FirebaseDatabase db= FirebaseDatabase.getInstance();
    DatabaseReference db_location=db.getReference().child("Location");
    DatabaseReference db_users=db.getReference().child("Users");
    DatabaseReference db_request=db.getReference().child("Request");

    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    double latitude,longitude;
    LocationManager locationManager;
    Location location;
    int interval=1000*60*15;
    int dist=0;

    private FusedLocationProviderClient mFusedLocationClient;
    LocationManager locationmanager;
    int count;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

       // LocationUpdater();
       // fn_getlocation();
        Intent i=new Intent(getApplicationContext(),GetLocationService.class);
        stopService(i);
        startService(i);
        FirebaseUpdate();
        int count=0;
        return Service.START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                "GoogleSecure", Context.MODE_PRIVATE);
        String name=sharedPref.getString("username",null);
        db_location=db_location.child(name);
        db_request=db_request.child(name);

    }

    void FirebaseUpdate()
    {
        db_request.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("change--------",dataSnapshot.getValue().toString());
                String text=dataSnapshot.getValue().toString();
                if(text!=null && text.equals("end"))
                {
                    db_request.setValue("done");
                    stopSelf();

                }
                if(text!=null && text.equals("done"))
                {
                    return;
                }
                /*
                stopService(new Intent(getApplicationContext(),TestService.class));
                startService(new Intent(getApplicationContext(),TestService.class).putExtra("msg",text));*/

                db_request.setValue("done");
                Intent i=new Intent(getApplicationContext(),GetLocationService.class);
                stopService(i);
                startService(i);
                //generateNotification(getApplicationContext(),dataSnapshot.getValue().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    /*void LocationUpdater()
    {
        locationmanager= (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {
            if(locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            {
                Log.d("Location","gps available");
                locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locListener);
            }
            else
                locationmanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,10,100,locListener);
        }
    }*/

    private void fn_getlocation(){
        Log.d("GOOGLELOCSERVICE","getlocation()");
        locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnable && !isNetworkEnable){

        }else {

            if (!isGPSEnable && isNetworkEnable){
                Log.d("GoogleLocService","Network enabled##############");
                location = null;
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
                //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,0,this);
                {
                    Intent intent = new Intent("LOCATION_READY");
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                            0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, interval,
                            0, pendingIntent);
                }

            }
            else if (isGPSEnable){
                Log.d("GPS","using gps now#################");
                location = null;
                //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0,this);
                {
                    Intent intent = new Intent("LOCATION_READY");
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                            0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, interval,
                            0, pendingIntent);
                }

            }


        }

    }

    private void generateNotification(Context context,String Text) {
        String message =Text;
        //Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),R.drawable.ringing);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setContentTitle("new message").setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message));

        Notification notification = builder.build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(122, notification);
    }
    /*public class MyLocationListener implements LocationListener
    {

        public void onLocationChanged(final Location location)
        {
            Log.i("********************", "Location changed");
            String text="lat = "+location.getLatitude()+"\nlong = "+location.getLongitude();
            String lat=location.getLatitude()+"";
            String longi=location.getLongitude()+"";
            String accuracy=location.getAccuracy()+"";
            db_location.child("lat").setValue(lat);
            db_location.child("long").setValue(longi);
            db_location.child("accuracy").setValue(accuracy);
            db_location.child("time").setValue(new Date().getTime()+"");
            db_location.child("count").setValue(count++);
        }

        public void onProviderDisabled(String provider)
        {
            Toast.makeText( getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT ).show();
            if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
            {
                    locationmanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locListener);
            }
        }


        public void onProviderEnabled(String provider)
        {
            Toast.makeText( getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
            if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
            {
                if(locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                {
                    Log.d("Location","gps available");
                    locationmanager.removeUpdates(locListener);
                    locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10,100,locListener);
                }
            }
        }


        public void onStatusChanged(String provider, int status, Bundle extras)
        {

        }

    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        {
            Log.d("**************","new rev");
            //startService(new Intent(getApplicationContext(),GoogleService.class));
        }
        //locationmanager.removeUpdates(locListener);
    }
}
