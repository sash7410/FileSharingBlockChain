package com.example.securedfilesharingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

//import android.support.v7.app.AppCompatActivity;

public class LoginMainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String PREF_USERNAME = "username";
    private static final String PREF_PASSWORD = "password";
    EditText usr;
    EditText pwd;
    TextView studname, studemail;
    Button lbtn;
    Intent intent = null;
    String email, pass;
    private DatabaseReference mDatabase;
    DatabaseReference rootRef, demoRef;
    Intent intent_user_home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        usr = findViewById(R.id.usn);
        pwd = findViewById(R.id.pw);
        lbtn = findViewById(R.id.loginbtn);
        intent_user_home=new Intent(this,user_home.class);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Database reference pointing to root of database
        rootRef = FirebaseDatabase.getInstance().getReference();
        Log.d("rootRef",rootRef.toString());

            demoRef = rootRef.child("user_details");

            Log.d("demoRef",demoRef.toString());


        /*to store username and password*/
        SharedPreferences pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String username = pref.getString(PREF_USERNAME, null);
        String password = pref.getString(PREF_PASSWORD, null);
        //Toast.makeText(this, username+password, Toast.LENGTH_SHORT).show();
        if (username == null || password == null) {
            getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                    .edit()
                    .putString(PREF_USERNAME, usr.getText().toString())
                    .putString(PREF_PASSWORD, pwd.getText().toString())
                    .apply();
        } else {
            usr.setText(username);
            pwd.setText(password);
        }

        //add the function to connect to database
        lbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                        .edit()
                        .putString(PREF_USERNAME, usr.getText().toString())
                        .putString(PREF_PASSWORD, pwd.getText().toString())
                        .apply();
                email = usr.getText().toString().trim();
                pass = pwd.getText().toString().trim();
                login_check();

            }
        });


    }


    public void login_check()
    {
        try
        {
            URL url = new URL(Global.url + "Login");
            JSONObject jsn = new JSONObject();
            jsn.put("email",email);
           // jsn.put("pass",pass);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
            StrictMode.setThreadPolicy(policy);
            final String response = HttpConnection.getResponse(url,jsn);

            //Toast.makeText(LoginMainActivity.this,response,Toast.LENGTH_LONG).show();
            if(response.equalsIgnoreCase(""))
            {
                Toast.makeText(LoginMainActivity.this,"Not Matching", Toast.LENGTH_SHORT).show();

            }
            else
            {

                demoRef.child(response).addValueEventListener(new ValueEventListener() {



                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        user_details pod = dataSnapshot.getValue(user_details.class);

                        Log.d("response", "Email: " + pod.email + ", pass " + pod.pass);

                        if(email.equals(pod.email)&& pass.equals(pod.pass))
                        {
                            Log.d("result","Matching");
                            user_home.user=pod.email;

                                startActivity(intent_user_home);






                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Email and Password are not matching",Toast.LENGTH_LONG).show();
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("Error", "Failed to read value.", error.toException());
                    }
                });


            }
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
