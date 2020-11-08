package com.example.morningstar7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectionActivity extends AppCompatActivity{

    // Buttons declaration
    Button btn_scan, btn_search, btn_manage, btn_sync;
    Object object;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selection_interface);
        getSupportActionBar().setTitle("Main Menu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Assigning buttons
        btn_scan = findViewById(R.id.btn_scan);
        btn_search = findViewById(R.id.btn_search);
        btn_manage = findViewById(R.id.btn_manage);
        btn_sync = findViewById(R.id.btn_sync);


        // Move to the ScanningActivity Interface
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(SelectionActivity.this, ScanningActivity.class));
            }
        });

        // Move to the SearchActivity Interface
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(SelectionActivity.this, SearchActivity.class));
            }
        });

        // Move to the ManageActivity Interface
        btn_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(SelectionActivity.this, ManageActivity.class));
            }
        });

        // Move to the SyncActivity Interface
        btn_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(SelectionActivity.this, SyncActivity.class));
            }
        });
    }
}
