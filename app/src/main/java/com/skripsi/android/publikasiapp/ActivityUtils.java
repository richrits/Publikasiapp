package com.skripsi.android.publikasiapp;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.skripsi.android.publikasiapp.app.AppConfig;
import com.skripsi.android.publikasiapp.app.AppController;
import com.skripsi.android.publikasiapp.helper.SQLiteHandler;

import java.util.HashMap;
import java.util.Map;

public class ActivityUtils {
    private static final String TAG = "ActivityUtils";

    public static void addActivityToServer(final String title, final String halaman, final String aktivitas){

        // Fetching user details from sqlite
        HashMap<String, String> user = new SQLiteHandler(AppController.getInstance()).getUserDetails();
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        final String email = user.get("email");
//        final String title = sharedPreferences.getString(PREF_TITLE,"tidak ada judul");
//        final String title = getIntent().getStringExtra("title");
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, AppConfig.URL_ACTIVITY_USER, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Berhasil menambahkan aktivitas: " + response.toString());

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "gagal menambahkan aktivitas"+ error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email",email);
                params.put("title",title);
                params.put("halaman",halaman);
                params.put("aktivitas",aktivitas);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

}
