package brdevelopers.com.jobvibe;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {


    private LayoutInflater inflater;
    private List<Job_details> joblist= Collections.emptyList();

    public RecyclerAdapter(Context context,List<Job_details> joblistt)
    {
        inflater=LayoutInflater.from(context);
        this.joblist=joblist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=null;
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Job_details job_details=joblist.get(position);
    }

    @Override
    public int getItemCount() {
        return joblist.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {


        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
