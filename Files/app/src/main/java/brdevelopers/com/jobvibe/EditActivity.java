package brdevelopers.com.jobvibe;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class EditActivity extends AppCompatActivity {
    TextView tv_personal,tv_education;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        tv_personal=findViewById(R.id.TV_profileName);
        tv_education=findViewById(R.id.TV_profileEmail);
        loadFragmentEdit(new Edit_PersonalFragment());

    }
    public void loadEdit(View view)
    {
        if(view.getId()==R.id.TV_editPersonal)
        {
            loadFragmentEdit(new Edit_PersonalFragment());
        }
        else if(view.getId()==R.id.TV_editEducation)
        {
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