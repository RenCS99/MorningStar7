package com.example.morningstar7;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.EditText;
import android.widget.Toast;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.morningstar7.DataBaseHelper.readFromLocalDatabase;


public class SyncActivity extends AppCompatActivity {

    Button btn_syncNow;
    RecyclerView rv_syncList;
    DataBaseHelper dataBaseHelper;
    RecyclerView.LayoutManager layoutManager;
    RecyclerAdapter adapter;
    ArrayList<SyncEntry> arrayList = new ArrayList<>();
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sync_interface);
        getSupportActionBar().setTitle("Sync");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_syncNow = findViewById(R.id.btn_syncNow);
        rv_syncList = (RecyclerView) findViewById(R.id.rv_syncList);
        layoutManager = new LinearLayoutManager(this);
        rv_syncList.setLayoutManager(layoutManager);
        rv_syncList.setHasFixedSize(true);
        adapter = new RecyclerAdapter(arrayList);
        rv_syncList.setAdapter(adapter);
        readFromLocalStorage();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                readFromLocalStorage();
            }
        };

        dataBaseHelper = new DataBaseHelper(SyncActivity.this);
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();

        // Show entries to sync
        ShowAllEntriesToSync(dataBaseHelper);

        Cursor cur = readFromLocalDatabase(db);

        btn_syncNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //saveToOnlineStorage(Contact.getScan_Id());
                //Toast.makeText(SyncActivity.this, everyone.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void readFromLocalStorage(){
        arrayList.clear();
        DataBaseHelper dataBaseHelper1 = new DataBaseHelper(this);
        SQLiteDatabase database = dataBaseHelper1.getReadableDatabase();

        Cursor cursor = readFromLocalDatabase(database);

        while(cursor.moveToNext()){
            int scan_id = cursor.getInt(cursor.getColumnIndex(dataBaseHelper1.COLUMN_S_SCAN_ID));
            int sync_status = cursor.getInt(cursor.getColumnIndex(dataBaseHelper1.COLUMN_S_SYNC_STATUS));
            arrayList.add(new SyncEntry(scan_id, sync_status));
        }

        adapter.notifyDataSetChanged();
        cursor.close();
        dataBaseHelper1.close();
    }

    private void saveToOnlineStorage(final int scan_id){
        if(checkNetworkConnection()){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DataBaseHelper.SERVER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String Response = jsonObject.getString("response");
                                if(Response.equals("OK")) {
                                    saveToLocalStorage(scan_id, DataBaseHelper.SYNC_STATUS_OK);
                                }
                                else {
                                    saveToLocalStorage(scan_id, DataBaseHelper.SYNC_STATUS_FAILED);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                           @Override
                           public void onErrorResponse(VolleyError error) {
                                saveToLocalStorage(scan_id, DataBaseHelper.SYNC_STATUS_FAILED);
                           }
                       })
                    {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("scan_id", String.valueOf(scan_id));
                            return params;
                        }
                    };
            MySingleton.getInstance(SyncActivity.this).addToRequestQue(stringRequest);
        }
        else {
            saveToLocalStorage(scan_id, DataBaseHelper.SYNC_STATUS_FAILED);
        }
    }

    public boolean checkNetworkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void ShowAllEntriesToSync(DataBaseHelper dataBaseHelper1){
        adapter = new RecyclerAdapter(arrayList);
        rv_syncList.setAdapter(adapter);
    }

    private void saveToLocalStorage(int scan_id, int sync_status){

        DataBaseHelper dataBaseHelper1 = new DataBaseHelper(this);
        SQLiteDatabase database = dataBaseHelper1.getWritableDatabase();
        dataBaseHelper1.saveToLocalDatabase(scan_id, sync_status, database);
        readFromLocalStorage();
        dataBaseHelper1.close();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(broadcastReceiver, new IntentFilter(dataBaseHelper.UI_UPDATE_BROADCAST));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
}
