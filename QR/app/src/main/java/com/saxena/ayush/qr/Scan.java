package com.saxena.ayush.qr;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Ayush Saxena on 12/28/2016.
 */

public class Scan extends AppCompatActivity {
    TextView tv;
    Button btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan);
        tv = (TextView) findViewById(R.id.scrolltext);
        btn = (Button) findViewById(R.id.copybbutton);
        final String data = getIntent().getStringExtra("DATA");
        if (data.contains("$text$")) {
            tv.setText(data.substring(6));
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Copied data", data.substring(6));
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (data.contains("$link$")) {
            String link = data.substring(6);
            if (!(link.contains("http"))) {
                link = "https://" + link;
            }
            final String l1 = link;
            AlertDialog.Builder ad = new AlertDialog.Builder(Scan.this);
            ad.setTitle("Link Found");
            ad.setMessage(link);
            ad.setPositiveButton("Open", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Uri uri = Uri.parse(l1);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                    finish();
                }
            });
            ad.setNeutralButton("Copy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Copied data", l1);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
            ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    finish();
                }
            });
            AlertDialog adb = ad.create();
            adb.show();
        } else if (data.contains("$phone$")) {
            String temp = data.substring(7);
            String name = "", number = "", num = "";
            int i;
            for (i = 0; temp.charAt(i) != '$'; name = name + temp.charAt(i), i++) ;
            temp = temp.substring(i + 1);
            number = temp.substring(0, temp.length() - 1);
            for (i = 0; i < number.length(); i++) {
                if (number.charAt(i) - 48 >= 0 && number.charAt(i) - 48 <= 9)
                    num += number.charAt(i);
            }
            final String finalNum=num;
            AlertDialog.Builder ad = new AlertDialog.Builder(Scan.this);
            ad.setTitle("Contact Found");
            ad.setMessage("Name=" + name + "\nNumber=" + num);
            tv.setText("Name=" + name + "\nNumber=" + num);
            ad.setPositiveButton("Call", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+finalNum));
                    if (ActivityCompat.checkSelfPermission(Scan.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(callIntent);
                }
            });
            ad.setNeutralButton("Copy Number", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Copied data",finalNum);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
                }
            });
            ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    finish();
                }
            });
            AlertDialog adb=ad.create();
            adb.show();
        }
        else
        {
            if(((data.contains("http://")||data.contains("https://"))&& data.contains(".") &&(data.contains("www")||data.contains(".com")||data.contains(".in")||data.contains(".org")||data.contains(".edu"))))
            {
                tv.setText(data);
                final String l1=data;
                AlertDialog.Builder ad=new AlertDialog.Builder(Scan.this);
                ad.setTitle("Link Found");
                ad.setMessage(data);
                ad.setPositiveButton("Open", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = Uri.parse(l1);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        finish();
                    }
                });
                ad.setNeutralButton("Copy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Copied data",l1);
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(getApplicationContext(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                });
                AlertDialog adb=ad.create();
                adb.show();
            }
            tv.setText(data);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Copied data",data);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}
