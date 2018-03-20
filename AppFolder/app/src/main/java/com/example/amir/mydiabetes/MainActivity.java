package com.example.amir.mydiabetes;

import android.Manifest;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements
        AddFragment.OnFragmentInteractionListener,
        CalendarFragment.OnFragmentInteractionListener,
        AboutFragment.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener {
        TextView userName,userEmail;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //request permissions
        if((ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)&&
                (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED))
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.SEND_SMS}, 1);

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 2);


        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 2);

        afterPermissionCheck();
    }
    public void afterPermissionCheck()
    {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        userName = headerView.findViewById(R.id.user_name);
        userEmail = headerView.findViewById(R.id.user_email);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String name = prefs.getString("txt_name", "");
        String email = prefs.getString("edit_text_email", "");
        String phone = prefs.getString("edit_text_emergency", "");
        if(userName.getText().equals("Enter your name"))
            userName.setText(""+name);
        if(userEmail.getText().equals("Enter your email address"))
            userEmail.setText(""+email);

        Fragment fragment = null;
        if(name.equals("") || phone.equals("Enter phone for emergency sms"))
             fragment = (PrefsFragment)new PrefsFragment();
        else
            fragment = new AddFragment();
        //getActionBar().setTitle("Preferences");
        if (fragment != null) {

            android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, fragment);
            ft.commit();
        }
        drawer.closeDrawer(GravityCompat.START);
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

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        String name = prefs.getString("txt_name", "");
        String email = prefs.getString("edit_text_email", "");
        if(userName.getText().equals("Enter your name"))
            userName.setText(""+name);
        if(userEmail.getText().equals("Enter your email address"))
            userEmail.setText(""+email);

        
        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.nav_glucometer) {
            fragment = new AddFragment();
        } else if (id == R.id.nav_planner) {
            fragment = new CalendarFragment();
        }  else if (id == R.id.nav_chart) {
            fragment = new AvgFragment();
        } else if (id == R.id.nav_settings) {
            fragment = (PrefsFragment)new PrefsFragment();
        } else if (id == R.id.nav_about) {
            fragment = new AboutFragment();
        }
        if (fragment != null) {
            prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String phone = prefs.getString("edit_text_emergency", "");
            Log.w("name",userName.getText().toString()+"\n");
            Log.w("phone",phone);
            if("Enter your name".equals(name) || "Enter phone for emergency sms".equals(phone) )
            {
                Toast.makeText(this,"Please fill all details",Toast.LENGTH_LONG).show();
                return false;
            }
            android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, fragment);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(String title) {
        // NOTE:  Code to replace the toolbar title based current visible fragment
        getSupportActionBar().setTitle(title);
    }
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    afterPermissionCheck();
                } else {
                    Log.e("MainActivity","Permissions was denied");
                }
            }
            case 2: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    afterPermissionCheck();
                } else {
                    Log.e("MainActivity", "Permissions was denied");
                }
            }

            default: break;
        }
    }

}
