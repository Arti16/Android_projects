package com.saxena.ayush.rasp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.support.annotation.IntegerRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonElement;
import com.google.gson.internal.Excluder;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import ai.api.ui.AIDialog;

public class MainActivity extends AppCompatActivity implements AIListener{
    TextToSpeech tts;
    Button btn,activity_change;
    private AIService aiService;
    ProgressBar pb;
    ProgressDialog pd;
    TextView tv;
    FirebaseDatabase db= FirebaseDatabase.getInstance();
    DatabaseReference db_message=db.getReference().child("message");
    DatabaseReference db_home_data=db.getReference().child("home_data");
    String preDevice,preRoom;
    int preState;
    int retry=0;
    Map<String,String> map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermission();
        btn=(Button)findViewById(R.id.button);
        activity_change=(Button)findViewById(R.id.button2);
        activity_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this, VoiceRecog.class);
                startActivity(i);
            }
        });
        tv=(TextView)findViewById(R.id.editText);
        pb=(ProgressBar)findViewById(R.id.progressBar4);
        pb.setMax(100);
        map=new HashMap<String,String>();
        setDBChange();

        new AsyncTask<Void,Void,Exception>(){

            @Override
            protected Exception doInBackground(Void... voids) {
                tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if(status != TextToSpeech.ERROR) {
                            tts.setLanguage(Locale.US);
                            tts.setSpeechRate(1.2f);
                            tts.setPitch(0.9f);
                        }
                    }
                });
                return null;
            }

            @Override
            protected void onPostExecute(Exception e) {
                super.onPostExecute(e);
                if(e!=null)
                {
                    Toast.makeText(MainActivity.this, "Sorry Text To Speech Not Working", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(MainActivity.this, "Ready", Toast.LENGTH_SHORT).show();

            }
        }.execute();
        pd=new ProgressDialog(this);
        pd.setMessage("Listening...");
        pd.setCancelable(false);
        pd.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                aiService.cancel();
            }
        });
        final AIConfiguration config = new AIConfiguration("08b62c66ba6f4fe18359ef63a007129d",
                AIConfiguration.SupportedLanguages.EnglishUS,
                AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(this, config);
        aiService.setListener(this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aiService.startListening();
            }
        });

    }
    void setDBChange()
    {
        db_home_data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                map.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for(DataSnapshot child:snapshot.getChildren())
                    {
                        map.put(snapshot.getKey()+"-"+child.getKey(),child.getValue().toString());
                    }
                }
                }

                @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    @Override
    public void onResult(AIResponse response) {
        retry=0;
        boolean done_flag=true;
        String device=null,room=null,reply=null;
        int state=0;
        Result result = response.getResult();
        aiService.stopListening();
        String action=result.getAction();
        pb.setProgress(100);
        if(action.equals("turn_on")){
            if (result.getParameters() != null && !result.getParameters().isEmpty()) {
                for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                    if(entry.getKey().equals("room"))
                    {
                        room=entry.getValue().getAsString();
                    }
                    if(entry.getKey().equals("devices"))
                    {
                        device=entry.getValue().getAsString();
                    }
                    if(entry.getKey().equals("state"))
                    {
                        state= Integer.parseInt(entry.getValue().getAsString());
                    }
                }
            }

        }
        else if(action.equals("turn_on_lights"))
        {
            if (result.getParameters() != null && !result.getParameters().isEmpty()) {
                for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                    if(entry.getKey().equals("room"))
                    {
                        room=entry.getValue().getAsString();
                    }

                }

            }
            state=1;
            device="lights";
        }
        else if(action.equals("turn_off_lights"))
        {
            if (result.getParameters() != null && !result.getParameters().isEmpty()) {
                for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                    if(entry.getKey().equals("room"))
                    {
                        room=entry.getValue().getAsString();
                    }
                }
            }
            state=0;
            device="lights";
        }
        else if(action.equals("correct_previous"))
        {
            device=preDevice;
            room=preRoom;
            state=preState;
            Toast.makeText(this, preDevice, Toast.LENGTH_LONG).show();
            String data=map.get(room+"-"+device);
            int port;
            if(data==null)
                port=-1;
            else
                port=Integer.parseInt(data);
            if(port!=-1)
                db_message.setValue(port+"-"+(1-state));
            if (result.getParameters() != null && !result.getParameters().isEmpty()) {
                for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                    if(entry.getKey().equals("room"))
                    {
                        String temp=entry.getValue().getAsString();
                        if(temp!=null && !temp.isEmpty())
                        {
                            room=temp;
                        }
                    }
                    if(entry.getKey().equals("devices"))
                    {
                        String temp=entry.getValue().getAsString();
                        if(temp!=null && !temp.isEmpty())
                        {
                            device=temp;
                        }
                    }
                    if(entry.getKey().equals("state"))
                    {
                        String temp=entry.getValue().getAsString();
                        if(temp!=null && !temp.isEmpty())
                        {
                            state=Integer.parseInt(temp);
                        }

                    }
                }
            }

        }

        reply=result.getFulfillment().getSpeech();
        if(reply.equalsIgnoreCase("done") && device!=null && room!=null)
        {
            preDevice = device;
            preRoom = room;
            preState = state;
        }
        if(reply.equalsIgnoreCase("done") && device!=null && room!=null) {
            String data=map.get(room+"-"+device);
            int port;
            if(data==null)
                port=-1;
            else
                port=Integer.parseInt(data);


            if(port==-1)
            {
                reply="Sorry,Device unavailable";
            }
            else {
                    db_message.setValue(port+"-" + state);
                    preDevice = device;
                    preRoom = room;
                    preState = state;
            }
        }
        String text="You said:"+result.getResolvedQuery()+"\nDevice:"+device+"\nRoom:"+room+"\nState:"+state;
        tv.setText(text);
        tv.append("\n "+reply);
        speak(reply);
        pd.cancel();
        if((action.contains("turn")||action.contains("correct")))
        {
            if (reply == null || !reply.equalsIgnoreCase("done")) {
                callListener();
            }
        }
        else if(reply.contains("?"))
        {
            callListener();
        }

    }
    void speak(String text)
    {
        if(text!=null && text.equalsIgnoreCase("done"))
        {
            text="Done,Thank you";
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            tts.speak(text,TextToSpeech.QUEUE_FLUSH,null,null);
        } else {
            tts.speak(text,TextToSpeech.QUEUE_FLUSH,null);
        }
    }
    void callListener()
    {
        new AsyncTask<Void, Void, Exception>() {

            @Override
            protected Exception doInBackground(Void... voids) {

                while (tts.isSpeaking()) ;
                return null;
            }

            @Override
            protected void onPostExecute(Exception e) {
                super.onPostExecute(e);
                if (e == null)
                    aiService.startListening();
            }
        }.execute();
    }
    @Override
    public void onError(AIError error) {
        /*AlertDialog.Builder adb=new AlertDialog.Builder(this);
        aiService.stopListening();
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        //adb.setMessage("Error occured try after some time");
        adb.setMessage(error.toString());
        AlertDialog ad=adb.create();
        ad.show();*/
        String err=error.getMessage();
        tv.setText(err);
        if(err.contains("No recognition result matched"))
        {
            speak("Sorry,Please try again");
            pd.cancel();
            retry++;
            if(retry==5)
            {
                speak("Turning off,Thank you");
                return;
            }
            new AsyncTask<Void,Void,Exception>(){

                @Override
                protected Exception doInBackground(Void... voids) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        return  e;
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Exception e) {
                    super.onPostExecute(e);
                    if(e==null)
                    aiService.startListening();
                }
            }.execute();
        }
        else
        {
            String text="Try again later";
            speak(text);
            pd.cancel();
        }

    }

    @Override
    public void onAudioLevel(float level) {
        pb.setProgress((int)(level*10));

    }

    @Override
    public void onListeningStarted() {
        pd.show();
        pb.setProgress(0);


    }

    @Override
    public void onListeningCanceled() {

        aiService.stopListening();

    }

    @Override
    public void onListeningFinished() {

    }
    void getPermission()
    {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        12212);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 12212: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Toast.makeText(this, "Sorry", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
