package com.example.morningstar7;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.sql.Connection;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {

    Button btn_login, btn_register;
    EditText et_usernamelogin, et_passwordlogin;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_interface);
        getSupportActionBar().hide();

        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        et_usernamelogin = findViewById(R.id.et_usernamelogin);
        et_passwordlogin = findViewById(R.id.et_passwordlogin);
        dataBaseHelper = new DataBaseHelper(LoginActivity.this);

        // Button Listeners for the login and or direct to register interface
        btn_login.setOnClickListener(v -> {
            UserRegistrationModel userRegistrationModel;
            userRegistrationModel = new UserRegistrationModel(null, null, et_usernamelogin.getText().toString(), et_passwordlogin.getText().toString(), null);

            final String un, pass;
            un = String.valueOf(et_usernamelogin.getText());
            pass = String.valueOf(et_passwordlogin.getText());
            if(!un.equals("") && !pass.equals("")) {
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
                    PutData putData = new PutData("http://10.0.0.195/morningstar/login.php", "POST", field, data);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String result = putData.getResult();
                            if(result.equals("Login Success")) {
                                dataBaseHelper.checkIfEntryInDb(userRegistrationModel);
                                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(), SelectionActivity.class));
                                finish();
                            }
                            else {

                                boolean b = dataBaseHelper.checkIfEntryInDb(userRegistrationModel);

                                if(b){
                                    Toast.makeText(LoginActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, SelectionActivity.class));
                                }
                                else {
                                    Toast.makeText(LoginActivity.this, "No Existing User.", Toast.LENGTH_SHORT).show();
                                    et_usernamelogin.setText("");
                                    et_passwordlogin.setText("");
                                }
                                //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    //End Write and Read data with URL
                });
            }
            else {
                Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_LONG).show();
            }





        });

        btn_register.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }
}