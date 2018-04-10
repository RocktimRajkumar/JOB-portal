package brdevelopers.com.jobvibe;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {


   private TextView matched,recommended,viewed,saved,applied,tv_home,tv_activity,tv_notification,tv_empname,tv_empemail;
   private ImageView iv_home,iv_activity,iv_notification,iv_profileImage;
   public static String canemail;
   public static String name,getdegree,getfos;
   private boolean onbackpressed=false;
   private TabLayout tabLayout;
   private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        iv_home=findViewById(R.id.IV_home);
        iv_activity=findViewById(R.id.IV_activity);
        iv_notification=findViewById(R.id.IV_notification);
        tv_home=findViewById(R.id.TV_home);
        tv_activity=findViewById(R.id.TV_activity);
        tv_notification=findViewById(R.id.TV_notification);
        tabLayout=findViewById(R.id.TL_tab);
        viewPager=findViewById(R.id.VP_view);


        tabLayout.setupWithViewPager(viewPager);
        setterViewPager(viewPager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        iv_home.setOnClickListener(this);
        iv_activity.setOnClickListener(this);
        iv_notification.setOnClickListener(this);
        tv_home.setOnClickListener(this);
        tv_activity.setOnClickListener(this);
        tv_notification.setOnClickListener(this);

        canemail=getIntent().getStringExtra("emailid");
        name=getIntent().getStringExtra("name");
        getdegree= getIntent().getStringExtra("getdegree");
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

        tv_empname.setOnClickListener(new View.OnClickListener() {
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

    }

    private void setterViewPager(ViewPager viewPager) {

        ViewAdapter viewAdapter=new ViewAdapter(getSupportFragmentManager());
        viewAdapter.addFragment(new MatchedFragment(),"Matched");
        viewAdapter.addFragment(new Recommended(),"Recommended");
        viewPager.setAdapter(viewAdapter);
    }

    private void setterViewPagerActivity(ViewPager viewPager) {

        ViewAdapter viewAdapter=new ViewAdapter(getSupportFragmentManager());
        viewAdapter.addFragment(new Viewed_Fragment(),"Viewed");
        viewAdapter.addFragment(new Saved_Fragment(),"Saved");
        viewAdapter.addFragment(new Applied_Fragment(),"Applied");
        viewPager.setAdapter(viewAdapter);
    }

    private void setterViewPagerNotification(ViewPager viewPager) {

        ViewAdapter viewAdapter=new ViewAdapter(getSupportFragmentManager());
        viewAdapter.addFragment(new NotificationFragment(),"Notification");
        viewPager.setAdapter(viewAdapter);
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
            Intent intent=new Intent(Home.this,FAQ.class);
            startActivity(intent);

        } else if (id == R.id.nav_terms) {
            Intent intent=new Intent(Home.this,Terms.class);
            startActivity(intent);


        } else if (id == R.id.nav_report) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_about) {
            Intent intent=new Intent(Home.this,AboutUs.class);
            startActivity(intent);

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

        if (v.getId() == R.id.IV_home || v.getId() == R.id.TV_home) {
            iv_home.setImageResource(R.drawable.ic_onhome);
            tv_home.setTextColor(Color.rgb(199, 26, 66));
            iv_activity.setImageResource(R.drawable.ic_activity);
            tv_activity.setTextColor(Color.rgb(0, 150, 136));
            iv_notification.setImageResource(R.drawable.ic_notification);
            tv_notification.setTextColor(Color.rgb(0, 150, 136));

            tabLayout.setVisibility(View.VISIBLE);
            tabLayout.setupWithViewPager(viewPager);
            setterViewPager(viewPager);

        } else if (v.getId() == R.id.IV_activity || v.getId() == R.id.TV_activity) {
            iv_activity.setImageResource(R.drawable.ic_onactivity);
            tv_activity.setTextColor(Color.rgb(199, 26, 66));
            iv_home.setImageResource(R.drawable.ic_home);
            tv_home.setTextColor(Color.rgb(0, 150, 136));
            iv_notification.setImageResource(R.drawable.ic_notification);
            tv_notification.setTextColor(Color.rgb(0, 150, 136));

            tabLayout.setVisibility(View.VISIBLE);
            tabLayout.setupWithViewPager(viewPager);
            setterViewPagerActivity(viewPager);


        } else if (v.getId() == R.id.IV_notification || v.getId() == R.id.TV_notification) {
            iv_notification.setImageResource(R.drawable.ic_onnotification);
            tv_notification.setTextColor(Color.rgb(199, 26, 66));
            iv_home.setImageResource(R.drawable.ic_home);
            tv_home.setTextColor(Color.rgb(0, 150, 136));
            iv_activity.setImageResource(R.drawable.ic_activity);
            tv_activity.setTextColor(Color.rgb(0, 150, 136));

            tabLayout.setVisibility(View.GONE);
            setterViewPagerNotification(viewPager);


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

//        getMenuInflater().inflate(R.menu.home, menu);

        return true;
    }

    public class ViewAdapter extends FragmentStatePagerAdapter{

        private List<Fragment> toplist=new ArrayList<>();
        private List<String>titlelist=new ArrayList<>();

        public ViewAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return toplist.get(position);
        }

        @Override
        public int getCount() {
            return toplist.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titlelist.get(position);
        }


        public void addFragment(Fragment fragment, String string){

            toplist.add(fragment);
            titlelist.add(string);
        }

    }


}
