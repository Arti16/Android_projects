package com.saxena.ayush.keepit;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Ayush on 5/23/2017.
 */

public class FirstSetUp extends AppCompatActivity {
    Button next,prev;
    int pointer=1;
    TextView dot1,dot2,dot3;
    FirebaseAuth auth;
    static String loginPin="";
    RelativeLayout layout;
    static boolean confirmed=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_setup);
        dot1=(TextView)findViewById(R.id.dot1);
        dot2=(TextView)findViewById(R.id.dot2);
        dot3=(TextView)findViewById(R.id.dot3);
        prev=(Button)findViewById(R.id.btn_prev);
        layout=(RelativeLayout)findViewById(R.id.parent_firstsetup);
        OptionFragment oo=new OptionFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,oo).commit();
        check();
        next=(Button)findViewById(R.id.btn_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pointer==1)
                {
                    auth=FirebaseAuth.getInstance();
                    FirebaseUser currentUser = auth.getCurrentUser();
                    if(currentUser!=null)
                    {
                        pointer++;
                        check();
                        uncheck(1);
                        SetPinFragment frag2=new SetPinFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag2).commit();
                        prev.setVisibility(View.VISIBLE);
                    }
                }
                else if(pointer==2)
                {
                    if(loginPin.length()<6)
                    {
                        Snackbar snack=Snackbar.make(layout,"Please set the pin",Snackbar.LENGTH_LONG);
                        snack.show();
                    }
                    else{
                        pointer++;
                        check();
                        uncheck(2);
                        ConfirmPinFragment frag3=new ConfirmPinFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag3).commit();


                    }
                }
                else if(pointer==3)
                {
                    if(confirmed)
                    {
                        GetPin g=new GetPin(FirstSetUp.this);
                        g.SetPin(loginPin);
                        Intent i=new Intent(FirstSetUp.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pointer==2)
                {
                    OptionFragment oo=new OptionFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,oo).commit();
                    loginPin="";
                    confirmed=false;
                    pointer--;
                    check();uncheck(2);
                    prev.setVisibility(View.INVISIBLE);
                }
                else if(pointer==3)
                {
                    SetPinFragment frag2=new SetPinFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag2).commit();
                    loginPin="";
                    confirmed=false;
                    pointer--;
                    check();uncheck(3);
                }
            }
        });
        

    }
    void check()
    {
        switch(pointer)
        {
            case 1:
                dot1.setBackground(getMyDrawble(R.drawable.checked_pin));
                break;
            case 2:
                dot2.setBackground(getMyDrawble(R.drawable.checked_pin));
                break;
            case 3:
                dot3.setBackground(getMyDrawble(R.drawable.checked_pin));
                break;
        }
    }
    void uncheck(int c)
    {
        switch(c)
        {
            case 1:
                dot1.setBackground(getMyDrawble(R.drawable.unchecked_pin));
                break;
            case 2:
                dot2.setBackground(getMyDrawble(R.drawable.unchecked_pin));
                break;
            case 3:
                dot3.setBackground(getMyDrawble(R.drawable.unchecked_pin));
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
}
