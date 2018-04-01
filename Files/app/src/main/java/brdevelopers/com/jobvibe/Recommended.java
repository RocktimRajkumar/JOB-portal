package brdevelopers.com.jobvibe;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class Recommended extends Fragment {

    private String allJob="http://103.230.103.142/jobportalapp/job.asmx/JobSearch";
    private RecyclerView recyclerView;
    List<Job_details> list=new ArrayList<>();
    RecyclerAdapter recyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.recommended,container,false);
        recyclerView=view.findViewById(R.id.RV_job);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        Log.d("LogCheck","hi");
        loadalljob();

        return view;
    }

    private void loadalljob() {

        StringRequest stringRequest=new StringRequest(Request.Method.POST, allJob, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("LogCheck",response);

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("JobList");

                    for(int i=0;i<jsonArray.length();i++)
                    {
                        Log.d("LogCheck","inside forloop");
                        JSONObject jobobject=jsonArray.getJSONObject(i);

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
//                        String jcompanyprofile=jsonObject.getString("companyprofile");
//                        String jemail=jsonObject.getString("email");

                        Log.d("LogCheck",jtitle);

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
//                        job_details.setJbcompanyprofile(jcompanyprofile);
//                        job_details.setJbemail(jemail);

                        list.add(job_details);

                    }

                    recyclerAdapter=new RecyclerAdapter(getActivity(),list);
                    recyclerView.setAdapter(recyclerAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("LogCheck",""+e);
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("LogCheck",""+error);
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String,String> jobHash=new HashMap<>();
                jobHash.put("location","");
                jobHash.put("skill","");
                jobHash.put("course","");

                return jobHash;
            }

        };

        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }
}
