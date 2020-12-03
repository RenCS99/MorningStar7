package com.example.morningstar7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;


//new  merger from different branch

// scanning class
// button for submit
// texts for barcode, row, and cols
public class ScanningActivity extends AppCompatActivity{
    private CodeScanner mCodeScanner;
    RelativeLayout relativeLayoutSubmitForm;
    FrameLayout framelayout;
    EditText editText_barcode;
    EditText editText_row;
    EditText editText_column;

    Button btn_submit;

    DataBaseHelper dataBaseHelper;

    ArrayAdapter userArrayAdapter;

    //override function

    //get scan ability of enabled
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanning_interface);
        getSupportActionBar().setTitle("Scan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dataBaseHelper = new DataBaseHelper(ScanningActivity.this);

        relativeLayoutSubmitForm = findViewById(R.id.relativeLayoutSubmitForm);
        framelayout = findViewById(R.id.framelayout);
        editText_barcode = findViewById(R.id.editText_barcode);
        editText_row = findViewById(R.id.editText_row);
        editText_column = findViewById(R.id.editText_column);

        btn_submit = findViewById(R.id.btn_submit);

        Button btn_scan = findViewById(R.id.btn_scan);