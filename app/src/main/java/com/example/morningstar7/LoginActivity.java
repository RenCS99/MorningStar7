package com.example.morningstar7;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    Button btn_login, btn_register;
    EditText et_username, et_password;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_interface);

        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        dataBaseHelper = new DataBaseHelper(LoginActivity.this);

        // Button Listeners for the login and or direct to register interface
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserRegistrationModel userRegistrationModel;

                if(TextUtils.isEmpty(et_username.getText()) || TextUtils.isEmpty(et_password.getText())){
                    Toast.makeText(LoginActivity.this, "All fields required.", Toast.LENGTH_SHORT).show();
                    et_username.setText("");
                    et_password.setText("");
                } else{
                    userRegistrationModel = new UserRegistrationModel(null, null, et_username.getText().toString(), et_password.getText().toString(), null);
                    boolean b = dataBaseHelper.checkIfEntryInDb(userRegistrationModel);

                    if(b){
                        Toast.makeText(LoginActivity.this, "Access Granted.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, SelectionActivity.class));
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "No Existing User.", Toast.LENGTH_SHORT).show();
                        et_username.setText("");
                        et_password.setText("");
                    }
                }
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
}