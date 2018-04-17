package brdevelopers.com.jobvibe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity implements TextWatcher,View.OnClickListener{


   private TextInputLayout til_password,til_email;
   private TextInputEditText et_email, et_password;
   private TextView tv_btnlogin,tv_createnew;
   private ProgressBar progressBar;
   private int i_email=0,i_password=0;


    private String saveLogin="http://103.230.103.142/jobportalapp/job.asmx/CandidateLogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        et_email=findViewById(R.id.ET_email);
        et_password=findViewById(R.id.ET_password);
        tv_btnlogin=findViewById(R.id.TV_loginbutton);
        til_password=findViewById(R.id.TIL_password);
        til_email=findViewById(R.id.TIL_email);
        tv_createnew=findViewById(R.id.crete_new);
        progressBar=findViewById(R.id.progressbar);

        et_email.addTextChangedListener(this);
        et_password.addTextChangedListener(this);
        tv_btnlogin.setOnClickListener(this);
        tv_createnew.setOnClickListener(this);

        loadLoginDetails();
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
        String pattern = "^[a-zA-Z0-9]{1,20}@[a-zA-Z]{1,10}.(com|org)$";
        Matcher matcherObj = Pattern.compile(pattern).matcher(email);

        if(et_email.getText().length()>0 && !matcherObj.matches()) {
            til_email.setError(getString(R.string.error_email));
            i_email=0;

        }
        if(et_email.getText().length()>0 && matcherObj.matches()) {
            til_email.setError(null);
            i_email=1;
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

        if(et_password.getText().length()==0) {
            til_password.setPasswordVisibilityToggleEnabled(false);
            i_password = 0;
            til_password.setError(null);
        }

    }

    @Override
    public void onClick(final View v) {

        if(v.getId()==R.id.TV_loginbutton) {
            final Float elevation = tv_btnlogin.getElevation();
            tv_btnlogin.setElevation(-elevation);

            int TIMMER = 200;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (i_email == 0 && et_email.getText().toString().length() == 0) {
                        et_email.requestFocus();
                        til_email.setError(getString(R.string.error_empty_field));
                        tv_btnlogin.setElevation(elevation);
                    } else if (i_email == 0) {
                        et_email.requestFocus();
                        tv_btnlogin.setElevation(elevation);
                    }

                    if (i_password == 0 && et_password.getText().toString().length() == 0) {
                        if (i_email == 0)
                            et_email.requestFocus();
                        else
                            et_password.requestFocus();
                        til_password.setError(getString(R.string.error_empty_field));
                        tv_btnlogin.setElevation(elevation);
                    } else if (i_password == 0) {
                        if (i_email == 0)
                            et_email.requestFocus();
                        else
                            et_password.requestFocus();
                        tv_btnlogin.setElevation(elevation);
                    }

                    if (i_password == 1 && i_email == 1) {

                        if(Util.isNetworkConnected(Login.this)) {

                            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            progressBar.setVisibility(View.VISIBLE);
                            String email=et_email.getText().toString();
                            String password=et_password.getText().toString();

                            //Saving user name and password to Shared preferences
                            saveLoginDetails("email",email);
                            saveLoginDetails("password",password);

                            checkCredential(email,password);
                        }
                        else{
                            Toast toast=new Toast(Login.this);
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
                }


            }, TIMMER);



        }
        else if(v.getId()==R.id.crete_new)
        {
            Intent profile = new Intent(Login.this, Sign_up.class);
            startActivity(profile);
            finish();
        }


    }

    //Saving Login Details
    private  void saveLoginDetails(String key,String value){
        SharedPreferences sharedPreferences=getSharedPreferences("Data",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }
    //Loading Login Details
    private void loadLoginDetails(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBar.setVisibility(View.VISIBLE);
        SharedPreferences sharedPreferences=getSharedPreferences("Data",MODE_PRIVATE);
        String email=sharedPreferences.getString("email"," ");
        String password=sharedPreferences.getString("password"," ");

        String pattern = "^[a-zA-Z0-9]{1,20}@[a-zA-Z]{1,10}.(com|org)$";
        Matcher matcherObj = Pattern.compile(pattern).matcher(email);

        if(email.length()>0 && matcherObj.matches() && password.length()>=8) {
            et_email.setText(email);
            et_password.setText(password);
            if(Util.isNetworkConnected(Login.this)) {
                checkCredential(email, password);
            }
            else{
                Toast toast=new Toast(Login.this);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM|Gravity.FILL_HORIZONTAL,0,0);

                progressBar.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                LayoutInflater inf=getLayoutInflater();

                View layoutview=inf.inflate(R.layout.custom_toast,(ViewGroup)findViewById(R.id.CustomToast_Parent));
                TextView tf=layoutview.findViewById(R.id.CustomToast);
                tf.setText("No Internet Connection "+ Html.fromHtml("&#9995;"));
                toast.setView(layoutview);
                toast.show();
            }
        }
        else {
            et_email.setText(null);
            et_password.setText(null);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    private void checkCredential(final String email, final String password) {

        StringRequest stringRequest=new StringRequest(Request.Method.POST, saveLogin, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("logcheck",response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String success=jsonObject.getString("Sucess");
                    if(success.equals("1"))
                    {
                        JSONObject jsonObject2=jsonObject.getJSONObject("CandidateDetails");
                        String email=jsonObject2.getString("email");
                        String name=jsonObject2.getString("name");

                        String degree=jsonObject2.getString("course");
                        String fieldofstudy=jsonObject2.getString("branch");


                        progressBar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        Intent profile = new Intent(getApplicationContext(),Home.class);

                        profile.putExtra("emailid",email);
                        profile.putExtra("name",name);
                        profile.putExtra("getdegree",degree);
                        profile.putExtra("getfos",fieldofstudy);
                        startActivity(profile);
                        finish();
                    }
                    else{
                        i_password=0;
                        i_email=0;
                        til_password.setError("Invalid login Credential!");
                        progressBar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("logcheck",""+e);
                    progressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("logcheck",""+error);
                Toast.makeText(Login.this, ""+error, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("email",email);
                hashMap.put("pwd",password);
                return hashMap;
            }
        };

        Volley.newRequestQueue(Login.this).add(stringRequest);
    }
}
