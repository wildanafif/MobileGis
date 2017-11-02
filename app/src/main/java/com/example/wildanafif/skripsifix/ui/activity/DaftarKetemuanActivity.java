package com.example.wildanafif.skripsifix.ui.activity;

/**
 * Created by wildan afif on 5/21/2017.
 */

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.example.wildanafif.skripsifix.R;
import com.example.wildanafif.skripsifix.ui.fragment.Fragment_DaftarKetemuanDikirim;
import com.example.wildanafif.skripsifix.ui.fragment.Fragment_DaftarKetemuanDiterima;


public class DaftarKetemuanActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private String back=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_ketemuan);
        setTitle("Daftar Ketemuan");

        Intent intent = getIntent();

// Get the extras (if there are any)
        if (intent.hasExtra("back")){
            this.back=intent.getStringExtra("back");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);



    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        if (back!=null){
            if (back.matches("profil")){
                finishAffinity();
                Intent intent= new Intent(DaftarKetemuanActivity.this, MainActivity.class);
                intent.putExtra("fragment","profil");
                startActivity(intent);
            }
            //Toast.makeText(this, ""+back, Toast.LENGTH_SHORT).show();

        }else{
            finishAffinity();
            Intent intent= new Intent(DaftarKetemuanActivity.this, MainActivity.class);
            intent.putExtra("fragment","profil");
            startActivity(intent);
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position){
                case 0:
                    Fragment_DaftarKetemuanDiterima fragment1=new Fragment_DaftarKetemuanDiterima();
                    return  fragment1;
                case 1:
                    Fragment_DaftarKetemuanDikirim fragment2= new Fragment_DaftarKetemuanDikirim();
                    return  fragment2;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Diterima";
                case 1:
                    return "Terkirim";

            }
            return null;
        }
    }
}

