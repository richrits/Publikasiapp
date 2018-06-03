package com.skripsi.android.publikasiapp.activity;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
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


import com.skripsi.android.publikasiapp.BuildConfig;
import com.skripsi.android.publikasiapp.R;
import com.skripsi.android.publikasiapp.fragment.HomeFragment;
import com.skripsi.android.publikasiapp.fragment.ProfileFragment;
import com.skripsi.android.publikasiapp.fragment.SearchFragment;
import com.skripsi.android.publikasiapp.fragment.SearchResultsFragment;
import com.skripsi.android.publikasiapp.helper.SQLiteHandler;
import com.skripsi.android.publikasiapp.helper.SessionManager;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private static final String TAG = "MainActivity";
    private ActionBar toolbar;
    Fragment fragment;


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // SqLite database handler
        db = new SQLiteHandler(this);

        // session manager
        session = new SessionManager(this);



        if (!session.isLoggedIn()) {
            logoutUser();
        }

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
        switch (item.getItemId()) {
            case R.id.navigation_home:
                Log.d(TAG, "onNavigationItemSelected: home");
                toolbar.setTitle("Publikasi");
                fragment = HomeFragment.getInstance();
                loadFragment(fragment);
                return true;
            case R.id.navigation_search:
                Log.d(TAG, "onNavigationItemSelected: search");
               // toolbar.setTitle("Cari");
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
///errrorr disini
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: start");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getString(R.string.action_search));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                              @Override
                                              public boolean onQueryTextSubmit(String query) {
                                                  Log.d(TAG, "onQueryTextSubmit: start");
                                                  if (query.length()>0){
                                                      fragment = new SearchResultsFragment();
                                                      Bundle args1 = new Bundle();
                                                      Bundle args2 = new Bundle();
                                                      String tipe = "search";

                                                      args1.putString("query_string",query);
                                                      args2.putString("search",tipe);
                                                      fragment.setArguments(args1);

                                                      loadFragment(fragment);
                                                  }
                                                  return false;
                                              }
                                              @Override
                                              public boolean onQueryTextChange(String query) {
                                                  return false;
                                              }
                                          }
        );
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
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    private void permission_check(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
                return;
            }
        }
        initialize();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==100 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            initialize();
        }else{
            permission_check();
        }
    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void initialize(){
        loadFragment(HomeFragment.getInstance());
    }
}
