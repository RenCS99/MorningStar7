package com.example.morningstar7;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // References to buttons and other controls on the layout
    Button btn_register, btn_showAll;
    EditText et_FirstName, et_LastName, et_Username, et_Password, et_EmailAddress;
    ListView lv_registeredUsers;
    ArrayAdapter userArrayAdapter;
    DataBaseHelper dataBaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_register = findViewById(R.id.btn_register);
        btn_showAll = findViewById(R.id.btn_showAll);
        et_FirstName = findViewById(R.id.et_FirstName);
        et_LastName = findViewById(R.id.et_LastName);
        et_Username = findViewById(R.id.et_Username);
        et_Password = findViewById(R.id.et_Password);
        et_EmailAddress = findViewById(R.id.et_EmailAddress);
        lv_registeredUsers = findViewById(R.id.lv_registeredUsers);

        dataBaseHelper = new DataBaseHelper(MainActivity.this);

        ShowRegisteredUserOnListView(dataBaseHelper);

        // Button Listeners for the add and view all buttons
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                UserRegistrationModel userRegistrationModel;

                if(TextUtils.isEmpty(et_FirstName.getText()) || TextUtils.isEmpty(et_LastName.getText()) || TextUtils.isEmpty(et_Username.getText()) || TextUtils.isEmpty(et_Password.getText()) || TextUtils.isEmpty(et_EmailAddress.getText())){
                    Toast.makeText(MainActivity.this, "Error Creating User", Toast.LENGTH_SHORT).show();
                    userRegistrationModel = new UserRegistrationModel(-1, "error", "error", "error", "error", "error");
                } else{
                    userRegistrationModel = new UserRegistrationModel(-1, et_FirstName.getText().toString(), et_LastName.getText().toString(), et_Username.getText().toString(), et_Password.getText().toString(), et_EmailAddress.getText().toString());
                    Toast.makeText(MainActivity.this, userRegistrationModel.toString(), Toast.LENGTH_SHORT).show();
                }

                DataBaseHelper dataBaseHelper  = new DataBaseHelper(MainActivity.this);

                boolean success = dataBaseHelper.addOne(userRegistrationModel);

                Toast.makeText(MainActivity.this, "Success = " + success, Toast.LENGTH_SHORT).show();
                ShowRegisteredUserOnListView(dataBaseHelper);
            }
        });

        btn_showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);

                ShowRegisteredUserOnListView(dataBaseHelper);

                //Toast.makeText(MainActivity.this, everyone.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        lv_registeredUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                UserRegistrationModel clickedUser = (UserRegistrationModel) parent.getItemAtPosition(position);
                dataBaseHelper.deleteOne(clickedUser);
                ShowRegisteredUserOnListView(dataBaseHelper);
                Toast.makeText(MainActivity.this, "Deleted " + clickedUser, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void ShowRegisteredUserOnListView(DataBaseHelper dataBaseHelper2) {
        userArrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, dataBaseHelper2.getEveryoneRegistered());
        lv_registeredUsers.setAdapter(userArrayAdapter);
    }
}