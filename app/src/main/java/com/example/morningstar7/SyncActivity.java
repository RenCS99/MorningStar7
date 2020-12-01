package com.example.morningstar7;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SyncActivity extends AppCompatActivity {

    Button btn_syncNow;
    RecyclerView rv_syncList;
    RecyclerAdapter adapter;
    ArrayList<SyncEntry> arrayList;

    //BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sync_interface);
        getSupportActionBar().setTitle("Sync");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_syncNow = findViewById(R.id.btn_syncNow);
        rv_syncList = findViewById(R.id.rv_syncList);
//        broadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                readFromLocalStorage();
//            }
//        };

        arrayList = new ArrayList<>();
        adapter = new RecyclerAdapter(arrayList);
        rv_syncList.setLayoutManager(new LinearLayoutManager(SyncActivity.this));
        rv_syncList.setAdapter(adapter);

        readFromLocalStorage();


        btn_syncNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < arrayList.size(); i++) {
                    saveToAppServer(arrayList.get(i).getBarcode_id(), arrayList.get(i).getContainer_name(), arrayList.get(i).getLatitude(), arrayList.get(i).getLongitude(), arrayList.get(i).getBarcode_row(), arrayList.get(i).getBarcode_sec(), arrayList.get(i).getLastUpdated(), arrayList.get(i).getSync_status());
                }
            }
        });

    }

    public void readFromLocalStorage(){
        arrayList.clear();
        DataBaseHelper dataBaseHelper1 = new DataBaseHelper(this);

        Cursor cur = dataBaseHelper1.readFromLocalDatabase();
        if(cur.getCount() == 0) {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
        else {
            while (cur.moveToNext()) {
                String barcode_id = cur.getString(cur.getColumnIndex(DataBaseHelper.COLUMN_B_BARCODEID));
                String container_name = cur.getString(cur.getColumnIndex(DataBaseHelper.COLUMN_B_CONTAINERNAME));
                double latitude = cur.getDouble(cur.getColumnIndex(DataBaseHelper.COLUMN_B_LATITUDE));
                double longitude = cur.getDouble(cur.getColumnIndex(DataBaseHelper.COLUMN_B_LONGITUDE));
                int row = cur.getInt(cur.getColumnIndex(DataBaseHelper.COLUMN_B_ROW));
                int section = cur.getInt(cur.getColumnIndex(DataBaseHelper.COLUMN_B_SECTION));
                String lastUpdated = cur.getString(cur.getColumnIndex(DataBaseHelper.COLUMN_B_LASTUPDATED));
                int sync_status = cur.getInt(cur.getColumnIndex(DataBaseHelper.COLUMN_B_SYNCSTATUS));
                arrayList.add(new SyncEntry(barcode_id, container_name, latitude, longitude, row, section, lastUpdated, sync_status));
            }
        }
        adapter.notifyDataSetChanged();
        cur.close();
        dataBaseHelper1.close();
    }

    private void saveToAppServer(String barcode_id, String container_name, double latitude, double longitude, int row, int section, String lastUpdated, int sync_status){

        DataBaseHelper dataBaseHelper1 = new DataBaseHelper(this);
        SQLiteDatabase db = dataBaseHelper1.getWritableDatabase();

        if(checkNetworkConnection()) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, dataBaseHelper1.SERVER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String Response = jsonObject.getString("response");
                                if(Response.equals("OK")) {
                                    saveToLocalStorage(barcode_id, container_name, latitude, longitude, row, section, lastUpdated, DataBaseHelper.SYNC_STATUS_OK);
                                }
                                else {
                                    saveToLocalStorage(barcode_id, container_name, latitude, longitude, row, section, lastUpdated, DataBaseHelper.SYNC_STATUS_FAILED);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            saveToLocalStorage(barcode_id, container_name, latitude, longitude, row, section, lastUpdated, DataBaseHelper.SYNC_STATUS_FAILED);
                        }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("b_barcodeId", barcode_id);
                    params.put("b_containerName", container_name);
                    params.put("b_latitude", String.valueOf(latitude));
                    params.put("b_longitude", String.valueOf(longitude));
                    params.put("b_row", String.valueOf(row));
                    params.put("b_section", String.valueOf(section));
                    params.put("b_lastUpdated", lastUpdated);
                    params.put("b_syncStatus", String.valueOf(sync_status));
                    return params;
                }
            };
            MySingleton.getInstance(SyncActivity.this).addToRequestQue(stringRequest);
        }
        else {
            saveToLocalStorage(barcode_id, container_name, latitude, longitude, row, section, lastUpdated, DataBaseHelper.SYNC_STATUS_FAILED);
        }

    }


    public boolean checkNetworkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void saveToLocalStorage(String barcode_id, String container_name, double latitude, double longitude, int row, int section, String lastUpdated, int sync_status) {
        DataBaseHelper dataBaseHelper1 = new DataBaseHelper(this);
        SQLiteDatabase db = dataBaseHelper1.getWritableDatabase();

        dataBaseHelper1.saveToLocalDatabase(barcode_id, container_name, latitude, longitude, row, section, lastUpdated, sync_status, db);

        readFromLocalStorage();
        dataBaseHelper1.close();
    }


//    @Override
//    protected void onStart() {
//        super.onStart();
//        registerReceiver(broadcastReceiver, new IntentFilter(dataBaseHelper.UI_UPDATE_BROADCAST));
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        unregisterReceiver(broadcastReceiver);
//    }
}
