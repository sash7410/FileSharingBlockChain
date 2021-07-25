package com.example.securedfilesharingapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

//import android.support.v7.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    EditText etfname;
    EditText etlname;
    EditText etemail;
    EditText etpass;
    EditText etcpass;
    EditText etphone;
    EditText etadd;
    String userId ="";
    public  static String utype="";
    Button reg;
    RadioButton r;
    RadioGroup g;
    String fname,lname, email, pass;
    String cpass, phone, address, gender;
    DatabaseReference rootRef, demoRef;
    private DatabaseReference mDatabase;
    DatabaseReference mRef;
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    public static final String LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String LOCATION_PREF = "locationPref";
    Context context;
    Activity activity;
    TextView tv;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        g = findViewById(R.id.rgrp);
        etfname = findViewById(R.id.etFName);
        etlname=findViewById(R.id.etLName);
        etemail = findViewById(R.id.etEmail);
        etpass = findViewById(R.id.etPass);
        etcpass = findViewById(R.id.etcpass);
        etphone = findViewById(R.id.etMob);
        etadd = findViewById(R.id.etAddress);
        tv=(TextView)findViewById(R.id.textView);


        reg = findViewById(R.id.button);
        int radiobuttid = g.getCheckedRadioButtonId();
        r = findViewById(radiobuttid);
        context = RegisterActivity.this;
        activity = RegisterActivity.this;


        // Database reference pointing to root of database
        rootRef = FirebaseDatabase.getInstance().getReference();
        Log.d("rootRef",rootRef.toString());

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Database reference pointing to demo node

            demoRef = rootRef.child("user_details");

            Log.d("demoRef",demoRef.toString());



        etpass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                if(etpass.getText().length()<6)
                {
                    etpass.setError("Password must be minimum 6 char");
                }
            }
        });
        etphone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                if(etphone.getText().length()>10)
                {
                    etphone.setError("Invalid Number");
                }
            }
        });


        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fname = etfname.getText().toString().trim();
                lname=etlname.getText().toString().trim();
                email = etemail.getText().toString().trim();
                pass = etpass.getText().toString().trim();
                cpass = etcpass.getText().toString().trim();
                phone = etphone.getText().toString().trim();
                address = etadd.getText().toString().trim();
                gender = r.getText().toString().trim();
                userId = demoRef.push().getKey();
                Log.d("userId",userId);
                user_details pow= new user_details(fname,lname,email,pass,phone,address,gender);
                if(fname.isEmpty()||lname.isEmpty()||email.isEmpty()||pass.isEmpty()||cpass.isEmpty()
                        ||phone.isEmpty()||address.isEmpty())
                {
                    Toast.makeText(RegisterActivity.this,"Fill in all the fields", Toast.LENGTH_SHORT).show();
                }
                else {
                     if (!pass.equals(cpass)) {
                            etpass.setError("Password Mismatch");
                            etcpass.setError("Password Mismatch");
                        } else {
                            try {
                                demoRef.child(userId).setValue(pow);

                                Toast.makeText(RegisterActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                                reg.setVisibility(View.GONE);
                                 send_data();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

            }
        });







    }

    public void send_data() throws IOException, JSONException {
        URL url = new URL(Global.url + "Register");
        JSONObject jsn = new JSONObject();

        jsn.put("email", email);

        jsn.put("mobile", phone);

        jsn.put("id", userId);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);
        String response = null;
        response = HttpConnection.getResponse(url, jsn);
        if(response.equalsIgnoreCase("ok"))
        {
            Intent intennt = new Intent(RegisterActivity.this,LoginMainActivity.class);
            startActivity(intennt);
            Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
        }
        else if(response.equalsIgnoreCase("eok"))
        {
            Toast.makeText(this, "DB Error Contact Admin", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void rbclick(View v) {
        int radiobuttid = g.getCheckedRadioButtonId();
        r = findViewById(radiobuttid);
        //Toast.makeText(this, "OOOOOOOOOOOOOOOOOOOOOOOOOOO--" + r.getText(), Toast.LENGTH_SHORT).show();
    }



}

