package brdevelopers.com.jobvibe;


import android.app.AlertDialog;;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.github.clans.fab.FloatingActionButton;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class MatchedFragment extends Fragment implements View.OnClickListener {

    private String allJob = "http://103.230.103.142/jobportalapp/job.asmx/JobSearch";
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private ProgressBar progressBar;
    private FloatingActionButton floatlocation, floatskill, floatcompany;
    private  List<Job_details> list;
    private HashSet<String> jblocation = new HashSet<>();
    private HashSet<String> jbcompany = new HashSet<>();
    private HashSet<String> jbskill = new HashSet<>();

    private static String course;

    private RequestQueue mRequestQueue;
    private SearchView searchView;
    boolean[] checkedItemscom;
    boolean[] checkedItemsskill;
    boolean[] checkedItemsloc;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.matched_fragment, container, false);
        recyclerView = view.findViewById(R.id.RV_job);
        progressBar = view.findViewById(R.id.progressbar);
        floatlocation = view.findViewById(R.id.location);
        floatskill = view.findViewById(R.id.skill);
        floatcompany = view.findViewById(R.id.company);



// Instantiate the cache
        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap

// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

// Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);

// Start the queue
        mRequestQueue.start();


        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);


        String degree=Home.getdegree;
        String FOS=Home.getfos;

        floatlocation.setOnClickListener(this);
        floatskill.setOnClickListener(this);
        floatcompany.setOnClickListener(this);


        if(degree.equals("B.TECH"))
        {
            degree=degree+"("+FOS+")";
        }

        course=degree;


        if (Util.isNetworkConnected(getActivity())) {
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                loadAlljob();
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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.home, menu);
        inflater.inflate(R.menu.searchfile, menu);
        final MenuItem myactionmenu=menu.findItem(R.id.search);
        searchView=(SearchView)myactionmenu.getActionView();

        searchView.setQueryHint(Html.fromHtml("<font color = #ffffff>" + getResources().getString(R.string.search_title) + "</font>"));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if(searchView.isIconified())
                    searchView.setIconified(true);

                myactionmenu.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                  searchFilter(newText);
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:

                progressBar.setVisibility(View.VISIBLE);

                if (Util.isNetworkConnected(getActivity())) {
                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    loadAlljob();
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

                        String jlocation = jobobject.getString("location");

                        jblocation.add(jlocation.toLowerCase());

                    }

                    checkedItemsloc = new boolean[jblocation.size()];
                    int i = 0;
                    for (String s : jblocation) {
                        checkedItemsloc[i] = false;
                        i++;
                    }

                    list = new ArrayList<>();

                    for (String loc : jblocation) {
                        loadCourseJob(list, loc);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("LogCheck", "" + e);
                    progressBar.setVisibility(View.GONE);
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("LogCheck", "" + error);
                        Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
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

    private void loadCourseJob(final List<Job_details> list, final String loc) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, allJob, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("LogCheck", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("JobList");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jobobject = jsonArray.getJSONObject(i);

                        String jid = jobobject.getString("jid");
                        String jtitle = jobobject.getString("jobtitle");
                        String jlocation = jobobject.getString("location");
                        String jldapply = jobobject.getString("ldapply");
                        String jcname = jobobject.getString("cname");
                        String jurl = jobobject.getString("url");
                        String jsalary = jobobject.getString("salary");
                        String jbca = jobobject.getString("bca");
                        String jmca = jobobject.getString("mca");
                        String jcse = jobobject.getString("cse");
                        String jit = jobobject.getString("it");
                        String jee = jobobject.getString("ee");
                        String jece = jobobject.getString("ece");
                        String jcivil = jobobject.getString("civil");
                        String jmba = jobobject.getString("mba");
                        String jasp = jobobject.getString("asp");
                        String jphp = jobobject.getString("php");
                        String jjava = jobobject.getString("java");
                        String jios = jobobject.getString("ios");
                        String jandroid = jobobject.getString("android");
                        String jdbms = jobobject.getString("dbms");
                        String jwrittentest = jobobject.getString("writtentest");
                        String jwalkin = jobobject.getString("walkin");
                        String jonline = jobobject.getString("online");
                        String jdescription = jobobject.getString("jobdescription");
                        String jcompanyprofile = jobobject.getString("companyprofile");
                        String jemail = jobobject.getString("email");


                        Job_details job_details = new Job_details();
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

                        if (!jcname.isEmpty())
                            jbcompany.add(jcname.toLowerCase());

                        if(!jasp.isEmpty())
                            jbskill.add(jasp.toLowerCase());
                        if(!jphp.isEmpty())
                            jbskill.add(jphp.toLowerCase());
                        if(!jjava.isEmpty())
                            jbskill.add(jjava.toLowerCase());
                        if(!jios.isEmpty())
                             jbskill.add(jios.toLowerCase());
                        if(!jandroid.isEmpty())
                             jbskill.add(jandroid.toLowerCase());
                        if(!jdbms.isEmpty())
                            jbskill.add(jdbms.toLowerCase());


                    }

                    checkedItemscom = new boolean[jbcompany.size()];
                    int k = 0;
                    for (String s : jbcompany) {
                        checkedItemscom[k] = false;
                        k++;
                    }


                    checkedItemsskill = new boolean[jbskill.size()];
                    int l = 0;
                    for (String s : jbskill) {
                        checkedItemsskill[l] = false;
                        l++;
                    }

                    if(list!=null) {
                        recyclerAdapter = new RecyclerAdapter(getActivity(), list);
                        recyclerView.setAdapter(recyclerAdapter);
                    }
                    progressBar.setVisibility(View.GONE);
                    if(getActivity()!=null)
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("LogCheck", "" + e);
                    progressBar.setVisibility(View.GONE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("LogCheck", "" + error);
                        Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> jobHash = new HashMap<>();
                jobHash.put("location", loc);
                jobHash.put("skill", "");
                jobHash.put("course", course);

                return jobHash;
            }

        };

//        Volley.newRequestQueue(getActivity()).add(stringRequest);

        mRequestQueue.add(stringRequest);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.location) {

            showLocation();
        } else if (v.getId() == R.id.skill) {
            showSkill();
        } else if (v.getId() == R.id.company) {
            showCompany();
        }
    }

    private void showCompany() {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Company");

        // add a checkbox list
        final String[] company = jbcompany.toArray(new String[0]);
//        boolean[] checkedItems = new boolean[jbcompany.size()];

        final List<String> newCompany = new ArrayList<>();

        builder.setMultiChoiceItems(company, checkedItemscom, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // user checked or unchecked a box
                if (isChecked)
                    newCompany.add(company[which]);
                else
                    newCompany.remove(company[which]);
            }
        });

        // add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked OK
                int i = 0;
                for (String s : jblocation) {
                    checkedItemsloc[i] = false;
                    i++;
                }
                i=0;
                for(String s:jbskill){
                    checkedItemsskill[i]=false;
                    i++;
                }
                if (newCompany.size() != 0)
                    loadNewCompanyJob(newCompany);

            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void showSkill() {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Skill");

        // add a checkbox list
        final String[] skill = jbskill.toArray(new String[0]);
//        final boolean[] checkedItems = new boolean[jbskill.size()];

        final List<String> newSkill = new ArrayList<>();

        builder.setMultiChoiceItems(skill, checkedItemsskill, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // user checked or unchecked a box
                if (isChecked)
                    newSkill.add(skill[which]);

                else
                    newSkill.remove(skill[which]);

            }
        });

        // add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked OK
                int i = 0;
                for (String s : jbcompany) {
                    checkedItemscom[i] = false;
                    i++;
                }
                i=0;
                for(String s:jblocation){
                    checkedItemsloc[i]=false;
                    i++;
                }
                if (newSkill.size() != 0)
                    loadNewSkillJob(newSkill);

            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void showLocation() {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Location");

        // add a checkbox list
        final String[] location = jblocation.toArray(new String[0]);


        final List<String> newLocation = new ArrayList<>();

        builder.setMultiChoiceItems(location, checkedItemsloc, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // user checked or unchecked a box
                if (isChecked)
                    newLocation.add(location[which]);
                else
                    newLocation.remove(location[which]);

            }
        });

        // add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked OK

                int i = 0;
                for (String s : jbcompany) {
                    checkedItemscom[i] = false;
                    i++;
                }
                i=0;
                for(String s:jbskill){
                    checkedItemsskill[i]=false;
                    i++;
                }

                if (newLocation.size() != 0)
                    loadNewLocationJob(newLocation);

            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void loadNewSkillJob(List<String> newSkilllist) {

        List<Job_details> skilllist = new ArrayList<>();

        for(String newSkill:newSkilllist){
            for(Job_details job_details:list){

                if(job_details.getJbasp().equalsIgnoreCase(newSkill)){
                    skilllist.add(job_details);
                } else if (job_details.getJbphp().equalsIgnoreCase(newSkill)) {
                    skilllist.add(job_details);
                } else if (job_details.getJbjava().equalsIgnoreCase(newSkill)) {
                    skilllist.add(job_details);
                } else if (job_details.getJbios().equalsIgnoreCase(newSkill)) {
                    skilllist.add(job_details);
                } else if (job_details.getJbandroid().equalsIgnoreCase(newSkill)) {
                    skilllist.add(job_details);
                } else if (job_details.getJbdbms().equalsIgnoreCase(newSkill)) {
                    skilllist.add(job_details);
                }
            }
        }
        recyclerAdapter.filter(skilllist);

    }

    private void loadNewCompanyJob(List<String> newCompanylist) {

        List<Job_details> companylist = new ArrayList<>();

        for (String newCompany : newCompanylist) {
            for (Job_details job_details : list) {
                if (job_details.getJbcompnayname().equalsIgnoreCase(newCompany)) {
                    companylist.add(job_details);
                }
            }
        }
        recyclerAdapter.filter(companylist);

    }

    private void loadNewLocationJob(List<String> newLocation) {

        List<Job_details> locationlist = new ArrayList<>();

        for (String newLoc : newLocation) {
            for (Job_details job_details : list) {
                if (job_details.getJblocation().equalsIgnoreCase(newLoc)) {
                    locationlist.add(job_details);
                }
            }
        }
        recyclerAdapter.filter(locationlist);
    }


    public void searchFilter(String query)
    {
        query=query.toLowerCase();
        final List<Job_details> newlist=new ArrayList<>();

        for(Job_details job:list)
        {
            final String name=job.getJbtitle().toLowerCase();
            if(name.startsWith(query))
            {
                newlist.add(job);
            }
        }
        recyclerAdapter.filter(newlist);
    }

}
