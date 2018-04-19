package brdevelopers.com.jobvibe;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Apply_Job extends AppCompatActivity {

    private TextView jobtitle,joblocation,lastdate,cname,url,salary,jobdescription,cprofile,email,selection,eligibility,preferedskill;
    private LinearLayout saved,apply;
    private String asp,php,java,ios,android,dbms;
    private String bca,mca,cse,it,ee,ece,civil,mba;
    private String written,walkin,online;
    private String jobid=null;
    private ImageView saveimg,iv_back;
    private ProgressBar progressBar;
    public String emailh=Home.canemail;
    public String name=Home.name;
    public String degree=Home.getdegree;
    public String fos=Home.getfos;
    private String applyjob="http://103.230.103.142/jobportalapp/job.asmx/ApplyJob";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply__job);
        progressBar=findViewById(R.id.progressbar);

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

        saved=findViewById(R.id.LL_save);
        iv_back=findViewById(R.id.IV_back);
        apply=findViewById(R.id.LL_apply);
        saveimg=findViewById(R.id.IV_save);

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

        //Storing data in sqlite database
        final DBManager db=new DBManager(this);
        Log.d("logcheck",Home.canemail);

        boolean bolviewed=db.isViewedExists(jobid,Home.canemail);
        if(bolviewed) {
            db.deleteViewed(jobid, Home.canemail);
            db.insertViewed(jobid, Home.canemail);
        }
        else
            db.insertViewed(jobid, Home.canemail);


        boolean imgcolor=db.isSavedExists(jobid,Home.canemail);
        if(imgcolor)
            saveimg.setImageResource(R.drawable.starblue);

        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean bolsaved = db.isSavedExists(jobid, Home.canemail);
                if (bolsaved) {
                    db.deleteSaved(jobid, Home.canemail);
                    View view =getLayoutInflater().inflate(R.layout.unsave_toast,(ViewGroup)findViewById(R.id.root));
                    Toast toast=new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(view);
                    toast.show();
                    saveimg.setImageResource(R.drawable.starwhite);
                } else {
                    db.insertData(jobid, Home.canemail);
                    View view =getLayoutInflater().inflate(R.layout.save_toast,(ViewGroup)findViewById(R.id.root));
                    Toast toast=new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(view);
                    toast.show();
                    saveimg.setImageResource(R.drawable.starblue);
                }
            }
        });


        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                applyJob();
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Apply_Job.this,Home.class);
                intent.putExtra("emailid",emailh);
                intent.putExtra("name",name);
                intent.putExtra("getdegree",degree);
                intent.putExtra("getfos",fos);
                startActivity(intent);
                overridePendingTransition(R.anim.topttobottom,R.anim.bottomtotop);
                finish();


            }
        });
    }

    private void applyJob() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, applyjob, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("LogCheck", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String success=jsonObject.getString("Sucess");
                    if(success.equals("1"))
                    {
                        DBManager db=new DBManager(Apply_Job.this);
                        db.insertNotifcation(jobid,Home.canemail,"0");

                        AlertDialog.Builder message= new AlertDialog.Builder(Apply_Job.this);

                        View v=getLayoutInflater().inflate(R.layout.success_layout,null);
                        message.setView(v);
                        TextView okay=(TextView) v.findViewById(R.id.TV_okay);
                        final AlertDialog dialog = message.create();


                        okay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Log.d("logcheck","Button is clicked");
                                dialog.dismiss();
                            }
                        });

                        dialog.show();

                    }
                    else{
                        Toast toast=new Toast(Apply_Job.this);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM|Gravity.FILL_HORIZONTAL,0,0);

                        LayoutInflater inf=getLayoutInflater();

                        View layoutview=inf.inflate(R.layout.custom_toast,(ViewGroup)findViewById(R.id.CustomToast_Parent));
                        TextView tf=layoutview.findViewById(R.id.CustomToast);
                        tf.setText("You haved already applied for this job. "+Html.fromHtml(" &#x1f604;"));
                        toast.setView(layoutview);
                        toast.show();
                    }

                    progressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("LogCheck", "" + e);
                    progressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("LogCheck", "" + error);
                        Toast.makeText(Apply_Job.this, "" + error, Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> jobHash = new HashMap<>();
                jobHash.put("cemail", Home.canemail);
                jobHash.put("eemail",email.getText().toString());
                jobHash.put("jobid",jobid);

                return jobHash;
            }

        };

        Volley.newRequestQueue(Apply_Job.this).add(stringRequest);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent intent=new Intent(Apply_Job.this,Home.class);
        intent.putExtra("emailid",emailh);
        intent.putExtra("name",name);
        intent.putExtra("getdegree",degree);
        intent.putExtra("getfos",fos);
        startActivity(intent);
        overridePendingTransition(R.anim.topttobottom,R.anim.bottomtotop);
        finish();
    }
}
