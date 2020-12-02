package com.example.morningstar7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class RegisterActivity extends AppCompatActivity {

    // References to buttons and other controls on the layout
    Button btn_register;
    EditText et_FirstName, et_LastName, et_Usernamesignup, et_Passwordsignup, et_EmailAddress;

    DataBaseHelper dataBaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_interface);

        btn_register = findViewById(R.id.btn_register);
        et_FirstName = findViewById(R.id.et_FirstName);
        et_LastName = findViewById(R.id.et_LastName);
        et_Usernamesignup = findViewById(R.id.et_Usernamesignup);
        et_Passwordsignup = findViewById(R.id.et_Passwordsignup);
        et_EmailAddress = findViewById(R.id.et_EmailAddress);

        dataBaseHelper = new DataBaseHelper(RegisterActivity.this);

        // Button Listeners for the add and view all buttons
        btn_register.setOnClickListener(v -> {
            final String firstname, lastname, username, password, email;
            firstname = String.valueOf(et_FirstName.getText());
            lastname = String.valueOf(et_LastName.getText());
            username = String.valueOf(et_Usernamesignup.getText());
            password = String.valueOf(et_Passwordsignup.getText());
            email = String.valueOf(et_EmailAddress.getText());

            if(!firstname.equals("") && !lastname.equals("") && !username.equals("") && !password.equals("") && !email.equals("")) {
                Handler handler = new Handler(Looper.getMainLooper());
                //End Write and Read data with URL
                handler.post(() -> {
                    //Starting Write and Read data with URL
                    //Creating array for parameters
                    String[] field = new String[5];
                    field[0] = "c_firstname";
                    field[1] = "c_lastname";
                    field[2] = "c_username";
                    field[3] = "c_password";
                    field[4] = "c_email";
                    //Creating array for data
                    String[] data = new String[5];
                    data[0] = firstname;
                    data[1] = lastname;
                    data[2] = username;
                    data[3] = password;
                    data[4] = email;
                    try {
                        PutData putData = new PutData("http://10.0.0.195/morningstar/signup.php", "POST", field, data);
                        if (putData.startPut()) {
                            if (putData.onComplete()) {
                                String result = putData.getResult();
                                if (result.equals("Registration Successful")) {
                                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                    finish();
                                } else {
                                    UserRegistrationModel userRegistrationModel;

                                    userRegistrationModel = new UserRegistrationModel(et_FirstName.getText().toString(), et_LastName.getText().toString(), et_Usernamesignup.getText().toString(), et_Passwordsignup.getText().toString(), et_EmailAddress.getText().toString());
                                    boolean success = dataBaseHelper.addOneToRegTable(userRegistrationModel);

                                    if (success) {
                                        //Toast.makeText(RegisterActivity.this, userRegistrationModel.toString(), Toast.LENGTH_SHORT).show();
                                        Toast.makeText(RegisterActivity.this, "Success = " + success, Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterActivity.this, SelectionActivity.class));
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Username Already Taken.", Toast.LENGTH_SHORT).show();
                                        et_FirstName.setText("");
                                        et_LastName.setText("");
                                        et_Usernamesignup.setText("");
                                        et_Passwordsignup.setText("");
                                        et_EmailAddress.setText("");
                                    }
                                }
                                //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (Exception e) {
                        //ERROR Code 2002: WAIT_FAILURE_SERVER
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            }
        });
    }
}