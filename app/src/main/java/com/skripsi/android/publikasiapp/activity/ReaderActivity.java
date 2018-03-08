package com.skripsi.android.publikasiapp.activity;

import android.app.Activity;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;
import com.skripsi.android.publikasiapp.R;

/**
 * Created by Harits on 3/1/2018.
 */

public class ReaderActivity extends Activity {
    public PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        pdfView=(PDFView)findViewById(R.id.pdfView);
        pdfView.fromAsset("statkrim.pdf").defaultPage(1)
                .enableSwipe(true)
                .spacing(10)
                .load();
    }
}
