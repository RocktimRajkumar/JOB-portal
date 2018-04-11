package brdevelopers.com.jobvibe;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    private List<JobActivity> arrayList=null;
    private List<Job_details> listjob;
    private String allJob = "http://103.230.103.142/jobportalapp/job.asmx/GetJobDetails";
    private ProgressBar progressBar;
    private TextView tv_nojob;
    private ListView listView;

    private RequestQueue mRequestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_notification,container,false);

        progressBar=view.findViewById(R.id.progressbar);
        tv_nojob=view.findViewById(R.id.NotificationJob);
        listView=view.findViewById(R.id.notifyjob);

        // Instantiate the cache
        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap

// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

// Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);

// Start the queue
        mRequestQueue.start();

        if (Util.isNetworkConnected(getActivity())) {

            DBManager dbManager=new DBManager(getActivity());
            arrayList=dbManager.getNotificationData(Home.canemail);

            listjob=new ArrayList<>();
            if(arrayList!=null){
                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                tv_nojob.setVisibility(View.GONE);
                for (JobActivity jobActivity : arrayList) {
                    loadNotifyJob(jobActivity.getJobid());
                }

            }
            else {
                progressBar.setVisibility(View.GONE);
                tv_nojob.setVisibility(View.VISIBLE);
            }

        } else {
            Toast toast = new Toast(getActivity());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, 0);

            LayoutInflater inf = getActivity().getLayoutInflater();

            View layoutview = inf.inflate(R.layout.custom_toast, (ViewGroup) getActivity().findViewById(R.id.CustomToast_Parent));
            TextView tf = layoutview.findViewById(R.id.CustomToast);
            tf.setText("No Internet Connection " + Html.fromHtml("&#9995;"));
            toast.setView(layoutview);
            toast.show();

            progressBar.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }


        return view;
    }


    private void loadNotifyJob(final String jobid) {

        StringRequest stringRequest=new StringRequest(Request.Method.POST, allJob, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("LogCheck",response);
                progressBar.setVisibility(View.VISIBLE);
                if(getActivity()!=null)
                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                try {
                    JSONObject jsonObject=new JSONObject(response);

                    JSONObject jobobject=jsonObject.getJSONObject("Jobdetails");

                    String jtitle=jobobject.getString("jobtitle");
                    String jcname=jobobject.getString("cname");

                    Job_details job_details=new Job_details();
                    job_details.setJbtitle(jtitle);
                    job_details.setJbcompnayname(jcname);


                    listjob.add(job_details);

                    listView.setAdapter(new NotificationJob());
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

//        Volley.newRequestQueue(getActivity()).add(stringRequest);
        mRequestQueue.add(stringRequest);
    }

    public class NotificationJob extends BaseAdapter{

        @Override
        public int getCount() {
            return listjob.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inf=getLayoutInflater();
            View v=inf.inflate(R.layout.notifyrow,parent,false);
            TextView jtitle=v.findViewById(R.id.jobtitle);
            TextView jcompany=v.findViewById(R.id.jobcompany);
            jtitle.setText(listjob.get(position).getJbtitle());
            jcompany.setText(listjob.get(position).getJbcompnayname());

            return v;
        }
    }
}
