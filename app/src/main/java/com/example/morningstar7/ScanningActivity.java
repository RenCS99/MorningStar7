package com.example.morningstar7;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
//scanning

public class ScanningActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanning_interface);
        getSupportActionBar().setTitle("Scan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
}
