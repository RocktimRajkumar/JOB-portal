package brdevelopers.com.jobvibe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class Login extends AppCompatActivity {

   EditText et_email, et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        et_email=findViewById(R.id.TIL_email);
        et_password=findViewById(R.id.TIL_password);
    }
}
