package com.saxena.ayush.keepit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    Button btn;
    boolean valid=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent().getExtras()!=null  && getIntent().getExtras().getBoolean("Valid"))
            valid=true;
        setContentView(R.layout.activity_main);
        btn=(Button)findViewById(R.id.button2_actmain);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth=FirebaseAuth.getInstance();
                mAuth.signOut();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        GetPin getP=new GetPin(this);
        String pin=getP.getPin();
        if(currentUser==null|| pin.equals("NOPIN"))
        {
            //Toast.makeText(this, "Logged in as"+currentUser.getUid(), Toast.LENGTH_SHORT).show();
            Intent i=new Intent(this,FirstSetUp.class);
            startActivity(i);
            finish();
        }
        else if(!valid)
        {
            Intent i=new Intent(this,LoginActivity.class);
            i.putExtra("pin",pin);
            i.putExtra("uid",mAuth.getCurrentUser().getUid());
            startActivity(i);
            finish();
        }


    }
}
