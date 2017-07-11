package com.example.poision.smarthome;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    TextView lan,fb;
    String ip;
    boolean isConnected=false;
    DatabaseReference head= FirebaseDatabase.getInstance().getReference();
    int port;
    int state;
    EditText ed;
    Button btn,setBtn,create_btn;
    Socket s;
    DataOutputStream dos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lan=(TextView) findViewById(R.id.lan_state);
        fb=(TextView) findViewById(R.id.fb_state);

        ed=(EditText)findViewById(R.id.newip);
        ed.setText("192.168.43.222");
        ip="192.168.43.222";
        lan.setText("Connecting");
        btn=(Button)findViewById(R.id.change_ip);
        setBtn=(Button)findViewById(R.id.open_btn);
        create_btn=(Button)findViewById(R.id.newbtn);
        Background b=new Background();
        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn.setVisibility(View.VISIBLE);
                ed.setVisibility(View.VISIBLE);
            }
        });
        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,Create_new.class);
                startActivity(i);


            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(s!=null&&s.isConnected())
                {
                    try {
                        s.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                ip=ed.getText().toString();
                Background b=new Background();
                b.execute();
            }
        });
        b.execute();
        new AsyncTask<String,String,String>(){

            @Override
            protected String doInBackground(String... strings) {
                while(true)
                {
                    if(s==null || !s.isConnected())
                    {
                        publishProgress("Disconnected");
                    }

                }
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
                lan.setText(values[0]);
            }
        };
    }
    class Background extends AsyncTask<String,Void,Exception>{

        @Override
        protected Exception doInBackground(String... strings) {
            try{
                s=new Socket(ip,2023);
                dos=new DataOutputStream(s.getOutputStream());


            }catch (Exception e){
                return e;
            }

            return  null;
        }

        @Override
        protected void onPostExecute(Exception e) {
            super.onPostExecute(e);
            if(e==null)
            {
                isConnected=true;
                lan.setText("Connected");
            }
        }
    }
/*
    @Override
    protected void onResume() {
        super.onResume();
        new Background(ip).execute();
    }*/

    public void change(int port, int state)
    {
        this.port=port;
        this.state=state;
        try {
            if(s!=null && s.isConnected())
                dos.writeUTF(port+"-"+state);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    {
                        fb.setText("Connected");
                        fb.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                } else {
                    {fb.setText("Not Connected");fb.setTextColor(Color.RED);}
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });
        head.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text=dataSnapshot.child("message").getValue().toString();
                //tv.append("\n"+text);
                if(text==null && !text.contains("-"))
                    return;
                String[] message=text.split("-");
                int port=Integer.parseInt(message[0]);
                int state=Integer.parseInt(message[1]);
                change(port,state);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if(s!=null)s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
