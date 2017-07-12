package com.saxena.ayush.alarm;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Created by Ayush on 1/29/2017.
 */

public class SplashScreen extends AppCompatActivity {
    ProgressBar pb;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        pb=(ProgressBar)findViewById(R.id.progressBar);

        pb.setMax(100);
        NotificationManager notificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                && !notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            //toast();
            Toast.makeText(this, "Allow Sillent It in Do Not Disturb..", Toast.LENGTH_LONG).show();
            startActivity(intent);
        }
        SqlDatabase db=new SqlDatabase(getApplicationContext());
        db.open();
        Cursor c= db.getCursor(null);

        if(c==null || c.getCount()==0)
        {
            db.createEntry();
            db.closeTable();
            //Toast.makeText(SplashScreen.this, "Setting Everything", Toast.LENGTH_SHORT).show();
             Alert();
        }
        else {
            Intent ii = new Intent(getApplication(), MainActivity.class);
            startActivity(ii);
            finish();
        }
        db.closeTable();
    }
    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    void Alert()
    {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage("Are you from LPU?");
        //alertDialogBuilder.setMessage("Enter your Reg No.->'view time table'\n\n->Click save icon\n\n->'Export XML' \n\n Note:Zoom out and then click export");
        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent xx=new Intent(SplashScreen.this,MainActivity.class);
                startActivity(xx);
                Intent ii=new Intent(SplashScreen.this,GetFromUms.class);
                startActivity(ii);
                finish();
            }
        });
        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent xx=new Intent(SplashScreen.this,MainActivity.class);
                startActivity(xx);
                finish();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
