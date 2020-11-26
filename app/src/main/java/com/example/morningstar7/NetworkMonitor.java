package com.example.morningstar7;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NetworkMonitor extends BroadcastReceiver{


    @Override
    public void onReceive(final Context context, Intent intent) {
        if(checkNetworkConnection(context)){
            final DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
            final SQLiteDatabase db = dataBaseHelper.getWritableDatabase();

            Cursor cur = dataBaseHelper.readFromLocalDatabase(db);

            while(cur.moveToNext()){
                int sync_status = cur.getInt(cur.getColumnIndex(dataBaseHelper.COLUMN_S_SYNC_STATUS));
                if(sync_status == dataBaseHelper.SYNC_STATUS_FAILED){
                    final int scan_id = cur.getInt(cur.getColumnIndex(dataBaseHelper.COLUMN_S_SCAN_ID));
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, dataBaseHelper.SERVER_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String Response = jsonObject.getString("response");
                                        if(Response.equals("OK")){
                                            dataBaseHelper.updateLocalDatabase(scan_id, dataBaseHelper.SYNC_STATUS_OK, db);
                                            context.sendBroadcast(new Intent(dataBaseHelper.UI_UPDATE_BROADCAST));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            })
                            {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError{
                                    Map<String, String> params = new HashMap<>();
                                    params.put("scan_id", String.valueOf(scan_id));
                                    return super.getParams();
                                }
                            };
                    MySingleton.getInstance(context).addToRequestQue(stringRequest);
                }
            }

            dataBaseHelper.close();
        }
    }

    public boolean checkNetworkConnection(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
