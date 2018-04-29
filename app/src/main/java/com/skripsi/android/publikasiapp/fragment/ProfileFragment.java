package com.skripsi.android.publikasiapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.skripsi.android.publikasiapp.R;
import com.skripsi.android.publikasiapp.activity.LoginActivity;
import com.skripsi.android.publikasiapp.activity.MainActivity;
import com.skripsi.android.publikasiapp.helper.SQLiteHandler;
import com.skripsi.android.publikasiapp.helper.SessionManager;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private TextView txtName;
    private TextView txtEmail;
    private Button btnLogout;
    private static ProfileFragment INSTANCE = null;
    private SQLiteHandler db;
    private SessionManager session;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment getInstance() {
        Log.d(TAG, "getInstance: fragment profile terpanggil");
        if (INSTANCE == null) {
            INSTANCE = new ProfileFragment();
        }

        return INSTANCE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        rootView.setTag(TAG);
        txtName = (TextView) rootView.findViewById(R.id.name);
        txtEmail = (TextView) rootView.findViewById(R.id.email);


        // SqLite database handler
        db = new SQLiteHandler(getActivity().getApplicationContext());

        // session manager
        session = new SessionManager(getActivity().getApplicationContext());

//        if (!session.isLoggedIn()) {
//            logoutUser();
//        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");

        // Displaying the user details on the screen
        txtName.setText(name);
        txtEmail.setText(email);

        return rootView;

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_settings).setVisible(true);
        menu.findItem(R.id.action_logout).setVisible(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Log.d(TAG, "onOptionsItemSelected: setting selected");

//            Intent startSettingsActivity = new Intent(this,SettingsActivity.class);
//            startActivity(startSettingsActivity);
            return true;
        } else if (id == R.id.action_logout) {
            Log.d(TAG, "onOptionsItemSelected: logout selected");
            logoutUser();
        }
        return super.onOptionsItemSelected(item);
    }


}
