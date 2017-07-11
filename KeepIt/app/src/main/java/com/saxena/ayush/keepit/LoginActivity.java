package com.saxena.ayush.keepit;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Objects;

/**
 * Created by Ayush on 5/20/2017.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    Button btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn0,btnDel;
    RelativeLayout layout;
    TextView pin1,pin2,pin3,pin4,pin5,pin6;
    String tempPin="";
    int pointer;
    String uid;
    String pin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pin= getIntent().getExtras().getString("pin");
        uid= getIntent().getExtras().getString("uid");
        setContentView(R.layout.activity_login);
        init();

    }
    void init()
    {
        btn0=(Button)findViewById(R.id.key_0);
        btn1=(Button)findViewById(R.id.key_1);
        btn2=(Button)findViewById(R.id.key_2);
        btn3=(Button)findViewById(R.id.key_3);
        btn4=(Button)findViewById(R.id.key_4);
        btn5=(Button)findViewById(R.id.key_5);
        btn6=(Button)findViewById(R.id.key_6);
        btn7=(Button)findViewById(R.id.key_7);
        btn8=(Button)findViewById(R.id.key_8);
        btn9=(Button)findViewById(R.id.key_9);
        btnDel=(Button)findViewById(R.id.key_del);

        pin1=(TextView)findViewById(R.id.pin1);
        pin2=(TextView)findViewById(R.id.pin2);
        pin3=(TextView)findViewById(R.id.pin3);
        pin4=(TextView)findViewById(R.id.pin4);
        pin5=(TextView)findViewById(R.id.pin5);
        pin6=(TextView)findViewById(R.id.pin6);

        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btnDel.setOnClickListener(this);
        btnDel.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                pointer=0;
                tempPin="";
                for(int i=1;i<=6;i++)
                    uncheck(i);
                return false;
            }
        });
        layout=(RelativeLayout)findViewById(R.id.relativelayout);
        tempPin="";
        pointer=0;
    }
    void check()
    {
        switch(pointer)
        {
            case 1:
                pin1.setBackground(getMyDrawble(R.drawable.checked_pin));
                break;
            case 2:
                pin2.setBackground(getMyDrawble(R.drawable.checked_pin));
                break;
            case 3:
                pin3.setBackground(getMyDrawble(R.drawable.checked_pin));
                break;
            case 4:
                pin4.setBackground(getMyDrawble(R.drawable.checked_pin));
                break;
            case 5:
                pin5.setBackground(getMyDrawble(R.drawable.checked_pin));
                break;
            case 6:
                pin6.setBackground(getMyDrawble(R.drawable.checked_pin));
                break;
        }
    }
    void uncheck(int c)
    {
        switch(c)
        {
            case 1:
                pin1.setBackground(getMyDrawble(R.drawable.unchecked_pin));
                break;
            case 2:
                pin2.setBackground(getMyDrawble(R.drawable.unchecked_pin));
                break;
            case 3:
                pin3.setBackground(getMyDrawble(R.drawable.unchecked_pin));
                break;
            case 4:
                pin4.setBackground(getMyDrawble(R.drawable.unchecked_pin));
                break;
            case 5:
                pin5.setBackground(getMyDrawble(R.drawable.unchecked_pin));
                break;
            case 6:
                pin6.setBackground(getMyDrawble(R.drawable.unchecked_pin));
                break;
        }
    }
    Drawable getMyDrawble(int id)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getResources().getDrawable(id, this.getTheme());
        } else {
            return getResources().getDrawable(id);
        }
    }
    @Override
    public void onClick(View view) {

            int id=view.getId();
            if(id==R.id.key_del)
            {
                if(pointer==0)
                    return;
                tempPin=tempPin.substring(0,tempPin.length()-1);
                uncheck(pointer);
                pointer--;
                return;
            }
            if(pointer==6)
                return;
            switch (id)
            {
                case R.id.key_0:
                {
                    tempPin=tempPin+0;
                    pointer++;
                    check();
                }
                break;
                case R.id.key_1:
                {
                    tempPin=tempPin+1;
                    pointer++;
                    check();
                }
                break;
                case R.id.key_2:
                {
                    tempPin=tempPin+2;
                    pointer++;
                    check();
                }
                break;
                case R.id.key_3:
                {
                    tempPin=tempPin+3;
                    pointer++;
                    check();
                }
                break;
                case R.id.key_4:
                {
                    tempPin=tempPin+4;
                    pointer++;
                    check();
                }
                break;
                case R.id.key_5:
                {
                    tempPin=tempPin+5;
                    pointer++;
                    check();
                }
                break;
                case R.id.key_6:
                {
                    tempPin=tempPin+6;
                    pointer++;
                    check();
                }
                break;
                case R.id.key_7:
                {
                    tempPin=tempPin+7;
                    pointer++;
                    check();
                }
                break;
                case R.id.key_8:
                {
                    tempPin=tempPin+8;
                    pointer++;
                    check();
                }
                break;
                case R.id.key_9:
                {
                    tempPin=tempPin+9;
                    pointer++;
                    check();
                }
                break;

            }
        if(pointer==6)
        {
            new AsyncTask<Void,Void,Void>(){
                @Override
                protected Void doInBackground(Void... voids) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    if(Objects.equals(pin, tempPin))
                    {
                        Intent i=new Intent(LoginActivity.this,MainPage.class);
                        i.putExtra("uid",uid);
                        startActivity(i);
                        finish();
                    }
                    else
                    {
                        Snackbar snack=Snackbar.make(layout,"Wrong Passcode",Snackbar.LENGTH_LONG).setAction("Forgot?", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(LoginActivity.this, "Baad me dekhlenge", Toast.LENGTH_SHORT).show();
                            }
                        });

                        // Changing message text color
                        snack.setActionTextColor(Color.YELLOW);;
                        snack.show();
                        pointer=0;
                        tempPin="";
                        for(int i=1;i<=6;i++)
                            uncheck(i);
                    }
                }
            }.execute();
        }
    }
}
