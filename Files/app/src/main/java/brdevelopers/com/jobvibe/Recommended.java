package brdevelopers.com.jobvibe;

import android.app.AlertDialog;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
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
    private ImageView nojob;
    private FloatingActionMenu floatingActionMenu;

    private SearchView searchView;

    boolean[] checkedItemscom;
    boolean[] checkedItemsskill;
    boolean[] checkedItemsloc;

    private int scroll=0;

    private Animation animShow, animHide;

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
        nojob=view.findViewById(R.id.IV_nojob);
        floatingActionMenu=view.findViewById(R.id.menu);

        initAnimation();

//        Hiding the bottom layout and floating button when scroll down and show when scroll up

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy>0) {
                    if(scroll==0) {
                        scroll=1;
                        Home.layoutbottom.startAnimation(animHide);
                        Home.layoutbottom.setVisibility(View.GONE);
                        floatingActionMenu.hideMenu(true);
                    }
                }
                else if(dy<0){
                    if(scroll==1) {
                        scroll=0;
                        Home.layoutbottom.startAnimation(animShow);
                        Home.layoutbottom.setVisibility(View.VISIBLE);
                        floatingActionMenu.showMenu(true);
                    }

                }
            }
        });

//        closing the floating menu when touch on Parent layout(Relative Layout)

        view.findViewById(R.id.recommendedRoot).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(floatingActionMenu.isOpened())
                    floatingActionMenu.close(true);
                return false;
            }
        });

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

    private void initAnimation()
    {
        animShow = AnimationUtils.loadAnimation( getActivity(), R.anim.view_show);
        animHide = AnimationUtils.loadAnimation( getActivity(), R.anim.view_hide);
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.home, menu);
        inflater.inflate(R.menu.searchfile, menu);
        final MenuItem myactionmenu=menu.findItem(R.id.search);
        searchView=(SearchView)myactionmenu.getActionView();


        searchView.setQueryHint(Html.fromHtml("<font color = #000>" + getResources().getString(R.string.search_title) + "</font>"));

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean bol=floatingActionMenu.isOpened();
                if(bol){
                    floatingActionMenu.close(true);
                }
                searchView.setBackgroundResource(R.drawable.searchview_bg);
                ((EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setTextColor(Color.BLACK);


            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                boolean bol=floatingActionMenu.isOpened();
                if(bol){
                    floatingActionMenu.close(true);
                }
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                menu.getItem(0).setVisible(true);

                boolean bol=floatingActionMenu.isOpened();
                if(bol){
                    floatingActionMenu.close(true);
                }

                if(searchView.isIconified())
                    searchView.setIconified(true);

                myactionmenu.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                boolean bol=floatingActionMenu.isOpened();
                if(bol){
                    floatingActionMenu.close(true);
                }
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

                    checkedItemsloc = new boolean[jblocation.size()];
                    int i = 0;
                    for (String s : jblocation) {
                        checkedItemsloc[i] = false;
                        i++;
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

                    recyclerAdapter=new RecyclerAdapter(getActivity(),list);
                    recyclerView.setAdapter(recyclerAdapter);
                    progressBar.setVisibility(View.GONE);
                    if(getActivity()!=null)
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
//        boolean[] checkedItems = new boolean[jbcompany.size()];
        final List<String> newCompany = new ArrayList<>();
        int a=0;
        for(boolean bolu:checkedItemscom){
            if(bolu)
                newCompany.add(company[a]);
            a++;
        }



        builder.setMultiChoiceItems(company, checkedItemscom, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // user checked or unchecked a box
                if(isChecked)
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

                boolean bol=floatingActionMenu.isOpened();
                if(bol){
                    floatingActionMenu.close(true);
                }

                if (newCompany.size() != 0) {
                    floatcompany.setLabelTextColor(Color.RED);
                    floatskill.setLabelTextColor(Color.WHITE);
                    floatlocation.setLabelTextColor(Color.WHITE);
                    loadNewCompanyJob(newCompany);
                }
                else {
                    floatcompany.setLabelTextColor(Color.WHITE);
                    recyclerAdapter.filter(list);
                    nojob.setVisibility(View.GONE);
                }

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
//        final boolean[] checkedItems = new boolean[jbskill.size()];
        final List<String> newSkill = new ArrayList<>();

        int a=0;
        for(boolean bolu:checkedItemsskill){
            if(bolu)
                newSkill.add(skill[a]);
            a++;
        }

        builder.setMultiChoiceItems(skill, checkedItemsskill, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // user checked or unchecked a box
                if(isChecked)
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

                boolean bol=floatingActionMenu.isOpened();
                if(bol){
                    floatingActionMenu.close(true);
                }

                if (newSkill.size() != 0) {
                    floatcompany.setLabelTextColor(Color.WHITE);
                    floatskill.setLabelTextColor(Color.RED);
                    floatlocation.setLabelTextColor(Color.WHITE);
                    loadNewSkillJob(newSkill);
                }
                else {
                    floatskill.setLabelTextColor(Color.WHITE);
                    recyclerAdapter.filter(list);
                    nojob.setVisibility(View.GONE);
                }

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
//        final boolean[] checkedItems = new boolean[jblocation.size()];
        final List<String> newLocation = new ArrayList<>();

        int a=0;
        for(boolean bolu:checkedItemsloc){
            if(bolu)
                newLocation.add(location[a]);
            a++;
        }

        builder.setMultiChoiceItems(location, checkedItemsloc, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // user checked or unchecked a box
                if(isChecked)
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


                boolean bol=floatingActionMenu.isOpened();
                if(bol){
                    floatingActionMenu.close(true);
                }

                if (newLocation.size() != 0) {
                    floatcompany.setLabelTextColor(Color.WHITE);
                    floatskill.setLabelTextColor(Color.WHITE);
                    floatlocation.setLabelTextColor(Color.RED);
                    loadNewLocationJob(newLocation);
                }
                else {
                    floatlocation.setLabelTextColor(Color.WHITE);
                    recyclerAdapter.filter(list);
                    nojob.setVisibility(View.GONE);
                }


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
        int i=0;
        query=query.toLowerCase();
        final List<Job_details> newlist=new ArrayList<>();

        for(Job_details job:list)
        {
            final String name=job.getJbtitle().toLowerCase();
            if(name.startsWith(query))
            {
                i=1;
                nojob.setVisibility(View.GONE);
                newlist.add(job);
            }
            else if(i==0)
                nojob.setVisibility(View.VISIBLE);
        }
        recyclerAdapter.filter(newlist);
    }

}
