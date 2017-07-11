package com.example.shivmn.firebaselogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by SHIVM@n on 6/25/2017.
 */

public class signin extends AppCompatActivity{
    EditText et1;
    EditText et2;
    Button b1;
    FirebaseAuth fauth;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        fauth=FirebaseAuth.getInstance();
        b1=(Button)findViewById(R.id.login);
        et1=(EditText)findViewById(R.id.email);
        et2=(EditText)findViewById(R.id.pass);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=et1.getText().toString().trim();
                String pass=et2.getText().toString().trim();
                if(email.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Please enter the E-mail Address", Toast.LENGTH_LONG).show();
                    return;
                }
                if(pass.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Please enter the Password", Toast.LENGTH_LONG).show();
                    return;
                }
                if(pass.length() < 8)
                {
                    Toast.makeText(getApplicationContext(),"Password must be of atlest 8 characters.", Toast.LENGTH_LONG).show();
                    return;
                }
                fauth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(signin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),"Welcome ", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(),ChatLayout.class);
                            startActivity(i);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Some Error Occured", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
