package brdevelopers.com.jobvibe;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class EditActivity extends AppCompatActivity implements View.OnClickListener{
    TextView tv_personal,tv_education;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        tv_personal=findViewById(R.id.TV_editPersonal);
        tv_education=findViewById(R.id.TV_editEducation);
        tv_personal.setOnClickListener(this);
        tv_education.setOnClickListener(this);
        tv_personal.setBackgroundColor(Color.rgb(255,87,34));
        tv_personal.setTextColor(Color.rgb(255, 255, 255));
        loadFragmentEdit(new Edit_PersonalFragment());

    }
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.TV_editPersonal)
        {
            tv_personal.setBackgroundColor(Color.rgb(255,87,34));
            tv_personal.setTextColor(Color.rgb(255, 255, 255));
            tv_education.setBackgroundColor(Color.rgb(255, 255, 255));
            tv_education.setTextColor(Color.rgb(0,0,0));
            loadFragmentEdit(new Edit_PersonalFragment());

        }
        else if(view.getId()==R.id.TV_editEducation)
        {
            tv_education.setBackgroundColor(Color.rgb(255,87,34));
            tv_education.setTextColor(Color.rgb(255, 255, 255));
            tv_personal.setBackgroundColor(Color.rgb(255, 255, 255));
            tv_personal.setTextColor(Color.rgb(0,0,0));
            loadFragmentEdit(new Edit_EducationFragment());

        }
    }

    private void loadFragmentEdit(Fragment fragment) {
        FragmentManager fm=getFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.FL_edit,fragment);
        ft.commit();

    }


}