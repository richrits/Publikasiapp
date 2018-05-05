package com.skripsi.android.publikasiapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.app.SearchManager;
import android.support.v7.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;


import com.skripsi.android.publikasiapp.R;
import com.skripsi.android.publikasiapp.fragment.HomeFragment;
import com.skripsi.android.publikasiapp.fragment.ProfileFragment;
import com.skripsi.android.publikasiapp.fragment.SearchFragment;
import com.skripsi.android.publikasiapp.helper.SQLiteHandler;
import com.skripsi.android.publikasiapp.helper.SessionManager;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    private ActionBar toolbar;


    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = getSupportActionBar();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        toolbar.setTitle("Publikasi");
        loadFragment(HomeFragment.getInstance());


    }

    private void loadFragment(Fragment fragment) {
        Log.d(TAG, "loadFragment: started");

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frame_container, fragment).commit();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.navigation_home:
                Log.d(TAG, "onNavigationItemSelected: home");
                toolbar.setTitle("Publikasi");
                fragment = HomeFragment.getInstance();
                loadFragment(fragment);
                return true;
            case R.id.navigation_search:
                Log.d(TAG, "onNavigationItemSelected: search");
                toolbar.setTitle("Cari");
                fragment = SearchFragment.getInstance();
                loadFragment(fragment);
                return true;
            case R.id.navigation_profile:
                Log.d(TAG, "onNavigationItemSelected: profile");
                toolbar.setTitle("Profil");
                fragment = ProfileFragment.getInstance();
                loadFragment(fragment);
                return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)
                MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }


        });
        return true;
    }
    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: started");
        new AlertDialog.Builder(this).setTitle("Keluar?").setMessage("Apakah anda ingin keluar dari aplikasi?").setNegativeButton(android.R.string.no,null).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.super.onBackPressed();
            }
        }).create().show();

    }
}
