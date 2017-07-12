package com.ayush.shivman.messaging;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by ayush on 11/7/17.
 */

public class TestService extends Service {
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference db_message=db.getReference().child("messages").child("ayush");

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent,  int flags, int startId) {

        db_message.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("snapshot", "onDataChange: "+dataSnapshot.toString()+"\n\n\n");
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    for (DataSnapshot child:ds.getChildren())
                    {
                        Log.d("new message", "from "+ds.getKey()+"  "+child.toString());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }
}
