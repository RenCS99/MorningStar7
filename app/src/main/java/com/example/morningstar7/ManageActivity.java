package com.example.morningstar7;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;



public class ManageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_interface);
        getSupportActionBar().setTitle("Manage");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

}
