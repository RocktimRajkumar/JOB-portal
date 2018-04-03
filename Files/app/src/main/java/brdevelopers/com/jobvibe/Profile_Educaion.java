package brdevelopers.com.jobvibe;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Uchiha Itachi on 27-03-2018.
 */

public class Profile_Educaion extends Fragment {

    private EditText et_university,et_college,et_cyoc,et_cper;
    private EditText et_12board,et_12school,et_12yoc,et_12per;
    private EditText et_10board,et_10school,et_10yoc,et_10per;
    private TextView tv_btnnext;
    private ProgressBar progressBar;

    private final String editEducation="http://103.230.103.142/jobportalapp/job.asmx/EditCandidateEducationalDetails";
    private final String editPersonal="http://103.230.103.142/jobportalapp/job.asmx/EditCandidatePersonalDetails";
    private CandidateDetails candidateDetails;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.profile_education,container,false);

        Bundle bundle=getArguments();
        candidateDetails= (CandidateDetails) bundle.getSerializable("candidate");

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

        tv_btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                String mschool=et_10board.getText().toString();
                String myoc=et_10yoc.getText().toString();
                String mper=et_10per.getText().toString();

                if(Util.isNetworkConnected(getActivity())) {
                    eduDetailEntry(university, college, cyoc, cper, tboard, tschool, tyoc, tper, mboard, mschool, myoc, mper);
                }
                else{
                    Toast toast=new Toast(getActivity());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM| Gravity.FILL_HORIZONTAL,0,0);

                    LayoutInflater inf=getActivity().getLayoutInflater();

                    View layoutview=inf.inflate(R.layout.custom_toast,(ViewGroup)getActivity().findViewById(R.id.CustomToast_Parent));
                    TextView tf=layoutview.findViewById(R.id.CustomToast);
                    tf.setText("No Internet Connection "+ Html.fromHtml("&#9995;"));
                    toast.setView(layoutview);
                    toast.show();
                    progressBar.setVisibility(View.GONE);
                }

            }
        });
        return view;
    }

    //Sending Data to EditCandidateEducationalDetails
    private void eduDetailEntry(final String university, final String college, final String cyoc, final String cper,
                                final String tboard, final String tschool, final String tyoc, final String tper,
                                final String mboard, final String mschool, final String myoc, final String mper){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, editEducation, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("checklog",""+response);

                candidateDetails.setYOC(cyoc);
                candidateDetails.setPercentage(cper);
                candidateDetails.setInstituteName(college);
                candidateDetails.setUniversityName(university);
                candidateDetails.setHigherBoard(tboard);
                candidateDetails.setHigherSchool(tschool);
                candidateDetails.setHigherYOC(tyoc);
                candidateDetails.setHigherpercentage(tper);
                candidateDetails.setTenboard(mboard);
                candidateDetails.setTenschool(mschool);
                candidateDetails.setTenYOC(myoc);
                candidateDetails.setTenpercentage(mper);

                saveEduDetails(university,college,cyoc,cper);


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("checklog"," "+error);
                Toast.makeText(getActivity(), ""+error, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("email",candidateDetails.getEmail());
                hashMap.put("mschool",mschool);
                hashMap.put("mboard",mboard);
                hashMap.put("mpercentage",mper);
                hashMap.put("mpoy",myoc);
                hashMap.put("tschool",tschool);
                hashMap.put("tboard",tboard);
                hashMap.put("tpercentage",tper);
                hashMap.put("tpoy",tyoc);
                hashMap.put("uname",university);
                hashMap.put("iname",college);
                hashMap.put("poy",cyoc);
                hashMap.put("percentage",cper);
                return hashMap;
            }
        };
        Volley.newRequestQueue(getActivity()).add(stringRequest);


    }
    //Sending Data to EditCandidatePersonalDetail
    private void saveEduDetails(final String university, final String college,final String cyoc, final String cper) {

        StringRequest stringRequest=new StringRequest(Request.Method.POST, editPersonal, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("checklog",response);

                progressBar.setVisibility(View.GONE);

                Intent profile = new Intent(getActivity(),Home.class);
                profile.putExtra("candidate",candidateDetails);
                startActivity(profile);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("checklog",""+error);
                progressBar.setVisibility(View.GONE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("email",candidateDetails.getEmail());
                hashMap.put("name",candidateDetails.getName());
                hashMap.put("mobile",candidateDetails.getMobile());
                hashMap.put("currentcity",candidateDetails.getCurrentcity());
                hashMap.put("address",candidateDetails.getAddress());
                hashMap.put("pincode",candidateDetails.getPincode());
                hashMap.put("gender",candidateDetails.getGender());
                hashMap.put("dob",candidateDetails.getDob());
                hashMap.put("poy",cyoc);
                hashMap.put("percentage",cper);
                hashMap.put("il"," ");
                hashMap.put("iname",college);
                hashMap.put("uname",university);

                return hashMap;
            }
        };
        Volley.newRequestQueue(getActivity()).add(stringRequest);

    }

}
