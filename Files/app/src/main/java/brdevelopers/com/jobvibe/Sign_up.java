package brdevelopers.com.jobvibe;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by End User on 27-03-2018.
 */

public class Sign_up extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
    }

    public void openProfile(View view)
    {
        Intent profile=new Intent(Sign_up.this,Profile.class);
        startActivity(profile);
    }
}
