package com.example.morningstar7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    // References to buttons and other controls on the layout
    Button btn_register, btn_showAll;
    EditText et_FirstName, et_LastName, et_Username, et_Password, et_EmailAddress;
    ListView lv_registeredUsers;
    ArrayAdapter userArrayAdapter;
    DataBaseHelper dataBaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_interface);

        btn_register = findViewById(R.id.btn_register);
        btn_showAll = findViewById(R.id.btn_showAll);
        et_FirstName = findViewById(R.id.et_FirstName);
        et_LastName = findViewById(R.id.et_LastName);
        et_Username = findViewById(R.id.et_Username);
        et_Password = findViewById(R.id.et_Password);
        et_EmailAddress = findViewById(R.id.et_EmailAddress);
        lv_registeredUsers = findViewById(R.id.lv_registeredUsers);

        dataBaseHelper = new DataBaseHelper(RegisterActivity.this);

        //Shows Registered Users
        //ShowRegisteredUserOnListView(dataBaseHelper);

        // Button Listeners for the add and view all buttons
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                UserRegistrationModel userRegistrationModel;

                if(TextUtils.isEmpty(et_FirstName.getText()) || TextUtils.isEmpty(et_LastName.getText()) || TextUtils.isEmpty(et_Username.getText()) || TextUtils.isEmpty(et_Password.getText()) || TextUtils.isEmpty(et_EmailAddress.getText())){
                    Toast.makeText(RegisterActivity.this, "All Fields Required.", Toast.LENGTH_SHORT).show();
                    et_FirstName.setText("");
                    et_LastName.setText("");
                    et_Username.setText("");
                    et_Password.setText("");
                    et_EmailAddress.setText("");
                } else{
                    userRegistrationModel = new UserRegistrationModel(et_FirstName.getText().toString(), et_LastName.getText().toString(), et_Username.getText().toString(), et_Password.getText().toString(), et_EmailAddress.getText().toString());
                    boolean success = dataBaseHelper.addOneToRegTable(userRegistrationModel);

                    if (success) {
                        Toast.makeText(RegisterActivity.this, userRegistrationModel.toString(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(RegisterActivity.this, "Success = " + success, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, SelectionActivity.class));
                    }
                    else {
                        Toast.makeText(RegisterActivity.this, "Username Already Taken.", Toast.LENGTH_SHORT).show();
                        et_FirstName.setText("");
                        et_LastName.setText("");
                        et_Username.setText("");
                        et_Password.setText("");
                        et_EmailAddress.setText("");
                    }
                }
            }
        });

        btn_showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataBaseHelper dataBaseHelper = new DataBaseHelper(RegisterActivity.this);

                ShowRegisteredUserOnListView(dataBaseHelper);

                //Toast.makeText(MainActivity.this, everyone.toString(), Toast.LENGTH_SHORT).show();
            }
        });

//        lv_registeredUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
//                UserRegistrationModel clickedUser = (UserRegistrationModel) parent.getItemAtPosition(position);
//                dataBaseHelper.deleteOneFromRegTable(clickedUser);
//                ShowRegisteredUserOnListView(dataBaseHelper);
//                Toast.makeText(MainActivity.this, "Deleted " + clickedUser, Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    private void ShowRegisteredUserOnListView(DataBaseHelper dataBaseHelper2) {
        userArrayAdapter = new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_list_item_1, dataBaseHelper2.getEveryoneRegistered());
        lv_registeredUsers.setAdapter(userArrayAdapter);
    }
}