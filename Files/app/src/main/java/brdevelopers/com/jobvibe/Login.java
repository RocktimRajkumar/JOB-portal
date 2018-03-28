package brdevelopers.com.jobvibe;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity implements TextWatcher{

   TextInputLayout til_password;
   EditText et_email, et_password;
   TextView tv_btnlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        et_email=findViewById(R.id.ET_email);
        et_password=findViewById(R.id.ET_password);
        tv_btnlogin=findViewById(R.id.TV_btnnext);
        til_password=findViewById(R.id.TIL_password);
        et_password.addTextChangedListener(this);
    }

    public void openSignup(View view)
    {
        Intent signup=new Intent(Login.this,Sign_up.class);
        startActivity(signup);
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
        else{
            til_password.setPasswordVisibilityToggleEnabled(false);
        }
    }
}
