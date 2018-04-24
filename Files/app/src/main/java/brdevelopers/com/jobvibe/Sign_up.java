package brdevelopers.com.jobvibe;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by End User on 27-03-2018.
 */

public class Sign_up extends AppCompatActivity implements TextWatcher,View.OnClickListener{


    private TextInputLayout til_password,til_cpassword,til_email;
    private TextInputEditText et_password,et_cpassword,et_email;
    TextView tv_signup,tv_toLogin;
    private int i_password=0,i_cpassword=0,i_email=0;

    private String getCandidateDetails="http://103.230.103.142/jobportalapp/job.asmx/GetCandidateDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        til_password=findViewById(R.id.TIL_password);
        til_cpassword=findViewById(R.id.TIL_cpassword);
        et_password=findViewById(R.id.ET_password);
        et_cpassword=findViewById(R.id.ET_cpassword);
        tv_signup=findViewById(R.id.TV_signbutton);
        tv_toLogin=findViewById(R.id.TV_backtoLogin);
        et_email=findViewById(R.id.ET_email);
        til_email=findViewById(R.id.TIL_email);

        et_password.addTextChangedListener(this);
        et_cpassword.addTextChangedListener(this);
        et_email.addTextChangedListener(this);
        tv_signup.setOnClickListener(this);
        tv_toLogin.setOnClickListener(this);

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {


    }

    @Override
    public void afterTextChanged(Editable s) {

        String email = et_email.getText().toString().trim();
        String pattern = "^[a-zA-Z0-9._]{1,20}@[a-zA-Z]{1,10}.(com|org)$";
        Matcher matcherObj = Pattern.compile(pattern).matcher(email);

        if(et_email.getText().length()>0 && !matcherObj.matches()) {
            til_email.setError(getString(R.string.error_email));
            i_email=0;

        }
        if(et_email.getText().length()>0 && matcherObj.matches()) {
            til_email.setError(null);
            if(Util.isNetworkConnected(this)) {
                checkEmail(et_email.getText().toString());
            }
            else{
                Toast toast=new Toast(this);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM|Gravity.FILL_HORIZONTAL,0,0);

                LayoutInflater inf=getLayoutInflater();

                View layoutview=inf.inflate(R.layout.custom_toast,(ViewGroup)findViewById(R.id.CustomToast_Parent));
                TextView tf=layoutview.findViewById(R.id.CustomToast);
                tf.setText("No Internet Connection "+ Html.fromHtml("&#9995;"));
                toast.setView(layoutview);
                toast.show();
            }
        }
        if(et_email.getText().length()==0)
        {
            til_email.setError(null);
            i_email=0;
        }

        if(et_password.getText().length()>0){
            til_password.setPasswordVisibilityToggleEnabled(true);
            if(et_password.getText().length()<8) {
                til_password.setError(getString(R.string.error_password));
                i_password = 0;
            }

            else {
                i_password = 1;
                til_password.setError(null);
            }
        }
        if(et_cpassword.getText().length()>0){
            til_cpassword.setPasswordVisibilityToggleEnabled(true);
            if(!et_password.getText().toString().equals(et_cpassword.getText().toString()))
            {
                til_cpassword.setError(getString(R.string.error_cpassword));
                i_cpassword=0;
            }
            else {
                til_cpassword.setError(null);
                i_cpassword = 1;
            }
        }
        if(et_password.getText().length()==0) {
            til_password.setPasswordVisibilityToggleEnabled(false);
            i_password = 0;
            til_password.setError(null);
        }
        if(et_cpassword.getText().length()==0) {
            til_cpassword.setPasswordVisibilityToggleEnabled(false);
            til_cpassword.setError(null);
            i_cpassword = 0;
        }

    }

    private void checkEmail(final String s) {

        StringRequest stringRequest=new StringRequest(Request.Method.POST, getCandidateDetails, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String responsemessage=jsonObject.getString("Sucess");
                    Log.d("LogCheck",response);
                    if(responsemessage.equals("1")) {
                        til_email.setError("Email Id already used.");
                        i_email = 0;
                    }
                    else {
                        til_email.setError(null);
                        i_email = 1;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
            new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("LogCheck",""+error);
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> emailexists=new HashMap<>();
                emailexists.put("email",s);
                return emailexists;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.TV_signbutton){

        if (i_email == 0)
            et_email.requestFocus();
        else if (i_password == 0)
            et_password.requestFocus();
        else if (i_cpassword == 0)
            et_cpassword.requestFocus();


        final Float elevation = tv_signup.getElevation();
        tv_signup.setElevation(-elevation);

        int TIMMER = 200;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (i_email == 0 && et_email.getText().toString().length() == 0) {
                    et_email.requestFocus();
                    til_email.setError(getString(R.string.error_empty_field));
                    tv_signup.setElevation(elevation);
                } else if (i_email == 0) {
                    et_email.requestFocus();
                    tv_signup.setElevation(elevation);
                }

                if (i_password == 0 && et_password.getText().toString().length() == 0) {
                    if (i_email == 0)
                        et_email.requestFocus();
                    else
                        et_password.requestFocus();
                    til_password.setError(getString(R.string.error_empty_field));
                    tv_signup.setElevation(elevation);
                } else if (i_password == 0) {
                    if (i_email == 0)
                        et_email.requestFocus();
                    else
                        et_password.requestFocus();
                    tv_signup.setElevation(elevation);
                }

                if (i_cpassword == 0 && et_cpassword.getText().toString().length() == 0) {
                    if (i_email == 0)
                        et_email.requestFocus();
                    else if (i_password == 0)
                        et_password.requestFocus();
                    else
                        et_cpassword.requestFocus();
                    til_cpassword.setError(getString(R.string.error_empty_field));
                    tv_signup.setElevation(elevation);
                } else if (i_cpassword == 0) {
                    if (i_email == 0)
                        et_email.requestFocus();
                    else if (i_password == 0)
                        et_password.requestFocus();
                    else
                        et_cpassword.requestFocus();
                    tv_signup.setElevation(elevation);
                }


                int TIMMES = 300;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (i_password == 1 && i_email == 1 && i_cpassword == 1) {
                            if (Util.isNetworkConnected(Sign_up.this)) {
                                Intent profile = new Intent(Sign_up.this, Profile.class);
                                String email = et_email.getText().toString();
                                String password = et_password.getText().toString();
                                profile.putExtra("email", email);
                                profile.putExtra("password", password);
                                startActivity(profile);
                                finish();
                            } else {
                                Toast toast = new Toast(Sign_up.this);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, 0);

                                LayoutInflater inf = getLayoutInflater();

                                View layoutview = inf.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.CustomToast_Parent));
                                TextView tf = layoutview.findViewById(R.id.CustomToast);
                                tf.setText("No Internet Connection " + Html.fromHtml("&#9995;"));
                                toast.setView(layoutview);
                                toast.show();
                            }
                        }
                    }
                }, TIMMES);


            }
        }, TIMMER);

    }
        if(v.getId()== R.id.TV_backtoLogin){
            Intent intent=new Intent(Sign_up.this,Login.class);
            startActivity(intent);
            overridePendingTransition(R.anim.lefttoright,R.anim.righttoleft);
            finish();
        }

    }

}

