package brdevelopers.com.jobvibe;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    TextView matched,recommended;
    CandidateDetails candidateDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        matched=findViewById(R.id.TV_matched);
        recommended=findViewById(R.id.TV_recommended);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        matched.setOnClickListener(this);
        recommended.setOnClickListener(this);

        candidateDetails=(CandidateDetails)getIntent().getSerializableExtra("candidate");
        loadFragment(new MatchedFragment());


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImageView location = new ImageView(Home.this); // Create an icon
                location.setImageDrawable(getResources().getDrawable(R.drawable.ic_location));

                ImageView company = new ImageView(Home.this); // Create an icon
                company.setImageDrawable(getResources().getDrawable(R.drawable.ic_office));

                ImageView skill = new ImageView(Home.this); // Create an icon
                skill.setImageDrawable(getResources().getDrawable(R.drawable.ic_thought));

                SubActionButton.Builder itemBuilder = new SubActionButton.Builder(Home.this);

                SubActionButton actionButton1 = itemBuilder.setContentView(location).build();
                SubActionButton actionButton2 = itemBuilder.setContentView(company).build();
                SubActionButton actionButton3 = itemBuilder.setContentView(skill).build();

                FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(Home.this)
                        .addSubActionView(actionButton1)
                        .addSubActionView(actionButton2)
                        .addSubActionView(actionButton3)
                        .attachTo(fab)
                        .build();




            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

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
    }

    public void loadFragment(Fragment fragment) {
        //Sending Degree and FOS to Fragments
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
