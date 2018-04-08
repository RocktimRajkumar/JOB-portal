package brdevelopers.com.jobvibe;


import android.app.DownloadManager;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;


public class Edit_EducationFragment extends Fragment implements View.OnClickListener{

    private EditText et_university,et_college,et_cyoc,et_cper;
    private EditText et_12board,et_12school,et_12yoc,et_12per;
    private EditText et_10board,et_10school,et_10yoc,et_10per;
    private TextView tv_btnnext;
    private ProgressBar progressBar;

    private String editEducation="http://103.230.103.142/jobportalapp/job.asmx/EditCandidateEducationalDetails";
    private String getcandidatedetail="http://103.230.103.142/jobportalapp/job.asmx/GetCandidateDetails";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_edit__education,container,false);
        et_university=view.findViewById(R.id.ET_university);
        et_college=view.findViewById(R.id.ET_college);
        et_cyoc=view.findViewById(R.id.ET_cyoc);
        et_cper=view.findViewById(R.id.ET_cper);
        et_12board=view.findViewById(R.id.ET_12board);
        et_12school=view.findViewById(R.id.ET_12school);
        et_12yoc=view.findViewById(R.id.ET_12yoc);
        et_12per=view.findViewById(R.id.ET_12per);
        et_10board=view.findViewById(R.id.ET_10board);
        et_10school=view.findViewById(R.id.ET_10school);
        et_10yoc=view.findViewById(R.id.ET_10yoc);
        et_10per=view.findViewById(R.id.ET_10per);
        tv_btnnext=view.findViewById(R.id.TV_btnnext);
        progressBar=view.findViewById(R.id.progressbar);


        getCandidateDetals(Home.canemail);
        tv_btnnext.setOnClickListener(this);

        return view;
    }

    private void getCandidateDetals(final String email){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, getcandidatedetail, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONObject jsonObject2=jsonObject.getJSONObject("CandidateDetails");
                    String email=jsonObject2.getString("email");
                    String university=jsonObject2.getString("uname");
                    String college=jsonObject2.getString("iname");
                    String cyoc=jsonObject2.getString("poy");
                    String cper=jsonObject2.getString("percentage");
                    String tboard=jsonObject2.getString("tboard");
                    String tschool=jsonObject2.getString("tschool");
                    String tyoc=jsonObject2.getString("tpoy");
                    String tper=jsonObject2.getString("tpercentage");
                    String mboard=jsonObject2.getString("mboard");
                    String mschool=jsonObject2.getString("mschool");
                    String myoc=jsonObject2.getString("mpoy");
                    String mper=jsonObject2.getString("mpercentage");


                    et_university.setText(university);
                    et_college.setText(college);
                    et_cyoc.setText(cyoc);
                    et_cper.setText(cper);
                    et_12board.setText(tboard);
                    et_12school.setText(tschool);
                    et_12yoc.setText(tyoc);
                    et_12per.setText(tper);
                    et_10board.setText(mboard);
                    et_10school.setText(mschool);
                    et_10yoc.setText(myoc);
                    et_10per.setText(mper);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("email",email);
                return hashMap;
            }
        };
        Volley.newRequestQueue(getActivity()).add(stringRequest);

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.TV_btnnext)
        {
            progressBar.setVisibility(View.VISIBLE);

            String university=et_university.getText().toString();
            String college=et_college.getText().toString();
            String cyoc=et_cyoc.getText().toString();
            String cper=et_cper.getText().toString();
            String tboard=et_12board.getText().toString();
            String tschool=et_12school.getText().toString();
            String tyoc=et_12yoc.getText().toString();
            String tper=et_12per.getText().toString();
            String mboard=et_10board.getText().toString();
            String mschool=et_10school.getText().toString();
            String myoc=et_10yoc.getText().toString();
            String mper=et_10per.getText().toString();



            if(Util.isNetworkConnected(getActivity())) {
                String email=Home.canemail;
                editCandidateDetails(email,university,college, cyoc, cper, tboard, tschool, tyoc, tper, mboard, mschool, myoc, mper);
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
            }


        }

    }

    private void editCandidateDetails(final String email, final String university, final String college, final String cyoc, final String cper, final String tboard, final String tschool, final String tyoc, final String tper, final String mboard, final String mschool, final String myoc, final String mper){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, editEducation, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                Log.d("show"," "+response);
                Toast toast=new Toast(getActivity());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM|Gravity.FILL_HORIZONTAL,0,0);

                LayoutInflater inf=getActivity().getLayoutInflater();

                View layoutview=inf.inflate(R.layout.custom_toast,(ViewGroup)getActivity().findViewById(R.id.CustomToast_Parent));
                TextView tf=layoutview.findViewById(R.id.CustomToast);
                tf.setText("Details Updated Sucessfully "+ Html.fromHtml("&#x1f604;"));
                toast.setView(layoutview);
                toast.show();
                progressBar.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("show",""+error);
                progressBar.setVisibility(View.GONE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("email",email);
                hashMap.put("uname",university);
                hashMap.put("iname",college);
                hashMap.put("poy",cyoc);
                hashMap.put("percentage",cper);
                hashMap.put("tboard",tboard);
                hashMap.put("tschool",tschool);
                hashMap.put("tpoy",tyoc);
                hashMap.put("tpercentage",tper);
                hashMap.put("mboard",mboard);
                hashMap.put("mschool",mschool);
                hashMap.put("mpoy",myoc);
                hashMap.put("mpercentage",mper);
                return hashMap;
            }
        };
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }
}
