package brdevelopers.com.jobvibe;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Recommended extends Fragment implements View.OnClickListener {

    private String allJob="http://103.230.103.142/jobportalapp/job.asmx/JobSearch";
    private RecyclerView recyclerView;
    private List<Job_details> list;
    private RecyclerAdapter recyclerAdapter;
    private ProgressBar progressBar;
    private FloatingActionButton floatlocation,floatskill,floatcompany;
    private HashSet<String> jblocation=new HashSet<>();
    private HashSet<String> jbcompany=new HashSet<>();
    private HashSet<String> jbskill=new HashSet<>();

    private SearchView searchView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.recommended,container,false);
        progressBar=view.findViewById(R.id.progressbar);
        recyclerView=view.findViewById(R.id.RV_job);
        floatlocation=view.findViewById(R.id.location);
        floatskill=view.findViewById(R.id.skill);
        floatcompany=view.findViewById(R.id.company);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        floatlocation.setOnClickListener(this);
        floatskill.setOnClickListener(this);
        floatcompany.setOnClickListener(this);

        if(Util.isNetworkConnected(getActivity())) {
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            loadalljob();
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

    private void loadalljob() {

        StringRequest stringRequest=new StringRequest(Request.Method.POST, allJob, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("LogCheck",response);
                list=new ArrayList<>();

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("JobList");

                    for(int i=0;i<jsonArray.length();i++)
                    {
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

                        jblocation.add(jlocation.toLowerCase());

                        if(!jcname.isEmpty())
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

                    recyclerAdapter=new RecyclerAdapter(getActivity(),list);
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
                jobHash.put("location","");
                jobHash.put("skill","");
                jobHash.put("course","");

                return jobHash;
            }

        };

        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.location){

            showLocation();
        }
        else if(v.getId()==R.id.skill){
            showSkill();
        }
        else if(v.getId()==R.id.company){
            showCompany();
        }
    }

    private void showCompany() {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Company");

        // add a checkbox list
        final String []company=jbcompany.toArray(new String[0]);
        boolean[] checkedItems = new boolean[jbcompany.size()];
        int i=0;
        for (String s:jbcompany) {
            checkedItems[i]=false;
            i++;
        }


        final List<String> newCompany=new ArrayList<>();

        builder.setMultiChoiceItems(company, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // user checked or unchecked a box
                if(isChecked)
                    newCompany.add(company[which]);
                else
                    newCompany.remove(which);
            }
        });

        // add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked OK

                if(newCompany.size()!=0)
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
        final String []skill=jbskill.toArray(new String[0]);
        final boolean[] checkedItems = new boolean[jbskill.size()];
        int i=0;
        for (String s:jbskill) {
            checkedItems[i]=false;
            i++;
        }


        final List<String> newSkill=new ArrayList<>();

        builder.setMultiChoiceItems(skill, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // user checked or unchecked a box
                if(isChecked)
                    newSkill.add(skill[which]);

                else
                    newSkill.remove(which);

            }
        });

        // add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked OK
                if(newSkill.size()!=0)
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
        final String []location=jblocation.toArray(new String[0]);
        final boolean[] checkedItems = new boolean[jblocation.size()];
        int i=0;
        for (String s:jblocation) {
            checkedItems[i]=false;
            i++;
        }

        final List<String> newLocation=new ArrayList<>();

        builder.setMultiChoiceItems(location, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // user checked or unchecked a box
                if(isChecked)
                    newLocation.add(location[which]);
                else
                    newLocation.remove(which);

            }
        });

        // add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked OK
                if(newLocation.size()!=0)
                    loadNewLocationJob(newLocation);


            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void loadNewSkillJob(List<String> newSkilllist) {

        List<Job_details> skilllist=new ArrayList<>();

        for(String newSkill:newSkilllist){
            for(Job_details job_details:list){

                if(job_details.getJbasp().equalsIgnoreCase(newSkill)){
                    skilllist.add(job_details);
                }
                else if(job_details.getJbphp().equalsIgnoreCase(newSkill)){
                    skilllist.add(job_details);
                }
                else if(job_details.getJbjava().equalsIgnoreCase(newSkill)){
                    skilllist.add(job_details);
                }
                else if(job_details.getJbios().equalsIgnoreCase(newSkill)){
                    skilllist.add(job_details);
                }
                else if(job_details.getJbandroid().equalsIgnoreCase(newSkill)){
                    skilllist.add(job_details);
                }
                else if(job_details.getJbdbms().equalsIgnoreCase(newSkill)){
                    skilllist.add(job_details);
                }
            }
        }
        recyclerAdapter.filter(skilllist);

    }

    private void loadNewCompanyJob(List<String> newCompanylist) {

        List<Job_details> companylist=new ArrayList<>();

        for(String newCompany:newCompanylist){
            for(Job_details job_details:list){
                if(job_details.getJbcompnayname().equalsIgnoreCase(newCompany)){
                    companylist.add(job_details);
                }
            }
        }
        recyclerAdapter.filter(companylist);

    }

    private void loadNewLocationJob(List<String> newLocation) {

        List<Job_details> locationlist=new ArrayList<>();

        for(String newLoc:newLocation){
            for(Job_details job_details:list){
                if(job_details.getJblocation().equalsIgnoreCase(newLoc)){
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
