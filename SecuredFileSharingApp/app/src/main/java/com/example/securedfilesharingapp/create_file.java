package com.example.securedfilesharingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class create_file extends AppCompatActivity {
    Button btnClick,btnSave,btnUpload,bCreate;
    public static String imageFileName="",path="";
    public int CAMERA_REQUEST = 1;
    private static final int REQUEST_STORAGE_PERMISSION = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    String fname="";
    ImageView iv;
    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;
    int serverResponseCode = 0;
    private String selectedFilePath;
    EditText etFName;
    String imgString;
    public static String res="";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap mResultsBitmap;
    private String mTempPhotoPath;
    private static final String FILE_PROVIDER_AUTHORITY = "com.mydomain.fileprovider";
    Activity a;
    private AppExecutor mAppExcutor;
    public static ArrayList al=new ArrayList();
    int count=0;
    String fpath ="";
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_file);
        btnClick = (Button)findViewById(R.id.b1);
        btnSave = (Button)findViewById(R.id.b2);
        btnUpload = (Button)findViewById(R.id.b3);
        bCreate = (Button)findViewById(R.id.bCreate);
        tv = (TextView) findViewById(R.id.tvCount);
        iv = (ImageView)findViewById(R.id.imageView2);
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
        verifyStoragePermissions(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        etFName=(EditText)findViewById(R.id.etFName);

        a=this;

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)iv.getLayoutParams();
        params.width = dpToPx(400,getApplicationContext());
        params.height = dpToPx(400,getApplicationContext());
        iv.setLayoutParams(params);
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fname=etFName.getText().toString();
                etFName.setEnabled(false);
// Check for the external storage permission
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // If you do not have permission, request it
                    ActivityCompat.requestPermissions(a,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_STORAGE_PERMISSION);
                } else {
                    // Launch the camera if the permission exists
                    launchCamera();
                }
            }
        });
        mAppExcutor = new AppExecutor();
        btnSave.setOnClickListener((View v) -> {
            mAppExcutor.diskIO().execute(() -> {
                // Delete the temporary image file
                BitmapUtils.deleteImageFile(this, mTempPhotoPath);
                Log.d("??",mTempPhotoPath);
                al.add(fname+"_"+count+".png");

                // Save the image
                WriteFileString(fname+"_"+count+".png",mResultsBitmap);
                //   BitmapUtils.saveImage(this, mResultsBitmap);
                count++;
                tv.setText(count+" Image Captured ");

            });

            Toast.makeText(this,"Image Save",Toast.LENGTH_LONG).show();
            fname=etFName.getText().toString();

            String destPath = getApplicationContext().getExternalFilesDir(null).getAbsolutePath();
            // File FileDir = new File(destPath, "A");  // getDir();
            String path =destPath              + "/secure_data/temp.png";
            Log.d("path",path);

        });

        bCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Document document = new Document();
                    String destPath = getApplicationContext().getExternalFilesDir(null).getAbsolutePath();

                    //String directoryPath = android.os.Environment.getExternalStorageDirectory().toString();

                    PdfWriter.getInstance(document, new FileOutputStream(destPath +"/secure_data/"+fname+".pdf")); //  Change pdf's name.


                    document.open();
                    for(int i=0;i<al.size();i++) {
                        fpath = destPath + "/secure_data/" + al.get(i).toString();
                        Log.d("path", fpath);
                        Image image = Image.getInstance(fpath);  // Change image's name and extension.

                        float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                                - document.rightMargin() - 0) / image.getWidth()) * 100; // 0 means you have no indentation. If you have any, change it.
                        image.scalePercent(scaler);
                        image.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);

                        document.add(image);

                    }
                    document.close();
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(),"PDF Creation Successful",Toast.LENGTH_LONG).show();
            }
        });

        btnUpload.setOnClickListener((View v) -> {
            mAppExcutor.diskIO().execute(() -> {


            });


            //fname=etFName.getText().toString();

            String destPath = getApplicationContext().getExternalFilesDir(null).getAbsolutePath();
            // File FileDir = new File(destPath, "A");  // getDir();

           // for(int i=0;i<al.size();i++) {
                //fpath = destPath + "/secure_data/"+al.get(i).toString();
               // Log.d("path", fpath);
            fpath = destPath + "/secure_data/"+fname+".pdf";
            Log.d("fpath",fpath);
            uploadFile(fpath);
            btnUpload.setEnabled(false);
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    //creating new thread to handle Http Operations
//                    //path+imageFileName);
//                }
//            }).start();
//
        });
    }
    private void WriteFileString(String filename, Bitmap image) {
        try {

            String destPath = getApplicationContext().getExternalFilesDir(null).getAbsolutePath();
            // File FileDir = new File(destPath, "A");  // getDir();
            String path =destPath              + "/secure_data";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            path = path + "//" + filename;
            file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream stream = new FileOutputStream(path);
            OutputStreamWriter writer = new OutputStreamWriter(stream);
            image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            //writer.write(data);
            writer.flush();
            writer.close();
            stream.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public static int dpToPx(int dp, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        scaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // Called when you request permission to read and write to external storage
        switch (requestCode) {
            case REQUEST_STORAGE_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // If you get permission, launch the camera
                    launchCamera();
                } else {
                    // If you do not get permission, show a Toast
                    Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If the image capture activity was called and was successful
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Process the image and set it to the TextView
            processAndSetImage();
        } else {

            // Otherwise, delete the temporary image file
            BitmapUtils.deleteImageFile(this, mTempPhotoPath);
        }
    }

    /**
     * Creates a temporary image file and captures a picture to store in it.
     */
    private void launchCamera() {

        // Create the capture image intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the temporary File where the photo should go
            File photoFile = null;
            try {
                photoFile = BitmapUtils.createTempImageFile(this);
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                // Get the path of the temporary file
                mTempPhotoPath = photoFile.getAbsolutePath();

                // Get the content URI for the image file
                Uri photoURI = FileProvider.getUriForFile(this,
                        FILE_PROVIDER_AUTHORITY,
                        photoFile);

                // Add the URI so the camera can store the image
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                // Launch the camera activity
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
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
    public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            iv.setScaleX(mScaleFactor);
            iv.setScaleY(mScaleFactor);
            return true;
        }
    }
    /**
     * Method for processing the captured image and setting it to the TextView.
     */
    private void processAndSetImage() {



        // Resample the saved image to fit the ImageView
        mResultsBitmap = BitmapUtils.resamplePic(this, mTempPhotoPath);


        // Set the new bitmap to the ImageView
        iv.setImageBitmap(mResultsBitmap);
    }
    public void uploadFile(String sourceFileUri) {
        String serverResponseMessage="";

//        location = GPSTracker.locate;
//        Log.d("Location : ",location);


        //String upLoadServerUri = Global.url+"UploadData1?location=" + lat;
        String upLoadServerUri = Global.url+"upload?user=" + user_home.user+"&fname="+fname+".pdf";
        Log.d("upLoadServerUri",upLoadServerUri);
        String fileName = sourceFileUri;
        Log.d("path pdf",fileName);
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 10 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);
        if (!sourceFile.isFile()) {
            Log.e("uploadFile", "Source File Does not exist");
            // return 0;
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            URL url = new URL(upLoadServerUri);
            conn = (HttpURLConnection) url.openConnection(); // Open a HTTP  connection to  the URL
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("uploaded_file", fileName);

            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            //dos.writeUTF("Content-Disposition: form-data; name=\"lat\";lat=\""+ lat + "\"" + lineEnd);
            //  dos.writeUTF(lon);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""+ fileName + "\"" + lineEnd);

            dos.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available(); // create a buffer of  maximum size

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            serverResponseCode = conn.getResponseCode();
            serverResponseMessage = conn.getResponseMessage();



            //close the streams //
            fileInputStream.close();
            dos.flush();
            dos.close();

        } catch (MalformedURLException ex) {

            ex.printStackTrace();
            Toast.makeText(getApplicationContext(), "MalformedURLException", Toast.LENGTH_SHORT).show();
            Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
        } catch (Exception e) {

            e.printStackTrace();
            Log.d("Exception1: ",e.getMessage().toString());
            //Toast.makeText(getApplicationContext(), "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();

        }





        Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
        if(serverResponseCode == 200){
            Toast.makeText(getApplicationContext(), "File Upload Complete.", Toast.LENGTH_SHORT).show();
            Intent bk=new Intent(this,user_home.class);
            startActivity(bk);

        }


        //   return serverResponseCode;
    }
}