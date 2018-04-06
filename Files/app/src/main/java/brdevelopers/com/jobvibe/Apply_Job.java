package brdevelopers.com.jobvibe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class Apply_Job extends AppCompatActivity {

    TextView jobtitle,joblocation,lastdate,cname,url,salary,jobdescription,cprofile,email,selection,eligibility,preferedskill;
    String asp,php,java,ios,android,dbms;
    String bca,mca,cse,it,ee,ece,civil,mba;
    String written,walkin,online;
    String jobid=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply__job);

        jobtitle=findViewById(R.id.TV_jobtitle);
        joblocation=findViewById(R.id.TV_location);
        lastdate=findViewById(R.id.TV_date);
        cname=findViewById(R.id.TV_companyname);
        url=findViewById(R.id.TV_url);
        salary=findViewById(R.id.TV_salary);
        jobdescription=findViewById(R.id.TV_about);
        cprofile=findViewById(R.id.TV_cprofile);
        email=findViewById(R.id.TV_email);
        selection=findViewById(R.id.TV_selection);
        eligibility=findViewById(R.id.TV_eligible);
        preferedskill=findViewById(R.id.TV_skill);

        jobid=getIntent().getStringExtra("jobid");
        jobtitle.setText(getIntent().getStringExtra("jobtitle"));
        joblocation.setText(getIntent().getStringExtra("location"));
        lastdate.setText(getIntent().getStringExtra("lastdate"));
        cname.setText(getIntent().getStringExtra("cname"));
        url.setText(getIntent().getStringExtra("url"));
        salary.setText("₹"+getIntent().getStringExtra("salary"));
        jobdescription.setText(getIntent().getStringExtra("jobdescription"));
        cprofile.setText(getIntent().getStringExtra("cprofile"));
        email.setText(getIntent().getStringExtra("email"));

        Log.d("logcheck",jobid);

        bca=getIntent().getStringExtra("bca");
        mca=getIntent().getStringExtra("mca");
        cse=getIntent().getStringExtra("cse");
        it=getIntent().getStringExtra("it");
        ee=getIntent().getStringExtra("ee");
        ece=getIntent().getStringExtra("ece");
        civil=getIntent().getStringExtra("civil");
        mba=getIntent().getStringExtra("mba");

        asp=getIntent().getStringExtra("asp");
        php=getIntent().getStringExtra("php");
        java=getIntent().getStringExtra("java");
        ios=getIntent().getStringExtra("ios");
        android=getIntent().getStringExtra("android");
        dbms=getIntent().getStringExtra("dbms");

        written=getIntent().getStringExtra("writtentest");
        walkin=getIntent().getStringExtra("walkin");
        online=getIntent().getStringExtra("online");


        ArrayList<String>selectionList=new ArrayList<>();

        if(!written.equals(""))
            selectionList.add(written);
        if(!walkin.equals(""))
            selectionList.add(walkin);
        if(!online.equals(""))
            selectionList.add(online);

        String selectioncomma= TextUtils.join(",",selectionList);

        selection.setText(selectioncomma);


        ArrayList<String>eligible=new ArrayList<>();

        if(!bca.equals(""))
            eligible.add(bca);
        if(!mca.equals(""))
            eligible.add(mca);
        if(!cse.equals(""))
            eligible.add(cse);
        if(!it.equals(""))
            eligible.add(it);
        if(!ee.equals(""))
            eligible.add(ee);
        if(!ece.equals(""))
            eligible.add(ece);
        if(!civil.equals(""))
            eligible.add(civil);
        if(!mba.equals(""))
            eligible.add(mba);

        String eligiblecomma= TextUtils.join(",",eligible);

        eligibility.setText(eligiblecomma);



        ArrayList<String>skill=new ArrayList<>();

        if(!asp.equals(""))
            skill.add(asp);
        if(!php.equals(""))
            skill.add(php);
        if(!java.equals(""))
            skill.add(java);
        if(!ios.equals(""))
            skill.add(ios);
        if(!android.equals(""))
            skill.add(android);
        if(!dbms.equals(""))
            skill.add(dbms);

        String skillcomma= TextUtils.join(",",skill);

        preferedskill.setText(skillcomma);


    }
}
