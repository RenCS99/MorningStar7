package com.example.morningstar7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class SelectionActivity extends AppCompatActivity{

    DataBaseHelper dataBaseHelper;

    ArrayAdapter userArrayAdapter;

    ListView lv_barcodeRecord;

    private static final int CAMERA_PERMISSION_CODE = 100;

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


        lv_barcodeRecord = findViewById(R.id.lv_barcodeRecord);

        dataBaseHelper = new DataBaseHelper(this);

        ShowAllRecords(dataBaseHelper);

        // Move to the ScanningActivity Interface
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                checkPermission(Manifest.permission.CAMERA,
                        CAMERA_PERMISSION_CODE);
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


    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(this,
                    new String[] { permission },
                    requestCode);
        } else {
            Toast.makeText(this,
                    "Permission already granted",
                    Toast.LENGTH_SHORT)
                    .show();
            startActivity(new Intent(SelectionActivity.this, ScanningActivity.class));
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,
                        "Camera Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();

                startActivity(new Intent(SelectionActivity.this, ScanningActivity.class));
            } else {
                Toast.makeText(this,
                        "Camera Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();

            }
        }
    }

    private void ShowAllRecords(DataBaseHelper dataBaseHelper2) {
        //userArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataBaseHelper2.getBarcodeRecords());
        //lv_barcodeRecord.setAdapter(userArrayAdapter);
    }

}

