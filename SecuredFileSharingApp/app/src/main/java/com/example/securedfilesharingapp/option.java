package com.example.securedfilesharingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class option extends AppCompatActivity {

    Button btnlogin;
    Button btnreg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        btnlogin = findViewById(R.id.loginbtn);
        btnreg = findViewById(R.id.regbtn);


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(option.this, LoginMainActivity.class);
                startActivity(i);
            }
        });
        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(option.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }
}