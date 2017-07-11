package com.saxena.ayush.keepit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Ayush on 5/25/2017.
 */

public class MainPage extends AppCompatActivity {
    String uid;
    FloatingActionButton btnAdd;
    static TextView tv;
    FirebaseAuth mAuth;
    ListView listView;
    ArrayAdapter<String> arr;
    HashMap<String,String[]> map;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid=getIntent().getExtras().getString("uid");
        mAuth=FirebaseAuth.getInstance();
        setContentView(R.layout.act_mainpage);
        listView=(ListView) findViewById(R.id.listview);
        btnAdd=(FloatingActionButton)findViewById(R.id.btnAddAccount);
        tv=(TextView)findViewById(R.id.tempTv);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),NewAccount.class);
                i.putExtra("uid",uid);
                startActivity(i);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String text=adapterView.getItemAtPosition(i).toString();
                Intent ii=new Intent(getApplicationContext(),DisplayDetails.class);
                ii.putExtra("title",text);
                ii.putExtra("email",map.get(text)[0]);
                ii.putExtra("password",map.get(text)[1]);
                startActivity(ii);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        JsonFile f=new JsonFile(uid,this);
        arr=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        map=new HashMap<>();
        JSONObject jsonObject=f.getJsonRead();
        if(jsonObject!=null)
        {
            try {
                JSONArray jsonArray=jsonObject.getJSONArray("accounts");
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject obj=jsonArray.getJSONObject(i);
                    String title=obj.getString("title");
                    String email=obj.getString("email");
                    String password=obj.getString("password");
                    map.put(title,new String[]{email,password});
                    arr.add(title);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
        {
            arr.add("No Data Found");
        }
        listView.setAdapter(arr);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.SignOut) {
            mAuth.signOut();
            Intent i=new Intent(this,MainActivity.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
