package brdevelopers.com.jobvibe;


import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.os.ConditionVariable;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class Edit_PersonalFragment extends Fragment implements View.OnClickListener,View.OnFocusChangeListener,TextWatcher {
    private EditText et_email, et_dob,et_mobile,et_name,et_address,et_pincode,et_city,et_degree,et_fos;
    private RadioButton radio_male,radio_female;
    private ImageView iv_dob;
    private TextView tv_btnnext,tv_age;
    private ProgressBar progressBar;


    private String degreeUrl="http://103.230.103.142/jobportalapp/job.asmx/GetCourse";
    private String fosUrl="http://103.230.103.142/jobportalapp/job.asmx/GetBranch";
    private String editcandidate="http://103.230.103.142/jobportalapp/job.asmx/EditCandidatePersonalDetails";
    private String getcandidatedetail="http://103.230.103.142/jobportalapp/job.asmx/GetCandidateDetails";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_edit__personal,container,false);
        //Binding view to references
        et_email= view.findViewById(R.id.ET_email);
        et_name= view.findViewById(R.id.ET_name);
        et_mobile=view.findViewById(R.id.ET_mobie);
        et_address=view.findViewById(R.id.ET_address);
        et_pincode= view.findViewById(R.id.ET_pincode);
        et_city=view.findViewById(R.id.ET_currentcity);
        et_dob=view.findViewById(R.id.ET_dob);
        radio_male=view.findViewById(R.id.Radio_male);
        radio_female=view.findViewById(R.id.Radio_female);
        et_degree=view.findViewById(R.id.ET_degree);
        et_fos=view.findViewById(R.id.ET_fos);
        iv_dob=view.findViewById(R.id.IV_dob);
        tv_btnnext=view.findViewById(R.id.TV_btnnext);
        tv_age=view.findViewById(R.id.TV_age);
        progressBar=view.findViewById(R.id.progressbar);

        String email=Home.canemail;


        if (Util.isNetworkConnected(getActivity())) {
            getCandidateDetail(email);
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
        }

        et_dob.setOnClickListener(this);        //Edit Text dob click
        iv_dob.setOnClickListener(this);        //Image View dob click
        et_dob.setOnFocusChangeListener(this);  //Edit Text dob on focus
        et_mobile.addTextChangedListener(this);//Edit Text mobile on textchange


        tv_btnnext.setOnClickListener(this);

        return view;

    }

    private void getCandidateDetail(final String email){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, getcandidatedetail, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("checklog"," "+response);
                try {
                    JSONObject detailsObject=new JSONObject(response);
                    JSONObject jsonObject=detailsObject.getJSONObject("CandidateDetails");
                    String email=jsonObject.getString("email");
                    String name=jsonObject.getString("name");
                    String currentcity=jsonObject.getString("currentcity");
                    String address=jsonObject.getString("address");
                    String pincode=jsonObject.getString("pincode");
                    String gender=jsonObject.getString("gender");
                    String dob=jsonObject.getString("dob");
                    String getdegree=jsonObject.getString("course");
                    String getfos=jsonObject.getString("branch");
                    String mobile=jsonObject.getString("mob");

                    Calendar c=Calendar.getInstance();
                    final int dd,mm,yy;
                    dd=c.get(Calendar.DAY_OF_MONTH);
                    mm=c.get(Calendar.MONTH);
                    yy=c.get(Calendar.YEAR);

                    if(dob.length()>4) {
                        String[] date = dob.split("/");
                        Log.d("logcheck", "hjfjf" + date[0] + date[1] + date[2]);

                        calculateAge(dd, mm, yy, Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
                    }

                    et_email.setText(email);
                    et_name.setText(name);
                    et_mobile.setText(mobile);
                    et_address.setText(address);
                    et_pincode.setText(pincode);
                    et_city.setText(currentcity);
                    et_dob.setText(dob);
                    et_degree.setText(getdegree);
                    et_fos.setText(getfos);

                    if(gender.equals("Male"))
                        radio_male.isChecked();
                    else if (gender.equals("Female"))
                        radio_female.isChecked();



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("checklog"," "+error);
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



    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.TV_btnnext)
        {
            progressBar.setVisibility(View.VISIBLE);

            String email=et_email.getText().toString();
            String name=et_name.getText().toString();
            String mobile=et_mobile.getText().toString();
            String address=et_address.getText().toString();
            String pincode=et_pincode.getText().toString();
            String city=et_city.getText().toString();
            String dob=et_dob.getText().toString();

            String gender=null;
            boolean bol=radio_male.isChecked();
            if(bol)
                gender="Male";
            else
                gender="Female";


            if(Util.isNetworkConnected(getActivity())) {
                editcandidateDetails(email, name, mobile, address, pincode, city, dob, gender);
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
        else if(v.getId()==R.id.ET_dob)
        {
            //Calling dateOfBirth on click on edittext
            dateOfBirth();
        }
        else if(v.getId()==R.id.IV_dob)
        {
            //Calling dateOfBirth on click on calendar ImageView
            dateOfBirth();
        }

    }

    private void editcandidateDetails(final String email, final String name, final String mobile, final String address, final String pincode, final String city, final String dob, final String gender) {

        StringRequest stringRequest=new StringRequest(Request.Method.POST, editcandidate, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("show"," "+response);
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
                Log.d("checklog"," "+error);
                Toast.makeText(getActivity(), ""+error, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("email",email);
                hashMap.put("name",name);
                hashMap.put("mobile",mobile);
                hashMap.put("currentcity",city);
                hashMap.put("address",address);
                hashMap.put("pincode",pincode);
                hashMap.put("gender",gender);
                hashMap.put("dob",dob);
                hashMap.put("poy"," ");
                hashMap.put("percentage"," ");
                hashMap.put("il"," ");
                hashMap.put("iname"," ");
                hashMap.put("uname"," ");

                return hashMap;
            }
        };
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }


    private void dateOfBirth() {

        Calendar c=Calendar.getInstance();
        final int dd,mm,yy;
        dd=c.get(Calendar.DAY_OF_MONTH);
        mm=c.get(Calendar.MONTH);
        yy=c.get(Calendar.YEAR);

        DatePickerDialog dpd=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                et_dob.setText(dayOfMonth+"/"+month+"/"+year);
                calculateAge(dd,mm,yy,dayOfMonth,month,year); //calling calculateage to show it on textview age
            }
        },dd,mm,yy);

        dpd.updateDate(1980,0,1);
        dpd.show();
    }

    //On focus on edittext
    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if(v.getId()==R.id.ET_dob && hasFocus)
        {
            dateOfBirth();
        }

    }

// calculating age from datetimepicker and show it on textview age

    public void calculateAge(int cdd,int cmm,int cyy,int bdd,int bmm,int byy)
    {
        cmm++;
        bmm++;
        if(cdd<bdd)
            cmm=cmm-1;
        if(cmm<bmm)
            cyy=cyy-1;

        int age=cyy-byy;

        tv_age.setText("Age : "+age);
        tv_age.setVisibility(View.VISIBLE);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {


    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        String phoneno=et_mobile.getText().toString();

        if(phoneno.length()==1) {
            s.replace(0, phoneno.length(),"+91"+phoneno);
        }
        else if(phoneno.length()==3)
        {
            s.replace(0,phoneno.length(),"");
        }

    }



}
