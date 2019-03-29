package com.application.amrudesh.blazewallpaper.UI;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.application.amrudesh.blazewallpaper.Fragments.FragmentAll;
import com.application.amrudesh.blazewallpaper.Fragments.FragmentTop;
import com.application.amrudesh.blazewallpaper.Model.ViewPageAdapter;
import com.application.amrudesh.blazewallpaper.R;
import com.crashlytics.android.Crashlytics;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

public class Navigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    @BindView(R.id.search_view)
    MaterialSearchView searchView;
    @BindView(R.id.no_internet)
    LottieAnimationView animationViewInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_navigation);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Blaze Wallpaper");
        // Adding Fragments to Main Screen
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        if (haveNetwork()) {
            ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());
            viewPageAdapter.AddFragments(new FragmentAll(), "Latest");
            viewPageAdapter.AddFragments(new FragmentTop(), "Top Rated");
            viewPager.setAdapter(viewPageAdapter);
            tabLayout.setupWithViewPager(viewPager);
        } else {
            viewPager.setVisibility(View.GONE);
            animationViewInternet.setVisibility(View.VISIBLE);
            animationViewInternet.playAnimation();

        }
        // Drawer Start From here
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
        getMenuInflater().inflate(R.menu.navigation, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent i = new Intent(getApplicationContext(), SearchableActivity.class);
                i.putExtra("query", query);
                startActivity(i);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            startActivity(new Intent(this, Favourites.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private Boolean haveNetwork() {
        boolean wifi = false;
        boolean have_MobileData = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();

        for (NetworkInfo into : networkInfos) {
            if (into.getTypeName().equalsIgnoreCase("WIFI")) {
                if (into.isConnected()) {
                    wifi = true;
                }
            }
            if (into.getTypeName().equalsIgnoreCase("MOBILE")) {
                if (into.isConnected()) {
                    have_MobileData = true;
                }
            }
        }
        return wifi || have_MobileData;
    }


}
