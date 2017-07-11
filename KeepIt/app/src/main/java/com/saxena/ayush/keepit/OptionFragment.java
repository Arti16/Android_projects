package com.saxena.ayush.keepit;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Ayush on 5/24/2017.
 */

public class OptionFragment extends Fragment {
    Button btnSignup,btnLogin;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View context=inflater.inflate(R.layout.option_fragment,container,false);
        btnSignup=(Button)context.findViewById(R.id.btn_signup);
        btnLogin=(Button)context.findViewById(R.id.btn_login);
        return context;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUp signUpFrag=new SignUp();
               getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,signUpFrag).commit();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 LoginFirebase Frag=new LoginFirebase();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,Frag).commit();
            }
        });
    }
}
