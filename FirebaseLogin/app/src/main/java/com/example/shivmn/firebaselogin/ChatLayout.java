package com.example.shivmn.firebaselogin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SHIVM@n on 7/8/2017.
 */

public class ChatLayout extends AppCompatActivity{
    private EditText messageinput;
    private Button send;
    private FirebaseDatabase mFirebaseInstance=FirebaseDatabase.getInstance();
    ArrayList<messageModel> list;
    private DatabaseReference mFirebaseDatabase=mFirebaseInstance.getReference("messages");
    private ListView messages;

    public ChatLayout() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        messages=(ListView) findViewById(R.id.messages);
        messageinput = (EditText) findViewById(R.id.msginput);
        send = (Button) findViewById(R.id.send);
        list=new ArrayList<messageModel>();
        
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,String> map=new HashMap<String, String>();
                map.put("User",FirebaseAuth.getInstance().getCurrentUser().getEmail());
                map.put("message",messageinput.getText().toString());
               mFirebaseDatabase.child(System.currentTimeMillis()+"").setValue(map);
                // Clear the input
                messageinput.setText("");
            }
        });

        mFirebaseDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot ds, String s) {

                try{
                    /*for(DataSnapshot ds:dataSnapshot.getChildren())
                    {*/
                        messageModel obj=new messageModel();
                        obj.setMessageUser(ds.child("User").getValue().toString());
                        obj.setMessageText(ds.child("message").getValue().toString());
                        obj.setMessageTime(Long.parseLong(ds.getKey()));
                        list.add(obj);
                        Log.d("sasas", "onChildAdded:\n "+ds);
                   // }
                    messages.setAdapter(new MyAdapter(list,getApplicationContext(),ChatLayout.this));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
