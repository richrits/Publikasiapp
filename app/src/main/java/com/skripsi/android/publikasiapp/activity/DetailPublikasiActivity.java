package com.skripsi.android.publikasiapp.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.skripsi.android.publikasiapp.R;
import com.skripsi.android.publikasiapp.app.AppConfig;
import com.skripsi.android.publikasiapp.app.AppController;
import com.skripsi.android.publikasiapp.fragment.HomeFragment;
import com.skripsi.android.publikasiapp.helper.SQLiteHandler;
import com.skripsi.android.publikasiapp.helper.SessionManager;
import com.skripsi.android.publikasiapp.model.Publikasi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailPublikasiActivity extends AppCompatActivity {
    private static final String TAG = "DetailPublikasiActivity";
    private Button btnBaca;
    private Button btnRate;
    private Context context;
    private List<Publikasi> publikasiList;
    private ProgressDialog progressDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private String rating;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: startdetailPublikasi");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_publikasi);



        btnBaca = (Button) findViewById(R.id.btnBaca);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("tunggu sebentar");
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());



        ActionBar actionBar = this.getSupportActionBar();

        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        getIncomingIntent();

        btnBaca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: baca");

                Intent i = new Intent(getApplicationContext(), ReaderActivity.class);
                i.putExtra("pdf",getIntent().getStringExtra("pdf"));
                i.putExtra("title",getIntent().getStringExtra("title"));
                startActivity(i);
            }
        });

//        btnRate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "onClick: rating");
//
//                HashMap<String, String> user = db.getUserDetails();
//                String user_id = user.get("uid");
//                String publikasi_title = getIntent().getStringExtra("title");
//                rating = Float.toString(ratingBar.getRating());
//
//                ratingValue.setText(rating);
//                ratingButtonLinearLayout.setVisibility(View.GONE);
//                ratingValueLinearLayout.setVisibility(View.VISIBLE);
////
////                getRating(user_id,publikasi_title,rating);
//
//
//            }
//        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = NavUtils.getParentActivityIntent(this);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                NavUtils.navigateUpTo(this,intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void getIncomingIntent(){
        Log.d(TAG, "getIncomingIntent: started");

        if (getIntent().hasExtra("cover") && getIntent().hasExtra("title")){
            Log.d(TAG, "getIncomingIntent: found intent extras");

            String cover = getIntent().getStringExtra("cover");
            String title = getIntent().getStringExtra("title");
            String noKatalog = getIntent().getStringExtra("no_katalog");
            String noPublikasi = getIntent().getStringExtra("no_publikasi");
            String issn = getIntent().getStringExtra("issn");
            String tanggalRilis = getIntent().getStringExtra("tanggal_rilis");
            String ukuranFile = getIntent().getStringExtra("ukuran_file");
            String pdf = getIntent().getStringExtra("pdf");
            String _abstract = getIntent().getStringExtra("_abstract");

            setDetail(cover,title,noKatalog,noPublikasi,issn,tanggalRilis,ukuranFile,_abstract);


        }

    }

    private void setDetail(String cover, String title, String noKatalog, String noPublikasi, String issn, String tanggalRilis, String ukuranFile, String _abstract){
        Log.d(TAG, "setDetail: detail mulai disetting");

        ImageView coverPub = findViewById(R.id.cover);
        Glide.with(this)
                .asBitmap()
                .load(cover)
                .into(coverPub);

        TextView titlePub = findViewById(R.id.title);
        TextView no_katalog = findViewById(R.id.no_katalog);
        TextView no_publikasi = findViewById(R.id.no_publikasi);
        TextView issnPub = findViewById(R.id.issn);
        TextView tanggal_rilis = findViewById(R.id.tgl_rilis);
        TextView uk_file = findViewById(R.id.ukuran_file);
        TextView abstractPub = findViewById(R.id._abstract);

        titlePub.setText(title);
        no_katalog.setText(noKatalog);
        no_publikasi.setText(noPublikasi);
        issnPub.setText(issn);
        tanggal_rilis.setText(tanggalRilis);
        uk_file.setText(ukuranFile);
        abstractPub.setText(_abstract);
    }

//
//    private void permission_check(){
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
//            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
//                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
//                return;
//            }
//        }
//        initialize();
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode==100 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
//            initialize();
//        }else{
//            permission_check();
//        }
//    }
//
//    private void initialize(){
//
//    }

//    private void getRating(final String user_id, final String publikasi_title, final String publikasi_rating){
//        Log.d(TAG, "getRating: start");
//
//        StringRequest strReq = new StringRequest(com.android.volley.Request.Method.POST, AppConfig.URL_ACTIVITY_USER, new com.android.volley.Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d(TAG, "onResponse: add activity response");
//                try {
//                    JSONObject jobj = new JSONObject(response);
//                    boolean error = jobj.getBoolean("error");
//                    if (!error) {
//                        //activity sukses ke database
//                        //masukkan ke sqlite
//                        JSONObject activity = jobj.getJSONObject("activity");
//                        String user_id = activity.getString("user_id");
//                        String publikasi_title = activity.getString("publikasi_title");
//                        String publikasi_rating = activity.getString("publikasi_rating");
//
//                        //masukkan ke db
//                        db.addUserActivity(user_id, publikasi_title, publikasi_rating);
//                    } else {
//                        String errorMsg = jobj.getString("error_msg");
//                        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "onErrorResponse: Error : " + error.getMessage());
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams(){
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("user_id", user_id);
//                params.put("publikasi_title", publikasi_title);
//                params.put("publikasi_rating", publikasi_rating);
//                return params;
//            }
//        };
//
//        //add to request
//        AppController.getInstance().addToRequestQueue(strReq);
//    }

}
