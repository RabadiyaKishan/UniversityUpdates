package com.ematrix.infotech.uniupdates.Activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ematrix.infotech.uniupdates.Firebase.MyFirebaseMessagingService;
import com.ematrix.infotech.uniupdates.Fragment.FragmentChangeLanguage;
import com.ematrix.infotech.uniupdates.Fragment.FragmentChangeStream;
import com.ematrix.infotech.uniupdates.Fragment.FragmentChangeUniversity;
import com.ematrix.infotech.uniupdates.Fragment.FragmentLetestUpdates;
import com.ematrix.infotech.uniupdates.R;
import com.ematrix.infotech.uniupdates.Utils.Utils;
import com.google.android.material.navigation.NavigationView;

public class ActivityDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Context mContext;
    private String UserName, Email, ImageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mContext = ActivityDashboard.this;

        FragmentLetestUpdates mFragmentLetestUpdates = new FragmentLetestUpdates();
        setFragment(mFragmentLetestUpdates);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Dashboard");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nvView);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
    }

    protected void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContent, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        ActivityExit.ExitApplication(mContext);
    }

    private void displaySelectedScreen(int itemId) {
        Fragment fragment = null;
        switch (itemId) {

            case R.id.nav_Updates:
                FragmentLetestUpdates mFragmentLetestUpdates = new FragmentLetestUpdates();
                setFragment(mFragmentLetestUpdates);
                break;
            case R.id.nav_university:
                FragmentChangeUniversity mFragmentChangeUniversity = new FragmentChangeUniversity();
                setFragment(mFragmentChangeUniversity);
                break;
            case R.id.nav_stream:
                FragmentChangeStream mFragmentChangeStream = new FragmentChangeStream();
                setFragment(mFragmentChangeStream);
                break;
            case R.id.nav_lang:
                FragmentChangeLanguage mFragmentChangeLanguage = new FragmentChangeLanguage();
                setFragment(mFragmentChangeLanguage);
                break;
            case R.id.nav_feedback:
                Utils.Feedback(mContext);
                break;
            case R.id.nav_share:
                Utils.ShareApp(mContext);
                break;
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContent, fragment);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void Feedback() {
        String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }
}