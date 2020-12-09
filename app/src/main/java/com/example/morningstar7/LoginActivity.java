package com.example.morningstar7;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telecom.DisconnectCause;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;


public class LoginActivity extends AppCompatActivity {

    Button btn_login, btn_register;
    EditText et_usernamelogin, et_passwordlogin;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_interface);
        getSupportActionBar().hide();
        //String url = "http://127.0.0.1/morningstar/DbConnect.php";
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        et_usernamelogin = findViewById(R.id.et_usernamelogin);
        et_passwordlogin = findViewById(R.id.et_passwordlogin);
        dataBaseHelper = new DataBaseHelper(LoginActivity.this);

        // Button Listeners for the login and or direct to register interface
        btn_login.setOnClickListener(v -> {
            UserRegistrationModel userRegistrationModel;
            userRegistrationModel = new UserRegistrationModel(null, null, et_usernamelogin.getText().toString(), et_passwordlogin.getText().toString(), null);
            boolean b = dataBaseHelper.checkIfEntryInDb(userRegistrationModel);
            //boolean t = isConnectedToServer(url,120);
            //Toast.makeText(getApplicationContext(), " " + t, Toast.LENGTH_SHORT).show();
            //if(!t) {

                final String un, pass;
                un = String.valueOf(et_usernamelogin.getText());
                pass = String.valueOf(et_passwordlogin.getText());
                if (!un.equals("") && !pass.equals("")) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(() -> {
                        //Starting Write and Read data with URL
                        //Creating array for parameters
                        String[] field = new String[2];
                        field[0] = "c_username";
                        field[1] = "c_password";
                        //Creating array for data
                        String[] data = new String[2];
                        data[0] = un;
                        data[1] = pass;
                        try {
                            PutData putData = new PutData("http://10.0.0.195/morningstar/login.php", "POST", field, data);

                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if (result.equals("Login Success")) {
                                        if (b) {
                                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(getApplicationContext(), SelectionActivity.class));
                                        } else {
                                            new Connection().execute();
                                            startActivity(new Intent(getApplicationContext(), SelectionActivity.class));
                                        }
                                    } else {
                                        if (b) {
                                            Toast.makeText(LoginActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(LoginActivity.this, SelectionActivity.class));
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                                            et_usernamelogin.setText("");
                                            et_passwordlogin.setText("");
                                        }
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }
                        catch (Exception e){
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        //End Write and Read data with URL
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_LONG).show();
                }

//            }
//            else {
//                boolean b = dataBaseHelper.checkIfEntryInDb(userRegistrationModel);
//
//                if (b) {
//                    Toast.makeText(LoginActivity.this, "Log", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(LoginActivity.this, SelectionActivity.class));
//                } else {
//                    Toast.makeText(LoginActivity.this, "No Existing User.", Toast.LENGTH_SHORT).show();
//                    et_usernamelogin.setText("");
//                    et_passwordlogin.setText("");
//                }
//            }
        });

        btn_register.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    public class Connection extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result;
            String host = "http://10.0.0.195/morningstar/getCreds.php";
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(host));
                HttpResponse response = client.execute(request);
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                StringBuilder stringBuffer = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null){
                    stringBuffer.append(line);
                    break;
                }
                reader.close();
                result = stringBuffer.toString();

            }
            catch (Exception e) {
                return "Exception: " + e.getMessage();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                int success = jsonObject.getInt("success");
                if(success == 1){
                    JSONArray users = jsonObject.getJSONArray("users");
                    for(int i = 0; i < users.length(); i++) {
                        JSONObject user = users.getJSONObject(i);
                        UserRegistrationModel userRegistrationModel2 = new UserRegistrationModel(user.getString("c_firstname"), user.getString("c_lastname"), user.getString("c_username"), user.getString("c_password"), user.getString("c_email"));
                        boolean b = dataBaseHelper.addOneToRegTable(userRegistrationModel2);
                        if (b) {
                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                        }

                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "There is no users yet", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(getApplicationContext(), "Not passed", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isConnectedToServer(String url, int timeout) {
        try{
            URL myUrl = new URL(url);
            URLConnection connection = myUrl.openConnection();
            connection.setConnectTimeout(timeout);
            connection.connect();
            return true;
        } catch (Exception e){
            return false;
        }
    }
}