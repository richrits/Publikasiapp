package com.skripsi.android.publikasiapp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.skripsi.android.publikasiapp.R;
import com.skripsi.android.publikasiapp.model.Publikasi;

import java.util.List;

public class DetailPublikasiActivity extends AppCompatActivity {
    private static final String TAG = "DetailPublikasiActivity";
    private Button btnBaca;
    private Button btnUnduh;
    private Context context;
    private List<Publikasi> publikasiList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: startdetailPublikasi");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_publikasi);

        btnBaca = (Button) findViewById(R.id.btnBaca);
        btnUnduh = (Button) findViewById(R.id.btnUnduh);

        ActionBar actionBar = this.getSupportActionBar();

        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        getIncomingIntent();

        btnBaca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ReaderActivity.class);
                i.putExtra("pdf",getIntent().getStringExtra("pdf"));
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
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

}
