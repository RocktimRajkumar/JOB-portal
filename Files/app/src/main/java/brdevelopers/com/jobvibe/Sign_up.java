package brdevelopers.com.jobvibe;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by End User on 27-03-2018.
 */

public class Sign_up extends AppCompatActivity implements TextWatcher,View.OnClickListener{

    private TextInputLayout til_password,til_cpassword,til_email;
    private EditText et_password,et_cpassword,et_email;
    TextView tv_signup;
    private int i_password=0,i_cpassword=0,i_email=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        til_password=findViewById(R.id.TIL_password);
        til_cpassword=findViewById(R.id.TIL_cpassword);
        et_password=findViewById(R.id.ET_password);
        et_cpassword=findViewById(R.id.ET_cpassword);
        et_email=findViewById(R.id.ET_email);
        til_email=findViewById(R.id.TIL_email);
        tv_signup=findViewById(R.id.TV_signbutton);

        et_password.addTextChangedListener(this);
        et_cpassword.addTextChangedListener(this);
        et_email.addTextChangedListener(this);
        tv_signup.setOnClickListener(this);


    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {


    }

    @Override
    public void afterTextChanged(Editable s) {

        String email = et_email.getText().toString().trim();
        String pattern = "^[a-zA-Z0-9]{1,20}@[a-zA-Z]{1,10}.(com|org)$";
        Matcher matcherObj = Pattern.compile(pattern).matcher(email);

        if(et_email.getText().length()>0 && !matcherObj.matches()) {
            til_email.setError(getString(R.string.error_email));
            i_email=0;

        }
        if(et_email.getText().length()>0 && matcherObj.matches()) {
            til_email.setError(null);
            i_email=1;
        }
        if(et_email.getText().length()==0)
        {
            til_email.setError(null);
            i_email=0;
        }

        if(et_password.getText().length()>0){
            til_password.setPasswordVisibilityToggleEnabled(true);
            if(et_password.getText().length()<8) {
                til_password.setError(getString(R.string.error_password));
                i_password = 0;
            }

            else {
                i_password = 1;
                til_password.setError(null);
            }
        }
        if(et_cpassword.getText().length()>0){
            til_cpassword.setPasswordVisibilityToggleEnabled(true);
            if(!et_password.getText().toString().equals(et_cpassword.getText().toString()))
            {
                til_cpassword.setError(getString(R.string.error_cpassword));
                i_cpassword=0;
            }
            else {
                til_cpassword.setError(null);
                i_cpassword = 1;
            }
        }
        if(et_password.getText().length()==0) {
            til_password.setPasswordVisibilityToggleEnabled(false);
            i_password = 0;
            til_password.setError(null);
        }
        if(et_cpassword.getText().length()==0) {
            til_cpassword.setPasswordVisibilityToggleEnabled(false);
            til_cpassword.setError(null);
            i_cpassword = 0;
        }

    }

    @Override
    public void onClick(View v) {

        if(i_email==0)
            et_email.requestFocus();
        else if(i_password==0)
            et_password.requestFocus();
        else if(i_cpassword==0)
            et_cpassword.requestFocus();
        else {
            Intent profile = new Intent(Sign_up.this, Profile.class);
            startActivity(profile);
        }


        final Float elevation=tv_signup.getElevation();
        tv_signup.setElevation(-elevation);

        int TIMMER=200;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(i_email==0 && et_email.getText().toString().length()==0) {
                    et_email.requestFocus();
                    til_email.setError(getString(R.string.error_empty_field));
                    tv_signup.setElevation(elevation);
                }
                else if(i_email==0) {
                    et_email.requestFocus();
                    tv_signup.setElevation(elevation);
                }

                if(i_password==0 && et_password.getText().toString().length()==0){
                    if(i_email==0)
                        et_email.requestFocus();
                    else
                        et_password.requestFocus();
                    til_password.setError(getString(R.string.error_empty_field));
                    tv_signup.setElevation(elevation);
                }
                else if(i_password==0){
                    if(i_email==0)
                        et_email.requestFocus();
                    else
                        et_password.requestFocus();
                    tv_signup.setElevation(elevation);
                }

                if(i_cpassword==0 && et_cpassword.getText().toString().length()==0){
                    if(i_email==0)
                        et_email.requestFocus();
                    else if(i_password==0)
                        et_password.requestFocus();
                    else
                        et_cpassword.requestFocus();
                    til_cpassword.setError(getString(R.string.error_empty_field));
                    tv_signup.setElevation(elevation);
                }
                else if(i_cpassword==0){
                    if(i_email==0)
                        et_email.requestFocus();
                    else if(i_password==0)
                        et_password.requestFocus();
                    else
                        et_cpassword.requestFocus();
                    tv_signup.setElevation(elevation);
                }

                if(i_password==1 && i_email==1 &&i_cpassword==1)
                {
                    Intent profile = new Intent(Sign_up.this, Profile.class);
                    startActivity(profile);
                }

            }
        },TIMMER);

    }
}
