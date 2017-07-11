package com.secure.google.googlesecure;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ayush on 30/6/17.
 */

public class SetName extends AppCompatActivity {
    Button btn;
    EditText ed;
    Map<String,String> map_user;
    FirebaseDatabase db= FirebaseDatabase.getInstance();
    DatabaseReference db_location=db.getReference().child("Location");
    DatabaseReference db_users=db.getReference().child("Users");
    DatabaseReference db_request=db.getReference().child("Request");
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        map_user=new HashMap<String, String>();
        setContentView(R.layout.setname);
        btn=(Button)findViewById(R.id.button3);
        ed=(EditText)findViewById(R.id.editText2);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user=ed.getText().toString();
                if(user!=null && !user.isEmpty())
                {
                    if(map_user.get(user)!=null)
                    {
                        Toast.makeText(SetName.this, "Sorry user already exists", Toast.LENGTH_LONG).show();
                        return;
                    }
                    db_users.child(user).setValue("active");
                    db_request.child(user).setValue("default");
                    SharedPreferences.Editor editor = getSharedPreferences("GoogleSecure", Context.MODE_PRIVATE).edit();
                    editor.putString("username", user);
                    editor.apply();
                    finish();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        db_users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                map_user.clear();
                for(DataSnapshot data:dataSnapshot.getChildren())
                {
                    map_user.put(data.getKey(),"1");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
