package com.saxena.ayush.qr;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * Created by Ayush Saxena on 12/28/2016.
 */

public class ShowQR extends AppCompatActivity {
    ImageView qr;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showqr);
        qr=(ImageView)findViewById(R.id.imageView);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Your QR");
        String text;
        String type=getIntent().getStringExtra("type");



        if(type.equals("number"))
        {
            String name=getIntent().getStringExtra("name");
            String number=getIntent().getStringExtra("number");
            if(number.length()>10)
            number=number.substring(number.length()-10);
            text="$phone$"+name+"$"+number+"$";
            Toast.makeText(this,name, Toast.LENGTH_SHORT).show();
        }
        else if(type.equals("text"))
        {
            text="$text$"+getIntent().getStringExtra("text");
        }
        else //(type.equals("link"))
        {
            text="$link$"+getIntent().getStringExtra("link");
        }
        showiIt(text);

    }
    void showiIt(String text)
    {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try{
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qr.setImageBitmap(bitmap);
        }
        catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
