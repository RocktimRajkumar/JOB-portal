package brdevelopers.com.jobvibe;


import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


public class Applied_Fragment extends Fragment {

    private String allJob = "http://103.230.103.142/jobportalapp/job.asmx/JobSearch";
    private String getresponse="http://103.230.103.142/jobportalapp/job.asmx/GetResponse";
    private String idjob="http://103.230.103.142/jobportalapp/job.asmx/GetJobDetails";
    private ProgressBar progressBar;
    private List<Job_details> list;
    private HashSet<String> companyemail = new HashSet<>();
    private String cemail;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private ImageView iv_nojob;

    private RequestQueue mRequestQueue;

    private int scroll=0;

    private Animation animShow, animHide;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_applied_,container,false);
        recyclerView=view.findViewById(R.id.RV_job);
        progressBar = view.findViewById(R.id.progressbar);
        iv_nojob=view.findViewById(R.id.IV_nojob);



// Instantiate the cache
        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap

// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

// Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);

// Start the queue
        mRequestQueue.start();


        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        initAnimation();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy>0) {
                    if(scroll==0) {
                        scroll=1;
                        Home.layoutbottom.startAnimation(animHide);
                        Home.layoutbottom.setVisibility(View.GONE);
                    }
                }
                else if(dy<0){
                    if(scroll==1) {
                        scroll=0;
                        Home.layoutbottom.startAnimation(animShow);
                        Home.layoutbottom.setVisibility(View.VISIBLE);
                    }

                }
            }
        });

        cemail=Home.canemail;

        iv_nojob.setVisibility(View.VISIBLE);

        if(Util.isNetworkConnected(getActivity())) {
//            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            loadAlljob();

        }
        else{
            Toast toast=new Toast(getActivity());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM|Gravity.FILL_HORIZONTAL,0,0);

            LayoutInflater inf=getActivity().getLayoutInflater();

            View layoutview=inf.inflate(R.layout.custom_toast,(ViewGroup)getActivity().findViewById(R.id.CustomToast_Parent));
            TextView tf=layoutview.findViewById(R.id.CustomToast);
            tf.setText("No Internet Connection "+ Html.fromHtml("&#9995;"));
            toast.setView(layoutview);
            toast.show();
            progressBar.setVisibility(View.GONE);
//            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

        return view;
    }

    private void initAnimation()
    {
        animShow = AnimationUtils.loadAnimation( getActivity(), R.anim.view_show);
        animHide = AnimationUtils.loadAnimation( getActivity(), R.anim.view_hide);
    }

    private void loadAlljob() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, allJob, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("LogCheck", response);


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("JobList");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jobobject = jsonArray.getJSONObject(i);

                        String cemail = jobobject.getString("email");

                        companyemail.add(cemail.toLowerCase());

                    }

                    list = new ArrayList<>();

                    for (String comemail : companyemail) {
                        getAllResponse(comemail);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("LogCheck", "" + e);
                    progressBar.setVisibility(View.GONE);
//                    if(getActivity()!=null)
//                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("LogCheck", "" + error);
                        Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
//                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> jobHash = new HashMap<>();
                jobHash.put("location", "");
                jobHash.put("skill", "");
                jobHash.put("course", "");

                return jobHash;
            }

        };

//        Volley.newRequestQueue(getActivity()).add(stringRequest);
        mRequestQueue.add(stringRequest);

    }

    private void getAllResponse(final String comemail) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, getresponse, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("LogCheck", response);

                progressBar.setVisibility(View.VISIBLE);
//                if(getActivity()!=null)
//                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("ResponseList");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject resobject = jsonArray.getJSONObject(i);

                        String resid=resobject.getString("rid");
                        String canemail=resobject.getString("cemail");
                        String jobid=resobject.getString("jid");

                        if(canemail.equals(cemail)) {
                            jobidjob(jobid);

                        }

                    }


                    progressBar.setVisibility(View.GONE);
//                    if(getActivity()!=null)
//                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("LogCheck", "" + e);
                    progressBar.setVisibility(View.GONE);
//                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("LogCheck", "" + error);
                        Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
//                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> jobHash = new HashMap<>();
                jobHash.put("employeemail", comemail);

                return jobHash;
            }

        };

//        Volley.newRequestQueue(getActivity()).add(stringRequest);
        mRequestQueue.add(stringRequest);
    }


    private void jobidjob(final String jobid) {

        progressBar.setVisibility(View.VISIBLE);
//        if(getActivity()!=null)
//        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, idjob, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("LogCheck",response);

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

                        list.add(job_details);


                        recyclerAdapter = new RecyclerAdapter(getActivity(), list);
                        recyclerAdapter.setApplyvalue(1);
                        recyclerView.setAdapter(recyclerAdapter);

                        iv_nojob.setVisibility(View.GONE);

//                    if(getActivity()!=null)
//                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("LogCheck",""+e);
                    progressBar.setVisibility(View.GONE);
//                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("LogCheck",""+error);
                        Toast.makeText(getActivity(), ""+error, Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
//                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String,String> jobHash=new HashMap<>();
                jobHash.put("jobid",jobid);

                return jobHash;
            }

        };

//        Volley.newRequestQueue(getActivity()).add(stringRequest);
        mRequestQueue.add(stringRequest);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:

                progressBar.setVisibility(View.VISIBLE);
                if(Util.isNetworkConnected(getActivity())) {
//                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    loadAlljob();
                }
                else{
                    Toast toast=new Toast(getActivity());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM|Gravity.FILL_HORIZONTAL,0,0);

                    LayoutInflater inf=getActivity().getLayoutInflater();

                    View layoutview=inf.inflate(R.layout.custom_toast,(ViewGroup)getActivity().findViewById(R.id.CustomToast_Parent));
                    TextView tf=layoutview.findViewById(R.id.CustomToast);
                    tf.setText("No Internet Connection "+ Html.fromHtml("&#9995;"));
                    toast.setView(layoutview);
                    toast.show();
                    progressBar.setVisibility(View.GONE);
//                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
