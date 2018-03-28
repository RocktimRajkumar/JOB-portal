package brdevelopers.com.jobvibe;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class SplashScreen extends AppCompatActivity {

    private static int TIMMER=3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent login=new Intent(SplashScreen.this,Login.class);
                startActivity(login);
                finish();
            }
        },TIMMER);


    }

}
