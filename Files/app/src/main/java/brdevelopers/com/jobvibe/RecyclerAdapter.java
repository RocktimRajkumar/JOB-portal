package brdevelopers.com.jobvibe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private List<Job_details> joblist= Collections.emptyList();
    private Context context;
    private String applyjob="http://103.230.103.142/jobportalapp/job.asmx/ApplyJob";
    private View vv;
    int apply=0;

    public RecyclerAdapter(Context context,List<Job_details> joblist)
    {
        this.context=context;
        if(context!=null)
            inflater=LayoutInflater.from(context);
        this.joblist=joblist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=inflater.inflate(R.layout.job_row,parent,false);
        vv=view;
        MyViewHolder holder=new MyViewHolder(view,context);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Job_details job_details=joblist.get(position);
        holder.bind(job_details);

    }

    @Override
    public int getItemCount() {
        return joblist.size();
    }

    public void filter(List<Job_details> newlist){
        joblist=new ArrayList<>();
        joblist.addAll(newlist);
        notifyDataSetChanged();
    }

    public void setApplyvalue(int n){
        apply=n;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView jobtitle;
        TextView jobcompany;
        TextView joblocation;
        TextView applybtn;

        Context context;
        Job_details job_details;

        public MyViewHolder(View itemView, final Context context) {
            super(itemView);
            this.context=context;

            itemView.setOnClickListener(this);


            jobtitle=itemView.findViewById(R.id.TV_jobtitle);
            jobcompany=itemView.findViewById(R.id.TV_companyname);
            joblocation=itemView.findViewById(R.id.TV_location);
            applybtn=itemView.findViewById(R.id.TV_applybtn);

            if(apply==1){

                applybtn.setVisibility(View.GONE);
            }

            applybtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder report=new AlertDialog.Builder(context);

                    report.setMessage("Are you sure you want to apply for this Job?");

                    report.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if (Util.isNetworkConnected(context)) {
                                applyJob();
                            } else {
                                Toast toast = new Toast(context);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, 0);

                                LayoutInflater inf = inflater;

                                View layoutview = inf.inflate(R.layout.custom_toast, (ViewGroup) vv.findViewById(R.id.CustomToast_Parent));
                                TextView tf = layoutview.findViewById(R.id.CustomToast);
                                tf.setText("No Internet Connection " + Html.fromHtml("&#9995;"));
                                toast.setView(layoutview);
                                toast.show();

                            }
                        }
                    });
                    report.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    report.show();
                }
            });
        }


        public void bind(Job_details job_desc){
            jobtitle.setText(job_desc.getJbtitle());
            jobcompany.setText(job_desc.getJbcompnayname());
            joblocation.setText(job_desc.getJblocation());

            this.job_details=job_desc;
        }

        @Override
        public void onClick(View v) {
            Intent intent=new Intent(this.context,Apply_Job.class);
            intent.putExtra("jobid",job_details.getJbid());
            intent.putExtra("jobtitle",job_details.getJbtitle());
            intent.putExtra("location",job_details.getJblocation());
            intent.putExtra("lastdate",job_details.getJbldapply());
            intent.putExtra("cname",job_details.getJbcompnayname());
            intent.putExtra("url",job_details.getJburl());
            intent.putExtra("salary",job_details.getJbsalary());
            intent.putExtra("bca",job_details.getJbbca());
            intent.putExtra("mca",job_details.getJbmca());
            intent.putExtra("cse",job_details.getJbcse());
            intent.putExtra("it",job_details.getJbit());
            intent.putExtra("ee",job_details.getJbee());
            intent.putExtra("ece",job_details.getJbece());
            intent.putExtra("civil",job_details.getJbece());
            intent.putExtra("mba",job_details.getJbmba());
            intent.putExtra("asp",job_details.getJbasp());
            intent.putExtra("php",job_details.getJbphp());
            intent.putExtra("java",job_details.getJbjava());
            intent.putExtra("ios",job_details.getJbios());
            intent.putExtra("android",job_details.getJbandroid());
            intent.putExtra("dbms",job_details.getJbdbms());
            intent.putExtra("writtentest",job_details.getJbwrittentest());
            intent.putExtra("walkin",job_details.getJbwalking());
            intent.putExtra("online",job_details.getJbonline());
            intent.putExtra("jobdescription",job_details.getJbdescription());
            intent.putExtra("cprofile",job_details.getJbcompanyprofile());
            intent.putExtra("email",job_details.getJbemail());
            this.context.startActivity(intent);
            ((Activity)context).overridePendingTransition(R.anim.topttobottom,R.anim.bottomtotop);
            ((Activity)context).finish();
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
                            DBManager db=new DBManager(context);
                            db.insertNotifcation(job_details.getJbid(),Home.canemail,"0");

                            android.support.v7.app.AlertDialog.Builder message= new android.support.v7.app.AlertDialog.Builder(context);

                            View v=inflater.inflate(R.layout.success_layout,null);
                            message.setView(v);
                            TextView okay=(TextView) v.findViewById(R.id.TV_okay);
                            final android.support.v7.app.AlertDialog dialog = message.create();


                            okay.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    dialog.dismiss();
                                }
                            });

                            dialog.show();

                        }
                        else{
                            Toast toast=new Toast(context);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.BOTTOM|Gravity.FILL_HORIZONTAL,0,0);

                            LayoutInflater inf=inflater;

                            View layoutview=inf.inflate(R.layout.custom_toast,(ViewGroup)vv.findViewById(R.id.CustomToast_Parent));
                            TextView tf=layoutview.findViewById(R.id.CustomToast);
                            tf.setText("You haved already applied for this job. "+ Html.fromHtml(" &#x1f604;"));
                            toast.setView(layoutview);
                            toast.show();
                        }

//                        progressBar.setVisibility(View.GONE);
//                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("LogCheck", "" + e);
//                        progressBar.setVisibility(View.GONE);
//                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    }

                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("LogCheck", "" + error);
                            Toast.makeText(context, "" + error, Toast.LENGTH_SHORT).show();
//                            progressBar.setVisibility(View.GONE);
//                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    HashMap<String, String> jobHash = new HashMap<>();
                    jobHash.put("cemail", Home.canemail);
                    jobHash.put("eemail",job_details.getJbemail());
                    jobHash.put("jobid",job_details.getJbid());

                    return jobHash;
                }

            };

            Volley.newRequestQueue(context).add(stringRequest);
        }
    }
}
