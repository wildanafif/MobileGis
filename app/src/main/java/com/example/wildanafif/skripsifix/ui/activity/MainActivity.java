package com.example.wildanafif.skripsifix.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.wildanafif.skripsifix.R;
import com.example.wildanafif.skripsifix.ui.fragment.IklanFragment;
import com.example.wildanafif.skripsifix.ui.fragment.ProfilFragment;
import com.example.wildanafif.skripsifix.utils.ui.BottomNavigationViewHelper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        //bottom navigation
        navigation = (BottomNavigationView) findViewById(R.id.navigation);

        this.setUiFragment();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigationView.setNavigationItemSelectedListener(this);
        BottomNavigationViewHelper.disableShiftMode(navigation);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new IklanFragment();
                    break;
                case R.id.navigation_list_iklan:

                    return true;
                case R.id.navigation_add_iklan:

                    return true;
                case R.id.navigation_notifications:

                    return true;
                case R.id.navigation_profile:
                    fragment = new ProfilFragment();
                    break;

            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame,fragment)
                    .commit();
            return true;
        }

    };






    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
           startActivity(new Intent(this,RegisterActivity.class));
        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(this,LoginActivity.class));
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setUiFragment(){
        Intent intent = getIntent();
        Fragment fragment = null;
        if(intent.hasExtra("fragment")){
            String status_fragment = intent.getExtras().getString("fragment");
            if (status_fragment.equals("profil")){
                fragment = new ProfilFragment();
                navigation.setSelectedItemId(R.id.navigation_profile);

            }
        }
        else{
            fragment = new IklanFragment();
            navigation.setSelectedItemId(R.id.navigation_home);
        }


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame,fragment)
                .commit();
    }
}
