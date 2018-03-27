package brdevelopers.com.jobvibe;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Profile extends AppCompatActivity {

    TextView tv_personal,tv_education;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        tv_personal=findViewById(R.id.TV_personal);
        tv_education=findViewById(R.id.TV_education);
        loadFragmentProfile(new Profile_personal());
    }

    public void loadProfile(View view)
    {
        if(view.getId()==R.id.TV_personal)
        {
            loadFragmentProfile(new Profile_personal());
        }
        else if(view.getId()==R.id.TV_education)
        {

        }
    }

    private void loadFragmentProfile(Fragment fragment) {

        FragmentManager fm=getFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.FL_profile,fragment);
        ft.commit();

    }
}
