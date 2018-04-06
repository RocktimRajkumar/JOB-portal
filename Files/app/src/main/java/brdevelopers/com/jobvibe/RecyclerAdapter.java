package brdevelopers.com.jobvibe;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private List<Job_details> joblist= Collections.emptyList();
    private Context context;

    public RecyclerAdapter(Context context,List<Job_details> joblist)
    {
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.joblist=joblist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=inflater.inflate(R.layout.job_row,parent,false);
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

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView jobtitle;
        TextView jobcompany;
        TextView joblocation;

//        ArrayList<Job_details> newJobList=new ArrayList<>();
        Context context;
        Job_details job_details;

        public MyViewHolder(View itemView, Context context) {
            super(itemView);
            this.context=context;
//            this.newJobList=newJobList;
            itemView.setOnClickListener(this);
            jobtitle=itemView.findViewById(R.id.TV_jobtitle);
            jobcompany=itemView.findViewById(R.id.TV_companyname);
            joblocation=itemView.findViewById(R.id.TV_location);
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
        }
    }
}
