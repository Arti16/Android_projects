package com.saxena.ayush.tictactoe;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Ayush Saxena on 1/14/2017.
 */

public class Lan extends AppCompatActivity {

    Button host,join;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lan_activity);
        host=(Button)findViewById(R.id.host);
        join=(Button)findViewById(R.id.join);
        host.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Lan.this,Game.class);
                i.putExtra("type","host");
                startActivity(i);

            }
        });
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Lan.this,Game.class);
                i.putExtra("type","join");
                startActivity(i);
            }
        });
    }
}
