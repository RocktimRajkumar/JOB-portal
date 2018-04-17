package brdevelopers.com.jobvibe;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Profile extends AppCompatActivity implements View.OnClickListener{

    public static TextView tv_personal,tv_education;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        tv_personal=findViewById(R.id.TV_personal);
        tv_education=findViewById(R.id.TV_education);
        tv_personal.setOnClickListener(this);
        tv_education.setOnClickListener(this);
        tv_personal.setBackgroundColor(Color.rgb(255,87,34));
        tv_personal.setTextColor(Color.rgb(255, 255, 255));
        loadFragmentProfile(new Profile_personal());

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.TV_personal)
        {

            tv_personal.setBackgroundColor(Color.rgb(255,87,34));
            tv_personal.setTextColor(Color.rgb(255, 255, 255));
            tv_education.setBackgroundColor(Color.rgb(255, 255, 255));
            tv_education.setTextColor(Color.rgb(0,0,0));
            loadFragmentProfile(new Profile_personal());
        }
        else if(v.getId()==R.id.TV_education)
        {

            tv_education.setBackgroundColor(Color.rgb(255,87,34));
            tv_education.setTextColor(Color.rgb(255, 255, 255));
            tv_personal.setBackgroundColor(Color.rgb(255, 255, 255));
            tv_personal.setTextColor(Color.rgb(0,0,0));
            loadFragmentProfile(new Profile_Educaion());
        }

    }

    private void loadFragmentProfile(Fragment fragment) {

        String email=getIntent().getStringExtra("email");
        String password=getIntent().getStringExtra("password");
        //Sending email and password to Fragments
        Bundle bundle=new Bundle();
        bundle.putString("email",email);
        bundle.putString("password",password);
        fragment.setArguments(bundle);
        FragmentManager fm=getFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.FL_profile,fragment);
        ft.commit();

    }


}
