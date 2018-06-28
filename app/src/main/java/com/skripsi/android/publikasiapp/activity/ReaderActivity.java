package com.skripsi.android.publikasiapp.activity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.skripsi.android.publikasiapp.utils.ActivityUtils;
import com.skripsi.android.publikasiapp.R;
import com.skripsi.android.publikasiapp.app.AppController;
import com.skripsi.android.publikasiapp.helper.SQLiteHandler;
import com.skripsi.android.publikasiapp.utils.DurationUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Harits on 3/1/2018.
 */

public class ReaderActivity extends AppCompatActivity {
    private PDFView pdfView;
//    private AppCompatSeekBar seekBar;
    private ProgressBar progressBar;
    private static final String TAG = "ReaderActivity";
    private boolean unduhflag;

    public static final String PREFERENCE = "preference";
    public static final String PREF_TITLE = "title";
    public static final String PREF_EMAIL = "email";
    public int openPub = 0;
    public int closePub = 0;


    private SQLiteHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        AppController.getInstance().setCurrentTitle(getIntent().getStringExtra("title"));


        android.support.v7.app.ActionBar actionBar = this.getSupportActionBar();
        pdfView=(PDFView)findViewById(R.id.pdfView);

        actionBar.setTitle(getIntent().getStringExtra("title"));
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();

        getIncomingIntent();


        //menaruh shared preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_TITLE,getIntent().getStringExtra("title"));
        editor.putString(PREF_EMAIL,user.get("email"));
        editor.apply();


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

            Log.d(TAG, "getIncomingIntent: found intent extras" );
            String title = getIntent().getStringExtra("title");

            String pdf_name = title + ".pdf";

            initProgressbar();
            downloadPdf(pdf_name);
        }

    }


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

//    private void deletePdf(String filename){
////        Log.d(TAG, "deletePdf: deleted");
////        File file = getFileStreamPath(filename);
////        file.delete();
////    }

    private void openPdf(String filename){
        try {

            File file = getFileStreamPath(filename);
            Log.e("file","file: "+  file.getAbsolutePath());
            progressBar.setVisibility(View.GONE);
            pdfView.setVisibility(View.VISIBLE);

            pdfView.fromFile(file)
                    .onRender(new OnRenderListener() {
                        @Override
                        public void onInitiallyRendered(int nbPages) {
                            Log.d(TAG, "onInitiallyRendered: " + Integer.toString(pdfView.getCurrentPage()));
                        }
                    })
                    .onPageScroll(new OnPageScrollListener() {
                        @Override
                        public void onPageScrolled(int page, float positionOffset) {

                        }
                    })
                    .onLoad(new OnLoadCompleteListener() {
                        @Override
                        public void loadComplete(int nbPages) {
                            openPub = (int) (new Date().getTime()/1000);
                            String halaman = Integer.toString(0);
                            String aktivitas = "START READ";
                            ActivityUtils.addActivityToServer(getIntent().getStringExtra("title"),halaman,aktivitas);

                            Log.d("Event", "catat user mulai baca ke server");
                        }
                    })
                    .enableDoubletap(true)
                    .onTap(new OnTapListener() {
                        @Override
                        public boolean onTap(MotionEvent e) {

                            String halaman = Integer.toString(pdfView.getCurrentPage());
                            String aktivitas = "TAP";
//                            ActivityUtils.addActivityToServer(getIntent().getStringExtra("title"),halaman,aktivitas);

                            Log.d("Event", e.toString());
                            return false;
                        }
                    })
                    .enableAntialiasing(true)
                    .spacing(10)
                    .pageSnap(true)
                    .onPageChange(new OnPageChangeListener() {

                        @Override
                        public void onPageChanged(int page, int pageCount) {
                            Toast.makeText(ReaderActivity.this, "PAGE " + page + "/" + pdfView.getPageCount(), Toast.LENGTH_SHORT).show();
                            Log.d("Event :", "halaman berganti ke"+ pdfView.getCurrentPage());
                        }

                    })
                    .load();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: Mencatatat aktivitas stop reader");
        closePub = (int) (new Date().getTime()/1000);
        String halaman = Integer.toString(pdfView.getCurrentPage());
        String aktivitas = "STOP READ";
        int duration = closePub-openPub;
        ActivityUtils.addActivityToServer(getIntent().getStringExtra("title"),halaman,aktivitas);
        DurationUtils.addDurationToServer(getIntent().getStringExtra("title"),duration);

        super.onStop();
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
