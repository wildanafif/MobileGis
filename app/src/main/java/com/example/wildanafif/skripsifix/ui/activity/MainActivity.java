package com.example.wildanafif.skripsifix.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.wildanafif.skripsifix.MapsActivity;
import com.example.wildanafif.skripsifix.R;
import com.example.wildanafif.skripsifix.TestNotifikasActivity;
import com.example.wildanafif.skripsifix.entitas.firebase.AuthFirebase;
import com.example.wildanafif.skripsifix.entitas.ui.MessageDialog;
import com.example.wildanafif.skripsifix.ui.fragment.ChatFragment;
import com.example.wildanafif.skripsifix.ui.fragment.IklanFragment;
import com.example.wildanafif.skripsifix.ui.fragment.ProfilFragment;
import com.example.wildanafif.skripsifix.utils.ui.BottomNavigationViewHelper;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Serializable {

    private BottomNavigationView navigation;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();

        //navigationView = (NavigationView) findViewById(R.id.nav_view);

        //bottom navigation
        navigation = (BottomNavigationView) findViewById(R.id.navigation);

        this.setUiFragment();
//        this.hideNavigasi();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
       // navigationView.setNavigationItemSelectedListener(this);
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
//                case R.id.navigation_list_iklan:
//
//                    return true;
                case R.id.navigation_add_iklan:

                    AuthFirebase authFirebase=new AuthFirebase();
                    boolean login=authFirebase.isLogin();
                    if (login){

                        Intent intent= new Intent(MainActivity.this, PasangIklanActivity.class);
                        finish();
                        startActivity(intent);
                    }else{
                        MessageDialog messageDialog=new MessageDialog(MainActivity.this);
                        messageDialog.setMessage("Fitur ini membutuhkan akses login, silahkan login terlebih dahulu");
                        messageDialog.show();
                    }

                    return true;
                case R.id.navigation_chat:
                    fragment = new ChatFragment();
                    break;
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

        if (id == R.id.nav_register) {
           startActivity(new Intent(this,RegisterActivity.class));
        } else if (id == R.id.nav_login) {
            startActivity(new Intent(this,LoginActivity.class));
        } else if (id == R.id.nav_logout) {
            startActivity(new Intent(this,ChatActivity.class));
        } else if (id == R.id.nav_profil) {

        } else if (id == R.id.nav_share) {
            startActivity(new Intent(MainActivity.this, TestNotifikasActivity.class));
        } else if (id == R.id.nav_send) {
            startActivity(new Intent(MainActivity.this, MapsActivity.class));
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


    private void hideNavigasi() {

        Menu nav_Menu = navigationView.getMenu();
        AuthFirebase authFirebase=new AuthFirebase();
        FirebaseUser user=authFirebase.getUserLogin();

        if (user==null){
            nav_Menu.findItem(R.id.nav_profil).setVisible(false);
            nav_Menu.findItem(R.id.nav_logout).setVisible(false);

        }else{
            nav_Menu.findItem(R.id.nav_register).setVisible(false);
            nav_Menu.findItem(R.id.nav_login).setVisible(false);
        }

    }
}
