package brdevelopers.com.jobvibe;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import java.util.List;
import java.util.Map;


public class Viewed_Fragment extends Fragment {

    private List<JobActivity> arrayList=null;
    private List<Job_details> listjob;
    private String allJob = "http://103.230.103.142/jobportalapp/job.asmx/GetJobDetails";
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private static RecyclerAdapter recyclerAdapter;
    private TextView tv_nojob;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_viewed,container,false);
        recyclerView = view.findViewById(R.id.RV_job);
        progressBar=view.findViewById(R.id.progressbar);
        tv_nojob=view.findViewById(R.id.TV_nojob);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        DBManager dbManager=new DBManager(getActivity());
        arrayList=dbManager.getViewedData(Home.canemail);

        listjob=new ArrayList<>();
        if(arrayList!=null){

            tv_nojob.setVisibility(View.GONE);
            for (JobActivity jobActivity : arrayList) {
                loadalViewedJob(jobActivity.getJobid());
            }

        }
        else {
            progressBar.setVisibility(View.GONE);
            tv_nojob.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private void loadalViewedJob(final String jobid) {

        StringRequest stringRequest=new StringRequest(Request.Method.POST, allJob, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("LogCheck",response);
                progressBar.setVisibility(View.VISIBLE);
                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                try {
                    JSONObject jsonObject=new JSONObject(response);

                        JSONObject jobobject=jsonObject.getJSONObject("Jobdetails");

                        String jid=jobobject.getString("jid");
                        String jtitle=jobobject.getString("jobtitle");
                        String jlocation=jobobject.getString("location");
                        String jldapply=jobobject.getString("ldapply");
                        String jcname=jobobject.getString("cname");
                        String jurl=jobobject.getString("url");
                        String jsalary=jobobject.getString("salary");
                        String jbca=jobobject.getString("bca");
                        String jmca=jobobject.getString("mca");
                        String jcse=jobobject.getString("cse");
                        String jit=jobobject.getString("it");
                        String jee=jobobject.getString("ee");
                        String jece=jobobject.getString("ece");
                        String jcivil=jobobject.getString("civil");
                        String jmba=jobobject.getString("mba");
                        String jasp=jobobject.getString("asp");
                        String jphp=jobobject.getString("php");
                        String jjava=jobobject.getString("java");
                        String jios=jobobject.getString("ios");
                        String jandroid=jobobject.getString("android");
                        String jdbms=jobobject.getString("dbms");
                        String jwrittentest=jobobject.getString("writtentest");
                        String jwalkin=jobobject.getString("walkin");
                        String jonline=jobobject.getString("online");
                        String jdescription=jobobject.getString("jobdescription");
                        String jcompanyprofile=jobobject.getString("companyprofile");
                        String jemail=jobobject.getString("email");


                        Job_details job_details=new Job_details();
                        job_details.setJbid(jid);
                        job_details.setJbtitle(jtitle);
                        job_details.setJblocation(jlocation);
                        job_details.setJbldapply(jldapply);
                        job_details.setJbcompnayname(jcname);
                        job_details.setJburl(jurl);
                        job_details.setJbsalary(jsalary);
                        job_details.setJbbca(jbca);
                        job_details.setJbmca(jmca);
                        job_details.setJbcse(jcse);
                        job_details.setJbit(jit);
                        job_details.setJbee(jee);
                        job_details.setJbece(jece);
                        job_details.setJbcivil(jcivil);
                        job_details.setJbmba(jmba);
                        job_details.setJbasp(jasp);
                        job_details.setJbphp(jphp);
                        job_details.setJbjava(jjava);
                        job_details.setJbios(jios);
                        job_details.setJbandroid(jandroid);
                        job_details.setJbdbms(jdbms);
                        job_details.setJbwrittentest(jwrittentest);
                        job_details.setJbwalking(jwalkin);
                        job_details.setJbonline(jonline);
                        job_details.setJbdescription(jdescription);
                        job_details.setJbcompanyprofile(jcompanyprofile);
                        job_details.setJbemail(jemail);

                        listjob.add(job_details);

                    recyclerAdapter=new RecyclerAdapter(getActivity(),listjob);
                    recyclerView.setAdapter(recyclerAdapter);
                    progressBar.setVisibility(View.GONE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("LogCheck",""+e);
                    progressBar.setVisibility(View.GONE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("LogCheck",""+error);
                        Toast.makeText(getActivity(), ""+error, Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String,String> jobHash=new HashMap<>();
                jobHash.put("jobid",jobid);

                return jobHash;
            }

        };

        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

}
