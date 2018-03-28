package brdevelopers.com.jobvibe;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by End User on 27-03-2018.
 */

public class Sign_up extends AppCompatActivity implements TextWatcher{

    TextInputLayout til_password,til_cpassword;
    EditText et_password,et_cpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        til_password=findViewById(R.id.TIL_password);
        til_cpassword=findViewById(R.id.TIL_cpassword);
        et_password=findViewById(R.id.ET_password);
        et_cpassword=findViewById(R.id.ET_cpassword);
        et_password.addTextChangedListener(this);
        et_cpassword.addTextChangedListener(this);
    }

    public void openProfile(View view)
    {
        Intent profile=new Intent(Sign_up.this,Profile.class);
        startActivity(profile);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        if(et_password.getText().length()>0){
            til_password.setPasswordVisibilityToggleEnabled(true);
        }
        if(et_cpassword.getText().length()>0){
            til_cpassword.setPasswordVisibilityToggleEnabled(true);
        }
        if(et_password.getText().length()==0)
            til_password.setPasswordVisibilityToggleEnabled(false);
        if(et_cpassword.getText().length()==0)
            til_cpassword.setPasswordVisibilityToggleEnabled(false);
    }
}
