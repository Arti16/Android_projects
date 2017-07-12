package com.saxena.ayush.apitest;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.internal.Streams;

import java.util.Locale;
import java.util.Map;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.ResponseMessage;
import ai.api.model.Result;
import ai.api.ui.AIDialog;

public class MainActivity extends AppCompatActivity implements AIListener {
    Button btn,btn2,speak;
    TextView tv;
    TextToSpeech tts;
    private AIService aiService;
    private AIDialog aidialog;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermission();
        btn2=(Button)findViewById(R.id.button2) ;
        speak=(Button)findViewById(R.id.button3);
        btn=(Button)findViewById(R.id.button);
        tv=(TextView)findViewById(R.id.editText);
        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                    tts.setSpeechRate(1.2f);
                    tts.setPitch(0.9f);
                    Toast.makeText(MainActivity.this, "Ready", Toast.LENGTH_SHORT).show();
                }
            }
        });
        speak.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

                tts.speak("Hello sir",TextToSpeech.QUEUE_FLUSH,null,null);
            }
        });
        pd=new ProgressDialog(this);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aiService.startListening();
            }
        });
        final AIConfiguration config = new AIConfiguration("8b8e3924e5064065b4ae47d7392b1454",
                AIConfiguration.SupportedLanguages.EnglishUS,
                AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(this, config);
        aiService.setListener(this);
        aidialog = new AIDialog(this, config);
        aidialog.setResultsListener(new AIDialog.AIDialogListener() {

            @Override
            public void onResult(AIResponse response) {
                Result result = response.getResult();

                // Get parameters
                String parameterString = "";
                if (result.getParameters() != null && !result.getParameters().isEmpty()) {
                    for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                        parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
                    }
                }

                // Show results in TextView.
                tv.setText("You said:\" " + result.getResolvedQuery() +" \""+
                        "\nAction: " + result.getAction() +
                        "\nParameters: " + parameterString+
                        "\nResponse:"+result.getFulfillment().getSpeech()
                );
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    tts.speak(result.getFulfillment().getSpeech(),TextToSpeech.QUEUE_FLUSH,null,null);
                } else {
                    tts.speak(result.getFulfillment().getSpeech(),TextToSpeech.QUEUE_FLUSH,null);
                }
            }

            @Override
            public void onError(AIError error) {
                tv.setText(error.toString());

                aidialog.close();
            }

            @Override
            public void onCancelled() {
                aidialog.close();
            }
        });
    }
    public void listenButtonOnClick(final View view) {
      //  aiService.startListening();
        aidialog.showAndListen();
    }

    public void onResult(final AIResponse response) {
        pd.cancel();
        Result result = response.getResult();

        // Get parameters
        String parameterString = "";
        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
            }
        }

        // Show results in TextView.
        tv.setText("You said:\" " + result.getResolvedQuery() +" \""+
                "\nAction: " + result.getAction() +
                "\nParameters: " + parameterString+
                "\nResponse:"+result.getFulfillment().getSpeech()
        );
        String text=result.getFulfillment().getSpeech();
        if(text!=null && !text.isEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    }

    @Override
    public void onError(AIError error) {
        tv.setText(error.getMessage());
        pd.cancel();
    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {
        pd.setMessage("Listening");
        pd.show();
    }
    public void onPause(){
        if(tts !=null){
            tts.stop();
            tts.shutdown();
        }
        super.onPause();
    }
    @Override
    public void onListeningCanceled() {
        pd.cancel();
    }

    @Override
    public void onListeningFinished() {
        pd.cancel();
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

