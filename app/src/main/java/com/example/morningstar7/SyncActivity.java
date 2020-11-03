package com.example.morningstar7;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


public class SyncActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sync_interface);
        getSupportActionBar().setTitle("Sync");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
}
