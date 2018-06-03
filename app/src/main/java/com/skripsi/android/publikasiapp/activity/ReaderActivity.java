package com.skripsi.android.publikasiapp.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.skripsi.android.publikasiapp.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Harits on 3/1/2018.
 */

public class ReaderActivity extends AppCompatActivity {
    private PDFView pdfView;
//    private AppCompatSeekBar seekBar;
    private ProgressBar progressBar;
    private static final String TAG = "ReaderActivity";
    private boolean unduhflag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);


        android.support.v7.app.ActionBar actionBar = this.getSupportActionBar();
        pdfView=(PDFView)findViewById(R.id.pdfView);

        actionBar.setTitle(getIntent().getStringExtra("title"));
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        getIncomingIntent();

//        final Button unduh = (Button)findViewById(R.id.unduhbutton);
//        unduh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(!unduhflag){
//
//                }
//            }
//        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
//            case R.id.unduhbutton:
//                unduhflag = true;
//                Toast.makeText(this,"pdf terunduh silahkan lihat pada profil -> unduhan",Toast.LENGTH_LONG).show();
//                return true;
            case android.R.id.home:
                Intent intent = NavUtils.getParentActivityIntent(this);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                NavUtils.navigateUpTo(this,intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reader_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void getIncomingIntent(){
        Log.d(TAG, "getIncomingIntent: started");

        if (getIntent().hasExtra("pdf")){
            Log.d(TAG, "getIncomingIntent: found intent extras");
            String title = getIntent().getStringExtra("title");

            String pdf_name = title + ".pdf";

//            initSeekbar();
            initProgressbar();
            downloadPdf(pdf_name);
        }

    }

//    private void initSeekbar(){
//        seekBar = findViewById(R.id.seekbar);
//        seekBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
//    }

    private void initProgressbar(){
        progressBar = findViewById(R.id.progressbar);
        progressBar.getProgressDrawable();
    }

    @SuppressLint("StaticFieldLeak")
    private void downloadPdf(final String filename){

        new AsyncTask<Void,Integer,Boolean>(){
            File file = getFileStreamPath(filename);
            @Override
            protected Boolean doInBackground(Void... voids) {
                return downloadPdf();
            }

            private Boolean downloadPdf() {
                try{
                   if(file.exists())
                        return true;
                    try{
                        FileOutputStream fileOutputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                        URL u = new URL(getIntent().getStringExtra("pdf"));
                        URLConnection conn = u.openConnection();
                        int contentLength = conn.getContentLength();
                        InputStream inputStream = new BufferedInputStream(u.openStream());
                        byte data[] = new byte[contentLength];
                        long total = 0;
                        int count;
                        while ((count=inputStream.read(data))!=-1){
                            total += count;
                            publishProgress((int)((total*100)/contentLength));
                            fileOutputStream.write(data,0,count);
                        }
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        inputStream.close();
                        return true;
                    }catch (final Exception e){
                        e.printStackTrace();
                        return false;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                progressBar.setProgress(values[0]);
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (aBoolean){
                    openPdf(filename);

                }else{
                    Toast.makeText(ReaderActivity.this, "unable to download PDF",Toast.LENGTH_SHORT).show();
                }
            }


        }.execute();

    }

    private void deletePdf(String filename){
        Log.d(TAG, "deletePdf: deleted");
        File file = getFileStreamPath(filename);
        file.delete();
    }

    // TODO: 6/3/2018 pengumpul data user? 
    private void openPdf(String filename){
        try {
            File file = getFileStreamPath(filename);
            Log.e("file","file: "+  file.getAbsolutePath());
            progressBar.setVisibility(View.GONE);
            pdfView.setVisibility(View.VISIBLE);
            pdfView.fromFile(file)
                    .onTap(new OnTapListener() {
                        @Override
                        public boolean onTap(MotionEvent e) {
                            Log.d("Event", e.toString());
                            return false;
                        }
                    })
                    .swipeHorizontal(false)
                    .enableAntialiasing(true)
                    .spacing(1)
                    .pageSnap(true)
                    .onPageChange(new OnPageChangeListener() {
                        @Override
                        public void onPageChanged(int page, int pageCount) {
//                            Toast.makeText(ReaderActivity.this, "PAGE " + page + "/" + pageCount, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .load();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (getIntent().hasExtra("pdf")){
//            String title = getIntent().getStringExtra("title");
//
//            String pdf_name = title + ".pdf";
//            if(!unduhflag){
//                deletePdf(pdf_name);
//            }
//        }
//    }
}
