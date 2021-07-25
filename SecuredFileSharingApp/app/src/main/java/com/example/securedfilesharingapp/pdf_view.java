package com.example.securedfilesharingapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class pdf_view extends AppCompatActivity {

    // creating a variable
    // for PDF view.
    PDFView pdfView;

    // url of our PDF file.
    public static String pdfurl = "",fname="";
    Button bd,bv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);

        bd=(Button)findViewById(R.id.btnDownload);
        bv=(Button)findViewById(R.id.btnView);
        bd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("pdfurl",pdfurl);
                Log.d("fname",fname);
                new DownloadFile().execute(pdfurl, fname);
            }
        });
        bv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdfurl));
                startActivity(browserIntent);


//                String extStorageDirectory = getApplicationContext().getExternalFilesDir(null).getAbsolutePath();
//
//                File pdfFile = new File(extStorageDirectory + "/current/" + fname);  // -> filename = maven.pdf
//                //Uri path = Uri.fromFile(pdfFile);
//                Uri path = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", pdfFile);
//                Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
//                pdfIntent.setDataAndType(path, "application/pdf");
//                pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//                try{
//                    startActivity(pdfIntent);
//                }catch(ActivityNotFoundException e){
//                    Toast.makeText(pdf_view.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
//                }
            }
        });


    }

    private class DownloadFile extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = getApplicationContext().getExternalFilesDir(null).getAbsolutePath();
           // String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "current");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try{
                pdfFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }

            FileDownloader.downloadFile(fileUrl, pdfFile);
            return null;
        }
    }
}