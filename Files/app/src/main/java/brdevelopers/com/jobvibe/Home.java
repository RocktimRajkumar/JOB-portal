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
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;



public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {


   private TextView matched,recommended,tv_home,tv_activity,tv_notification,tv_empname,tv_empemail;
   private CandidateDetails candidateDetails;
   private ImageView iv_home,iv_activity,iv_notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        matched=findViewById(R.id.TV_matched);
        recommended=findViewById(R.id.TV_recommended);
        iv_home=findViewById(R.id.IV_home);
        iv_activity=findViewById(R.id.IV_activity);
        iv_notification=findViewById(R.id.IV_notification);
        tv_home=findViewById(R.id.TV_home);
        tv_activity=findViewById(R.id.TV_activity);
        tv_notification=findViewById(R.id.TV_notification);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        matched.setOnClickListener(this);
        recommended.setOnClickListener(this);
        iv_home.setOnClickListener(this);
        iv_activity.setOnClickListener(this);
        iv_notification.setOnClickListener(this);
        tv_home.setOnClickListener(this);
        tv_activity.setOnClickListener(this);
        tv_notification.setOnClickListener(this);
        candidateDetails=(CandidateDetails)getIntent().getSerializableExtra("candidate");

        loadFragment(new MatchedFragment());



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        tv_empname = (TextView) headerView.findViewById(R.id.TV_empName);
        tv_empemail=(TextView)headerView.findViewById(R.id.TV_empEmail);

        tv_empname.setText(candidateDetails.getName());
        tv_empemail.setText(candidateDetails.getEmail());

        navigationView.setNavigationItemSelectedListener(this);

        loadFragment(new MatchedFragment());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
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
        }
        else if(v.getId()==R.id.TV_recommended)
        {
           loadFragment(new Recommended());
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
        }
        else if(v.getId()==R.id.IV_activity || v.getId()==R.id.TV_activity)
        {
            iv_activity.setImageResource(R.drawable.ic_onactivity);
            tv_activity.setTextColor(Color.rgb(199, 26, 66));
            iv_home.setImageResource(R.drawable.ic_home);
            tv_home.setTextColor(Color.rgb(0, 150, 136));
            iv_notification.setImageResource(R.drawable.ic_notification);
            tv_notification.setTextColor(Color.rgb(0, 150, 136));
            loadFragment(new ActivityFragment());
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
        }
    }


    public void loadFragment(Fragment fragment) {
        //Sending Degree and FOS to Fragments
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        Bundle bundle=new Bundle();
        bundle.putString("degree",candidateDetails.getDegree());
        bundle.putString("FOS",candidateDetails.getFieldOfStudy());
        fragment.setArguments(bundle);
        FragmentManager fm=getFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.FL_content,fragment);
        ft.commit();
    }
}
