package brdevelopers.com.jobvibe;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity implements TextWatcher,View.OnClickListener{

   private TextInputLayout til_password,til_email;
   private EditText et_email, et_password;
   private TextView tv_btnlogin,tv_createnew;
   private int i_email=0,i_password=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        et_email=findViewById(R.id.ET_email);
        et_password=findViewById(R.id.ET_password);
        tv_btnlogin=findViewById(R.id.TV_loginbutton);
        til_password=findViewById(R.id.TIL_password);
        til_email=findViewById(R.id.TIL_email);
        tv_createnew=findViewById(R.id.crete_new);

        et_email.addTextChangedListener(this);
        et_password.addTextChangedListener(this);
        tv_btnlogin.setOnClickListener(this);
        tv_createnew.setOnClickListener(this);
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

        if(et_password.getText().length()==0) {
            til_password.setPasswordVisibilityToggleEnabled(false);
            i_password = 0;
            til_password.setError(null);
        }

    }

    @Override
    public void onClick(final View v) {

        if(v.getId()==R.id.TV_loginbutton) {
            final Float elevation = tv_btnlogin.getElevation();
            tv_btnlogin.setElevation(-elevation);

            int TIMMER = 200;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (i_email == 0 && et_email.getText().toString().length() == 0) {
                        et_email.requestFocus();
                        til_email.setError(getString(R.string.error_empty_field));
                        tv_btnlogin.setElevation(elevation);
                    } else if (i_email == 0) {
                        et_email.requestFocus();
                        tv_btnlogin.setElevation(elevation);
                    }

                    if (i_password == 0 && et_password.getText().toString().length() == 0) {
                        if (i_email == 0)
                            et_email.requestFocus();
                        else
                            et_password.requestFocus();
                        til_password.setError(getString(R.string.error_empty_field));
                        tv_btnlogin.setElevation(elevation);
                    } else if (i_password == 0) {
                        if (i_email == 0)
                            et_email.requestFocus();
                        else
                            et_password.requestFocus();
                        tv_btnlogin.setElevation(elevation);
                    }

                    if (i_password == 1 && i_email == 1) {
                        Intent profile = new Intent(Login.this, Sign_up.class);
                        startActivity(profile);
                    }
                }


            }, TIMMER);
        }
         else if(v.getId()==R.id.crete_new)
        {
            Intent profile = new Intent(Login.this, Sign_up.class);
            startActivity(profile);
        }

    }
}
