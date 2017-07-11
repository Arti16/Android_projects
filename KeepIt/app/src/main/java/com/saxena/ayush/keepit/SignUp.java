package com.saxena.ayush.keepit;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Layout;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Ayush on 5/24/2017.
 */

public class SignUp extends Fragment {
    RelativeLayout layout;
    ProgressDialog pd;
    private TextInputEditText inputEmail, inputPassword,inputConfirmPass;
    private TextInputLayout inputLayoutEmail, inputLayoutPassword,inputLayoutConfirmPass;
    private Button btnSignUp,backbtn;
    private FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_signup,container,false);
        inputLayoutEmail = (TextInputLayout) view.findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) view.findViewById(R.id.input_layout_password);
        inputLayoutConfirmPass = (TextInputLayout) view.findViewById(R.id.input_layout_Confirm_password);
        inputEmail = (TextInputEditText) view.findViewById(R.id.input_email);
        inputPassword = (TextInputEditText) view.findViewById(R.id.input_password);
        inputConfirmPass = (TextInputEditText) view.findViewById(R.id.input_confirm_password);
        btnSignUp = (Button) view.findViewById(R.id.btn_signup);
        backbtn=(Button)view.findViewById(R.id.btnback);
        layout=(RelativeLayout)view.findViewById(R.id.parent_signup);

        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));
        inputConfirmPass.addTextChangedListener(new MyTextWatcher(inputConfirmPass));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        pd=new ProgressDialog(getActivity());
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OptionFragment frag=new OptionFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();

    }
    private void submitForm() {

        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }
        if (!checkConfirmPassword()) {
            return;
        }
        pd.setMessage("Please wait...\nCreating account..");
        pd.show();
        String email=inputEmail.getText().toString().trim();
        String password=inputPassword.getText().toString().trim();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pd.cancel();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Fire", "createUserWithEmail:success");
                            Snackbar snack=Snackbar.make(layout,"Successfully Signed Up ",Snackbar.LENGTH_LONG);
                            snack.show();
                            FragmentSuccess frag=new FragmentSuccess();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            String message;
                            try{
                                message=task.getException().getLocalizedMessage();
                            }
                            catch (Exception e)
                            {
                                message="Unable to sign Up...Please Retry";
                            }
                            Snackbar snack=Snackbar.make(layout,message,Snackbar.LENGTH_LONG);
                            snack.show();
                        }
                    }
                });
    }
    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError("Please Enter Valid Email Id");
            //requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        String pass=inputPassword.getText().toString().trim();
        if (pass.isEmpty() || pass.length()<8) {
            inputLayoutPassword.setError("Enter Strong Password");
            //requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }
    private boolean checkConfirmPassword()
    {
        if(!inputPassword.getText().toString().trim().equals(inputConfirmPass.getText().toString().trim()))
        {
            inputLayoutConfirmPass.setError("Passwords do not match");
           // requestFocus(inputConfirmPass);
            return false;
        }
        else {
            inputLayoutConfirmPass.setErrorEnabled(false);
        }
        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_password:
                    validatePassword();
                    break;
                case R.id.input_confirm_password:
                    checkConfirmPassword();
                    break;
            }
        }
    }
}
