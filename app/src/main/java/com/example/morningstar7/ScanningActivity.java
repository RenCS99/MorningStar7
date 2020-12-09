package com.example.morningstar7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
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


        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                ScanningActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        createDialog(result.getText().toString());
                        //Toast.makeText(ScanningActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                framelayout.setVisibility(View.VISIBLE);
                relativeLayoutSubmitForm.setVisibility(View.GONE);
                mCodeScanner.startPreview();
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RowColumnBarcodeModel rowColumnBarcodeModel;
                if(TextUtils.isEmpty(editText_row.getText()) || TextUtils.isEmpty(editText_column.getText()) || TextUtils.isEmpty(editText_barcode.getText())){
                    Toast.makeText(ScanningActivity.this, "Error while", Toast.LENGTH_SHORT).show();
                    rowColumnBarcodeModel = new RowColumnBarcodeModel(-1, 0, 0, "error");
                } else{

                    rowColumnBarcodeModel = new RowColumnBarcodeModel(1, Integer.parseInt(editText_row.getText().toString()), Integer.parseInt(editText_column.getText().toString()), editText_barcode.getText().toString());
                }

                DataBaseHelper dataBaseHelper  = new DataBaseHelper(ScanningActivity.this);

                long success = dataBaseHelper.addRowColumnBarcode(rowColumnBarcodeModel);
                Toast.makeText(ScanningActivity.this, "Barcode with values has been written in database at index: " + success, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    private void createDialog(final String rawValue) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setNegativeButton("Scan Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mCodeScanner.startPreview();
            }
        }).setPositiveButton("Add data", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                framelayout.setVisibility(View.GONE);
                relativeLayoutSubmitForm.setVisibility(View.VISIBLE);
                editText_barcode.setText(rawValue);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setTitle("Do you want to add this value into database?");
        dialog.setMessage(rawValue);
        dialog.show();
    }



}