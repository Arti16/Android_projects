package com.ayush.shivman.ourmessaging;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private static final String UNAME="USER";
    private static final String SENDER="SENDER";
    private static final String MESSAGE="MESSAGE";
    private static final String TIME="TIME";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!isMyServiceRunning(ChatService.class))
        {
            startService(new Intent(this,ChatService.class));
        }
        SqlDatabase db=new SqlDatabase(this);
        db.open();
        Cursor c=db.getCursor(null);
        c.moveToFirst();
        while(!c.isAfterLast())
        {
            String name=c.getString(c.getColumnIndex(UNAME));
            String sender=c.getString(c.getColumnIndex(SENDER));
            String message=c.getString(c.getColumnIndex(MESSAGE));
            String time=c.getString(c.getColumnIndex(TIME));
            Log.d("Cursor", "--"+name+","+sender+","+message+","+time);
            c.moveToNext();
        }
        db.closeTable();
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
}
