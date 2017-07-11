package com.saxena.ayush.rasp;

/**
 * Created by Ayush on 4/5/2017.
 */

import android.*;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ai.api.model.Result;

public class VoiceRecog extends Activity implements RecognitionListener {

    private TextView returnedText;
    private ToggleButton toggleButton;
    private ProgressBar progressBar;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;

    TextToSpeech tts;
    ProgressDialog pd,statusDialog;
    FirebaseDatabase db= FirebaseDatabase.getInstance();
    DatabaseReference db_message=db.getReference().child("message");
    DatabaseReference db_home_data=db.getReference().child("home_data");
    String preDevice,preRoom;
    int preState;
    int retry=0;
    Map<String,String> map;
    private String LOG_TAG = "Voice ";
    Speech mySpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.voice_activity);
        returnedText = (TextView) findViewById(R.id.textView1);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton1);
        getPermission();
        progressBar.setVisibility(View.INVISIBLE);
        setSpeech();

        map=new HashMap<String,String>();
        setDBChange();
        setUpTTS();

        pd=new ProgressDialog(this);
        pd.setMessage("Listening...");
        pd.setCancelable(false);
        pd.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                speech.cancel();
            }
        });

        statusDialog=new ProgressDialog(this);
        statusDialog.setMessage("Connecting...");
        statusDialog.setCancelable(false);
        statusDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                speech.cancel();
                finish();
            }
        });


        toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setIndeterminate(true);
                    speech.startListening(recognizerIntent);
                } else {
                    progressBar.setIndeterminate(false);
                    progressBar.setVisibility(View.INVISIBLE);
                    //speech.cancel();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speech.cancel();
        speech.destroy();
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
    void setUpTTS()
    {
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
                    AlertDialog.Builder adb=new AlertDialog.Builder(getApplicationContext());
                    adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
                    //adb.setMessage("Error occured try after some time");
                    adb.setMessage("Text to speech unavailable");
                    AlertDialog ad=adb.create();
                    ad.show();
                }

            }
        }.execute();
    }
    void setSpeech()
    {
        mySpeech=new Speech();
        if(speech!=null)
        speech.destroy();
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, Locale.getDefault());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 2);
        //recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS,true);

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

    @Override
    public void onResume() {
        super.onResume();
        setSpeech();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onBeginningOfSpeech() {
        progressBar.setIndeterminate(false);
        progressBar.setMax(10);

    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        progressBar.setIndeterminate(true);
        toggleButton.setChecked(false);
        pd.setMessage("finding results");
    }

    @Override
    public void onError(int errorCode) {
        String message;
        speech.cancel();
        pd.cancel();
        progressBar.setVisibility(View.INVISIBLE);
        System.out.println("\n\n\n\n");
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Unable to hear?";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Something went wrong,Please try again?";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network problem";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "I am unable to understand,try again?";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "I am busy right now,try again later";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "server error";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "unable to get anything,please try again?";
                break;
            default:
                message = "Didn't understand, please try again.?";
                break;
        }
        if(message.contains("?"))
        {
            speak(message);
            retry++;
            if(retry<3)
                callListener();
        }
        else
            speak(message);



        toggleButton.setChecked(false);
    }

    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Log.i(LOG_TAG, "onEvent");
    }

    @Override
    public void onPartialResults(Bundle arg0) {
        ArrayList<String> matches = arg0.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = "";
        if(matches!=null)
            text=matches.get(0);
        returnedText.setText(text);


    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {
        pd.setMessage("Listening");
        pd.show();
    }

    @Override
    public void onResults(final Bundle results) {
        speech.cancel();

        retry=0;
        progressBar.setVisibility(View.INVISIBLE);
        retry=0;
        new AsyncTask<Void,String,String[]>(){

            @Override
            protected String[] doInBackground(Void... voids) {
                String device=null,room=null,reply=null;
                int state=0;
                String action;
                String text="";
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if( matches != null)
                    text= matches.get(0);
                String resultsSpeech[]=mySpeech.init(text);
                action=resultsSpeech[0];
                if(action.equals("turn_on")){
                    if(mySpeech.getState()!=null)
                        state=Integer.parseInt(resultsSpeech[mySpeech.STATE]);
                    device=resultsSpeech[mySpeech.DEVICE];
                    room=resultsSpeech[mySpeech.ROOM];
                }
                else if(action.equals("correct_previous"))
                {
                    device=preDevice;
                    room=preRoom;
                    state=preState;
                    String data=map.get(room+"-"+device);
                    int port;
                    if(data==null)
                        port=-1;
                    else
                        port=Integer.parseInt(data);
                    if(port!=-1)
                        db_message.setValue(port+"-"+(1-state));
                    if(mySpeech.getDevice()!=null)
                    {
                        device=resultsSpeech[mySpeech.DEVICE];
                    }
                    if(mySpeech.getRoom()!=null)
                    {
                        room=resultsSpeech[mySpeech.ROOM];
                    }
                    if(mySpeech.getState()!=null)
                    {
                        state=Integer.parseInt(resultsSpeech[mySpeech.STATE]);
                    }

                }
                reply=resultsSpeech[mySpeech.REPLY];
                if(reply.equalsIgnoreCase("done") && device!=null && room!=null)
                {
                    preDevice = device;
                    preRoom = room;
                    preState = state;
                }
                if(reply.equalsIgnoreCase("done") && device!=null && room!=null) {
                    String data = map.get(room + "-" + device);
                    int port;
                    if (data == null)
                        port = -1;
                    else
                        port = Integer.parseInt(data);


                    if (port == -1) {
                        reply = "Sorry,Device unavailable";
                    } else {
                        publishProgress(port + "-" + state);
                        preDevice = device;
                        preRoom = room;
                        preState = state;
                    }
                }
                speak(reply);
                if((action.contains("turn")||action.contains("correct")))
                {
                    if (!reply.equalsIgnoreCase("done")) {
                        callListener();
                    }
                }
                else if(reply.contains("?"))
                {
                    callListener();
                }
                String xxx[]=new String[4];
                xxx[0]=device;
                xxx[1]=room;
                xxx[2]=state+"";
                xxx[3]=text;
                return xxx;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
                String x=values[0];
                db_message.setValue(x);

            }

            @Override
            protected void onPostExecute(String[] s) {
                super.onPostExecute(s);
                pd.cancel();
                returnedText.setText("You said : "+s[3]);
                returnedText.append("\n"+s[0]+" "+s[1]+" "+s[2]);
            }
        }.execute();
    }

    void callListener()
    {
        new AsyncTask<Void, Void, Exception>() {

            @Override
            protected Exception doInBackground(Void... voids) {

                if(tts.isSpeaking())
                    while (tts.isSpeaking()) ;
                else
                {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception e) {
                super.onPostExecute(e);
                if (e == null)
                {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setIndeterminate(true);
                    speech.cancel();
                    speech.startListening(recognizerIntent);
                }
            }
        }.execute();
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        progressBar.setProgress((int) rmsdB);
    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
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
                        if(statusDialog.isShowing())
                            statusDialog.cancel();
                    }
                } else {
                    {
                        statusDialog.show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });
    }
    void getPermission()
    {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(VoiceRecog.this,
                android.Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.RECORD_AUDIO)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.RECORD_AUDIO},
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