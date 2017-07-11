package com.example.shivmn.firebaselogin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText et1,et2;
    Button b1,b2;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et1=(EditText)findViewById(R.id.email);
        et2=(EditText)findViewById(R.id.pass);
        b1 = (Button)findViewById(R.id.signup);
        b2 = (Button)findViewById(R.id.login);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(this,ChatLayout.class));
            finish();
        }
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MainActivity.this,signin.class);
                startActivity(i);
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et1.getText().toString().trim();
                String pass = et2.getText().toString().trim();
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
                if(!passOk(pass))
                {
                    return;
                }
                firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(MainActivity.this,new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //Toast.makeText(MainActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                if(!task.isSuccessful())
                                {
                                    Toast.makeText(MainActivity.this,"Some Error Occured,Please try again later",Toast.LENGTH_LONG).show();
                                }
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(MainActivity.this,"Regristration Successful",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }
public boolean passOk(String pass)
{
    return true;
}
}
