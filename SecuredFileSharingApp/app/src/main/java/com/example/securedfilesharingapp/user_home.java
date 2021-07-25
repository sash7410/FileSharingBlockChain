package com.example.securedfilesharingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class user_home extends AppCompatActivity {
Button btnCreate,btnMyFile;
Intent i1;
public static String user="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        btnCreate=(Button)findViewById(R.id.btnCreate);
        btnMyFile=(Button)findViewById(R.id.btnMyFile);
        i1=new Intent(this,create_file.class);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_file.al=new ArrayList();
                startActivity(i1);

            }
        });
        btnMyFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new get_my_files_list().execute();


            }
        });
    }

    private class get_my_files_list extends AsyncTask<Void, String, String> {
        @Override
        public String doInBackground(Void... Void)
        {
            JSONObject jsn = new JSONObject();
            String response = "";



            try {
                URL url = new URL(Global.url +"get_my_files_list");
                jsn.put("user",user);

                response = HttpConnection.getResponse(url,jsn);
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }

            //  if (response.equals())
            return response;
        }
        @Override
        protected void onPostExecute(String s) {

            if(s.endsWith("null"))
            {

                s=s.substring(0,s.length()-4);
            }
            Log.d("lists",s);
            Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();
            String arr[]=s.split("#");
            view_files.fnames=arr;
            Intent intent = new Intent(user_home.this, view_files.class);
            startActivity(intent);

        }
    }

}