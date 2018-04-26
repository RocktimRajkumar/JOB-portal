package brdevelopers.com.jobvibe;

import android.Manifest;
import android.annotation.SuppressLint;

import android.app.AlertDialog;
import android.content.DialogInterface;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
   private final int REQUEST_CODE_GALLERY=999;
   private Toolbar toolbar;
   private LinearLayout layoutHome,layoutActivity,layoutNotify;
   public static LinearLayout layoutbottom;


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
        layoutHome=findViewById(R.id.layoutHome);
        layoutActivity=findViewById(R.id.layoutActivity);
        layoutNotify=findViewById(R.id.layoutNotification);
        layoutbottom=findViewById(R.id.RL_buttons);


        tabLayout.setupWithViewPager(viewPager);
        setterViewPager(viewPager);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        iv_home.setOnClickListener(this);
        iv_activity.setOnClickListener(this);
        iv_notification.setOnClickListener(this);
        tv_home.setOnClickListener(this);
        tv_activity.setOnClickListener(this);
        tv_notification.setOnClickListener(this);
        layoutHome.setOnClickListener(this);
        layoutActivity.setOnClickListener(this);
        layoutNotify.setOnClickListener(this);

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

        iv_profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityCompat.requestPermissions(Home.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_GALLERY);

            }
        });

        tv_empname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tv_empname.setText(name);
        tv_empemail.setText(canemail);

        navigationView.setNavigationItemSelectedListener(this);

        iv_home.setImageResource(R.drawable.ic_onhome);
        tv_home.setVisibility(View.VISIBLE);
        iv_activity.setImageResource(R.drawable.ic_activity);
        iv_notification.setImageResource(R.drawable.ic_notification);

        loadProfilePic();
    }

    private void loadProfilePic() {

        DBManager db=new DBManager(this);
        byte[] byteimg=db.getImage(Home.canemail);
        if(byteimg!=null){
            Bitmap bitimg=BitmapFactory.decodeByteArray(byteimg, 0, byteimg.length);
            try{
                iv_profileImage.setImageBitmap(bitimg);
            }
            catch (Exception ex)
            {
                Log.d("logcheck","exception "+ex);
            }
        }
    }

    //Permission menu for access gallery or camera
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==REQUEST_CODE_GALLERY){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){

                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE_GALLERY);
            }
            else{
                Toast.makeText(this, "You don't have permission to access file!.", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //Setting image to image View after permission granted
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==REQUEST_CODE_GALLERY && resultCode==RESULT_OK && data!=null) {

            try {
                Uri uri = data.getData();
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                iv_profileImage.setImageBitmap(bitmap);
                addImgToDb(bitmap);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    //Adding image to database
    private void addImgToDb(Bitmap bitmap) {


        byte[] profieimg=imageViewtoByte(iv_profileImage);
        DBManager db=new DBManager(this);
        boolean bol=db.isImgExists(Home.canemail);
        if(!bol){
            db.insertImage(Home.canemail,profieimg);
        }
        else if(bol){
            db.updateImage(Home.canemail,profieimg);
        }

    }


    //Converting image to byte
    private byte[] imageViewtoByte(ImageView iv_profileImage) {

        Bitmap bitmap=((BitmapDrawable)iv_profileImage.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,80,byteArrayOutputStream);
        byte[] bytearray=byteArrayOutputStream.toByteArray();
        return bytearray;
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

            View v=findViewById(android.R.id.content);
            Snackbar.make(v,"Press back again to exit "+Html.fromHtml("&#9995;"),Snackbar.LENGTH_SHORT).show();

            onbackpressed=true;
        }
        else{
            super.onBackPressed();
        }
        new CountDownTimer(3000,1000){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                onbackpressed=false;
            }
        }.start();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_editProfile) {
            Intent editProfile = new Intent(Home.this, EditActivity.class);
            startActivity(editProfile);
            overridePendingTransition(R.anim.topttobottom,R.anim.bottomtotop);
            finish();
        } else if (id == R.id.nav_faq) {
            Intent intent=new Intent(Home.this,FAQ.class);
            startActivity(intent);
            overridePendingTransition(R.anim.topttobottom,R.anim.bottomtotop);
            finish();

        } else if (id == R.id.nav_terms) {
            Intent intent=new Intent(Home.this,Terms.class);
            startActivity(intent);
            overridePendingTransition(R.anim.topttobottom,R.anim.bottomtotop);
            finish();

        } else if (id == R.id.nav_report) {
            AlertDialog.Builder report=new AlertDialog.Builder(Home.this);
            View reportView=getLayoutInflater().inflate(R.layout.problem_layout,null);
            EditText problem=reportView.findViewById(R.id.ET_subject);

            report.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(Home.this,"Report Successfully Sent",Toast.LENGTH_LONG).show();


                }
            });
            report.setNegativeButton("Abort", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            report.setView(reportView);
            report.show();


        } else if (id == R.id.nav_share) {

            Intent share=new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            String body="Download Job Vibe!";
            String sub="The best Job Portal app in India.";
            share.putExtra(Intent.EXTRA_TEXT,body);
            share.putExtra(Intent.EXTRA_SUBJECT,sub);
            share.putExtra(Intent.EXTRA_TEXT,"https://drive.google.com/file/d/1qFcvVtGTXOYAr2CCbauyxG9hJUvATyej/view?usp=sharing");
            startActivity(Intent.createChooser(share,"Share Using"));

        }
        else if (id == R.id.nav_about) {
            Intent intent=new Intent(Home.this,AboutUs.class);
            startActivity(intent);
            overridePendingTransition(R.anim.topttobottom,R.anim.bottomtotop);
            finish();

        }
        else if(id==R.id.nav_logout){

            SharedPreferences sharedPreferences=getSharedPreferences("Data",MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("email","");
            editor.putString("password","");
            editor.commit();

            Intent login=new Intent(Home.this,Login.class);
            startActivity(login);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {

        if (v.getId()==R.id.IV_home || v.getId()==R.id.TV_home || v.getId()==R.id.layoutHome) {
            iv_home.setImageResource(R.drawable.ic_onhome);
            tv_home.setVisibility(View.VISIBLE);
            tv_activity.setVisibility(View.GONE);
            tv_notification.setVisibility(View.GONE);
            iv_activity.setImageResource(R.drawable.ic_activity);
            iv_notification.setImageResource(R.drawable.ic_notification);

            toolbar.setTitle(R.string.title_activity_home);

            tabLayout.setVisibility(View.VISIBLE);
            tabLayout.setupWithViewPager(viewPager);
            setterViewPager(viewPager);

        } else if (v.getId()==R.id.IV_activity || v.getId()==R.id.TV_activity ||  v.getId()==R.id.layoutActivity) {
            iv_activity.setImageResource(R.drawable.ic_onactivity);
            tv_activity.setVisibility(View.VISIBLE);
            tv_notification.setVisibility(View.GONE);
            tv_home.setVisibility(View.GONE);
            iv_home.setImageResource(R.drawable.ic_home);
            iv_notification.setImageResource(R.drawable.ic_notification);

            toolbar.setTitle(R.string.title_activity_activity);

            tabLayout.setVisibility(View.VISIBLE);
            tabLayout.setupWithViewPager(viewPager);
            setterViewPagerActivity(viewPager);


        } else if (v.getId()==R.id.IV_notification || v.getId()==R.id.TV_notification || v.getId()==R.id.layoutNotification) {
            iv_notification.setImageResource(R.drawable.ic_onnotification);
            tv_notification.setVisibility(View.VISIBLE);
            tv_home.setVisibility(View.GONE);
            tv_activity.setVisibility(View.GONE);
            iv_home.setImageResource(R.drawable.ic_home);
            iv_activity.setImageResource(R.drawable.ic_activity);

            toolbar.setTitle(R.string.title_activity_notification);

            tabLayout.setVisibility(View.GONE);
            setterViewPagerNotification(viewPager);

        }


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
