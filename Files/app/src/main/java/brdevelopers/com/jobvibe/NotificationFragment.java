package brdevelopers.com.jobvibe;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
    private ImageView iv_nojob;
    private ListView listView;
    private RelativeLayout relativeNotify;
    int mLastFirstVisibleItem=0;
    int mLastVisibleItemCount=0;
    private Animation animShow, animHide;

    private RequestQueue mRequestQueue;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_notification,container,false);

        progressBar=view.findViewById(R.id.progressbar);
        iv_nojob=view.findViewById(R.id.NotificationJob);
        listView=view.findViewById(R.id.notifyjob);
        relativeNotify=view.findViewById(R.id.RL__notify);
        initAnimation();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DBManager dbupdate=new DBManager(getActivity());

                boolean bol=dbupdate.updateNotificationView(listjob.get(position).getJbid(),Home.canemail,"1");
                AlertDialog.Builder report=new AlertDialog.Builder(getActivity());
                View reportView=getLayoutInflater().inflate(R.layout.notify_dialog,null);
                report.setView(reportView);
                TextView okay=(TextView) reportView.findViewById(R.id.TV_ok);
                final AlertDialog dialog = report.create();


                okay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.d("logcheck","Button is clicked");
                        dialog.dismiss();
                    }
                });

                dialog.show();
                arrayList=new ArrayList<>();
                arrayList=dbupdate.getNotificationData(Home.canemail);
                checkAndProceed();
            }
        });



        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mLastFirstVisibleItem > firstVisibleItem) {
//                    Home.layoutbottom.setVisibility(View.VISIBLE);
                    Log.e("logcheck", "scrolling upmh");
                } else if (mLastFirstVisibleItem < firstVisibleItem) {
                    Home.layoutbottom.startAnimation(animHide);
                    Home.layoutbottom.setVisibility(View.GONE);
                    Log.e("logcheck", "scrolling down");
                } else if (mLastVisibleItemCount < visibleItemCount) {
//                    Home.layoutbottom.setVisibility(View.GONE);
                    Log.e("logcheck", "scrolling downim");
                } else if (mLastVisibleItemCount > visibleItemCount) {
                    Home.layoutbottom.startAnimation(animShow);
                    Home.layoutbottom.setVisibility(View.VISIBLE);
                    Log.e("logcheck", "scrolling up");
                }
                mLastFirstVisibleItem = firstVisibleItem;
                mLastVisibleItemCount = visibleItemCount;
            }

            public void onScrollStateChanged(AbsListView listView, int scrollState) {
            }
        });

        // Instantiate the cache
        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap

// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

// Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);

// Start the queue
        mRequestQueue.start();

        DBManager dbManager = new DBManager(getActivity());
        arrayList = dbManager.getNotificationData(Home.canemail);
        checkAndProceed();
        return view;
    }
        public void checkAndProceed() {
            if (Util.isNetworkConnected(getActivity())) {


                listjob = new ArrayList<>();
                if (arrayList != null) {
                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                    iv_nojob.setVisibility(View.GONE);
                    for (JobActivity jobActivity : arrayList) {
                        loadNotifyJob(jobActivity.getJobid());
                    }

                } else {
                    progressBar.setVisibility(View.GONE);
                    iv_nojob.setVisibility(View.VISIBLE);
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

        }

    private void initAnimation()
    {
        animShow = AnimationUtils.loadAnimation( getActivity(), R.anim.view_show);
        animHide = AnimationUtils.loadAnimation( getActivity(), R.anim.view_hide);
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

                    String jobid=jobobject.getString("jid");
                    String jtitle=jobobject.getString("jobtitle");
                    String jcname=jobobject.getString("cname");

                    Job_details job_details=new Job_details();
                    job_details.setJbid(jobid);
                    job_details.setJbtitle(jtitle);
                    job_details.setJbcompnayname(jcname);


                    listjob.add(job_details);

                    listView.setAdapter(new NotificationJob());
                    progressBar.setVisibility(View.GONE);
                    relativeNotify.setBackgroundColor(Color.argb(182,231,229,231));
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
            LinearLayout linerjob_notify=v.findViewById(R.id.LL_jobnotify);


            for(JobActivity jobActivity: arrayList){
                if(jobActivity.getJobid().equals(listjob.get(position).getJbid()) && jobActivity.getViewedjob().equals("1")){
                    linerjob_notify.setBackgroundColor(Color.WHITE);
                }
                else if(jobActivity.getJobid().equals(listjob.get(position).getJbid()) && jobActivity.getViewedjob().equals("0"))
                    linerjob_notify.setBackgroundColor(Color.rgb(231, 229, 231));

            }

            jtitle.setText(listjob.get(position).getJbtitle());
            jcompany.setText(listjob.get(position).getJbcompnayname());


            return v;
        }
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
                DBManager dbManager = new DBManager(getActivity());
                arrayList = dbManager.getNotificationData(Home.canemail);
                checkAndProceed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
