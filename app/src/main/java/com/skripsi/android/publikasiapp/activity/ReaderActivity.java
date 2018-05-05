package com.skripsi.android.publikasiapp.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.app.NavUtils;
import android.util.Log;

import com.github.barteksc.pdfviewer.PDFView;
import com.skripsi.android.publikasiapp.R;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Harits on 3/1/2018.
 */

public class ReaderActivity extends Activity {
    public PDFView pdfView;
    private static final String TAG = "ReaderActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        getIncomingIntent();

        pdfView=(PDFView)findViewById(R.id.pdfView);

        getIncomingIntent();


//        pdfView.fromAsset("statkrim.pdf").defaultPage(1)
//                .enableSwipe(true)
//                .spacing(10)
//                .load();

    }

    private void getIncomingIntent(){
        Log.d(TAG, "getIncomingIntent: started");

        if (getIntent().hasExtra("pdf")){
            Log.d(TAG, "getIncomingIntent: found intent extras");

            String pdf = getIntent().getStringExtra("pdf");

          //  pdfView.fromStream()

//            byte[] byteArray=null;
//            try {
//                InputStream inputStream = new FileInputStream(pdf);
//
//
//                String inputStreamToString = inputStream.toString();
//                byteArray = inputStreamToString.getBytes();
//
//                inputStream.close();
//            } catch (IOException e) {
//                System.out.println("IO Ex"+e);
//            }
//
//            pdfView.fromBytes(byteArray)
//                    .defaultPage(1)
//                    .enableSwipe(true)
//                    .spacing(10)
//                    .load();

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
    }
}
