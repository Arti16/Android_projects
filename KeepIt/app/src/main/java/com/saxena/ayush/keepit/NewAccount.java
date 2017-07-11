package com.saxena.ayush.keepit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewAccount extends AppCompatActivity {
    EditText title,email,pass;
    Button btnSave;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid=getIntent().getExtras().getString("uid");
        setContentView(R.layout.activity_new_account);
        title=(EditText)findViewById(R.id.titleAcc);
        email=(EditText)findViewById(R.id.emailAcc);
        pass=(EditText)findViewById(R.id.passwordAcc);
        btnSave=(Button)findViewById(R.id.btnSaveAcc);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stitle=title.getText().toString();
                String semail=email.getText().toString();
                String spass=pass.getText().toString();
                JSONObject newObj=null;
                if(!(stitle.isEmpty()||semail.isEmpty()||spass.isEmpty()))
                {
                    JsonFile obj=new JsonFile(uid,getApplicationContext());
                    JSONObject jsonObject=obj.getJsonRead();

                    if(jsonObject!=null)
                    {
                        try {
                            JSONArray arr=jsonObject.getJSONArray("accounts");
                            JSONObject neww=new JSONObject();
                            neww.put("title",stitle);
                            neww.put("email",semail);
                            neww.put("password",spass);
                            arr.put(neww);
                            newObj=new JSONObject();
                            newObj.put("uid",uid);
                            newObj.put("accounts",arr);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        try {
                            JSONArray arr=new JSONArray();
                            JSONObject neww=new JSONObject();
                            neww.put("title",stitle);
                            neww.put("email",semail);
                            neww.put("password",spass);
                            arr.put(neww);
                            newObj=new JSONObject();
                            newObj.put("uid",uid);
                            newObj.put("accounts",arr);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if(obj.writeJson(newObj))
                        Toast.makeText(NewAccount.this, "Successfully Saved", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
