package com.example.securedfilesharingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class view_files extends AppCompatActivity {
    public static ArrayList<ItemData> arrayList = new ArrayList<ItemData>();
    public static String fnames[]=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_files);
        final ListView list = findViewById(R.id.list);
        for(int i=0;i<fnames.length;i++)
        {
            //total[i]
            arrayList.add(new ItemData(fnames[i]));

        }
//
//
        CustomAdapter1 customAdapter = new CustomAdapter1(this, arrayList);
        list.setAdapter(customAdapter);

    }
}