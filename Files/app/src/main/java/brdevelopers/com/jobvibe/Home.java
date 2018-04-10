package brdevelopers.com.jobvibe;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {


   private TextView matched,recommended,viewed,saved,applied,tv_home,tv_activity,tv_notification,tv_empname,tv_empemail;
   private ImageView iv_home,iv_activity,iv_notification,iv_profileImage;
   public static String canemail;
   private static String name,getdegree,getfos;
   private boolean onbackpressed=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        matched=findViewById(R.id.TV_matched);
        recommended=findViewById(R.id.TV_recommended);
        viewed=findViewById(R.id.TV_viewed);
        saved=findViewById(R.id.TV_saved);
        applied=findViewById(R.id.TV_applied);
        iv_home=findViewById(R.id.IV_home);
        iv_activity=findViewById(R.id.IV_activity);
        iv_notification=findViewById(R.id.IV_notification);
        tv_home=findViewById(R.id.TV_home);
        tv_activity=findViewById(R.id.TV_activity);
        tv_notification=findViewById(R.id.TV_notification);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        matched.setBackgroundColor(Color.rgb(0, 150, 136));

        matched.setOnClickListener(this);
        recommended.setOnClickListener(this);
        viewed.setOnClickListener(this);
        saved.setOnClickListener(this);
        applied.setOnClickListener(this);
        iv_home.setOnClickListener(this);
        iv_activity.setOnClickListener(this);
        iv_notification.setOnClickListener(this);
        tv_home.setOnClickListener(this);
        tv_activity.setOnClickListener(this);
        tv_notification.setOnClickListener(this);

        canemail=getIntent().getStringExtra("emailid");
        name=getIntent().getStringExtra("name");
        getdegree= getIntent().getStringExtra("gedegree");
        getfos=getIntent().getStringExtra("getfos");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        tv_empname = (TextView) headerView.findViewById(R.id.TV_profileName);
        tv_empemail=(TextView)headerView.findViewById(R.id.TV_profileEmail);
        iv_profileImage=headerView.findViewById(R.id.imageView);

        iv_profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editProfile = new Intent(Home.this, EditActivity.class);
                startActivity(editProfile);

            }
        });

        tv_empname.setText(name);
        tv_empemail.setText(canemail);

        navigationView.setNavigationItemSelectedListener(this);

        iv_home.setImageResource(R.drawable.ic_onhome);
        tv_home.setTextColor(Color.rgb(199, 26, 66));
        iv_activity.setImageResource(R.drawable.ic_activity);
        tv_activity.setTextColor(Color.rgb(0, 150, 136));
        iv_notification.setImageResource(R.drawable.ic_notification);
        tv_notification.setTextColor(Color.rgb(0, 150, 136));

        loadFragment(new MatchedFragment());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            Log.d("logcheck","drawer");
            drawer.closeDrawer(GravityCompat.START);
        }
       else if(!onbackpressed){
            Log.d("logcheck","backpressed");
            Toast toast = new Toast(Home.this);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, 0);

            LayoutInflater inf = getLayoutInflater();

            View layoutview = inf.inflate(R.layout.custom_toast, (ViewGroup)findViewById(R.id.CustomToast_Parent));
            TextView tf = layoutview.findViewById(R.id.CustomToast);
            tf.setText("Press back again to exit " + Html.fromHtml("&#9995;"));
            toast.setView(layoutview);
            toast.show();
            onbackpressed=true;
        }
        else{
            Log.d("logcheck","back super");
            super.onBackPressed();
        }
        new CountDownTimer(3000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("logcheck","on countdown timer ontick");
            }

            @Override
            public void onFinish() {
                Log.d("logcheck","on countdown timer onfinish");
                onbackpressed=false;
            }
        }.start();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_setting) {
            // Handle the camera action
        } else if (id == R.id.nav_faq) {

        } else if (id == R.id.nav_terms) {

        } else if (id == R.id.nav_report) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_about) {

        }else if(id==R.id.nav_logout){

            SharedPreferences sharedPreferences=getSharedPreferences("Data",MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("email","");
            editor.putString("password","");
            editor.commit();

            Intent login=new Intent(Home.this,Login.class);
            startActivity(login);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.TV_matched)
        {

            loadFragment(new MatchedFragment());
            matched.setBackgroundColor(Color.rgb(0, 150, 136));
            recommended.setBackgroundColor(Color.rgb(255, 255, 255));
        }
        else if(v.getId()==R.id.TV_recommended)
        {

           loadFragment(new Recommended());

            recommended.setBackgroundColor(Color.rgb(0, 150, 136));
            matched.setBackgroundColor(Color.rgb(255, 255, 255));
        }
        else if(v.getId()==R.id.TV_viewed){
            loadFragment(new Viewed_Fragment());
        }
        else if(v.getId()==R.id.TV_saved){
            loadFragment(new Saved_Fragment());
        }
        else if(v.getId()==R.id.TV_applied){
            loadFragment(new Applied_Fragment());
        }
        else if(v.getId()==R.id.IV_home || v.getId()==R.id.TV_home)
        {
            iv_home.setImageResource(R.drawable.ic_onhome);
            tv_home.setTextColor(Color.rgb(199, 26, 66));
            iv_activity.setImageResource(R.drawable.ic_activity);
            tv_activity.setTextColor(Color.rgb(0, 150, 136));
            iv_notification.setImageResource(R.drawable.ic_notification);
            tv_notification.setTextColor(Color.rgb(0, 150, 136));


            loadFragment(new MatchedFragment());
            matched.setVisibility(View.VISIBLE);
            recommended.setVisibility(View.VISIBLE);
            viewed.setVisibility(View.GONE);
            saved.setVisibility(View.GONE);
            applied.setVisibility(View.GONE);

        }
        else if(v.getId()==R.id.IV_activity || v.getId()==R.id.TV_activity)
        {
            iv_activity.setImageResource(R.drawable.ic_onactivity);
            tv_activity.setTextColor(Color.rgb(199, 26, 66));
            iv_home.setImageResource(R.drawable.ic_home);
            tv_home.setTextColor(Color.rgb(0, 150, 136));
            iv_notification.setImageResource(R.drawable.ic_notification);
            tv_notification.setTextColor(Color.rgb(0, 150, 136));

            loadFragment(new Viewed_Fragment());
            matched.setVisibility(View.GONE);
            recommended.setVisibility(View.GONE);
            viewed.setVisibility(View.VISIBLE);
            saved.setVisibility(View.VISIBLE);
            applied.setVisibility(View.VISIBLE);


        }
        else if(v.getId()==R.id.IV_notification || v.getId()==R.id.TV_notification)
        {
            iv_notification.setImageResource(R.drawable.ic_onnotification);
            tv_notification.setTextColor(Color.rgb(199, 26, 66));
            iv_home.setImageResource(R.drawable.ic_home);
            tv_home.setTextColor(Color.rgb(0, 150, 136));
            iv_activity.setImageResource(R.drawable.ic_activity);
            tv_activity.setTextColor(Color.rgb(0, 150, 136));

            loadFragment(new NotificationFragment());
            matched.setVisibility(View.GONE);
            recommended.setVisibility(View.GONE);
            viewed.setVisibility(View.GONE);
            saved.setVisibility(View.GONE);
            applied.setVisibility(View.GONE);

        }
//        else if(v.getId()==R.id.imageView){
//
//        }
    }

    public void loadFragment(Fragment fragment) {
        //Sending Degree and FOS to Fragments
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        Bundle bundle=new Bundle();
        bundle.putString("degree",getdegree);
        bundle.putString("FOS",getfos);

        fragment.setArguments(bundle);
        FragmentManager fm=getFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.FL_content,fragment);
        ft.addToBackStack(null);

        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

//        getMenuInflater().inflate(R.menu.home, menu);

        return true;
    }


}
