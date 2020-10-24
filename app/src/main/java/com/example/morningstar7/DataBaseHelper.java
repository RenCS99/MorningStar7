package com.example.morningstar7;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {


    public static final String CREDENTIALS_TABLE = "CREDENTIALS_TABLE";
    public static final String COLUMN_C_FIRSTNAME = "c_firstname";
    public static final String COLUMN_C_LASTNAME = "c_lastname";
    public static final String COLUMN_C_USERNAME = "c_username";
    public static final String COLUMN_C_PASSWORD = "c_password";
    public static final String COLUMN_C_EMAIL = "c_email";
    public static final String COLUMN_C_USER_ID = "c_userId";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "MorningStar7.db", null, 1);
    }

    // This is called the first time a database is accessed. There should be code in here to create a new database.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + CREDENTIALS_TABLE + " (" + COLUMN_C_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_C_FIRSTNAME + " TEXT NOT NULL, " + COLUMN_C_LASTNAME + " TEXT NOT NULL, " + COLUMN_C_USERNAME + " Text NOT NULL, " + COLUMN_C_PASSWORD + " TEXT NOT NULL, " + COLUMN_C_EMAIL + " TEXT NOT NULL)";

        db.execSQL(createTableStatement);
    }

    // This is called if the database version number changes. It prevents previous users apps from breaking when you change the database design.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addOne(UserRegistrationModel userRegistrationModel){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_C_FIRSTNAME, userRegistrationModel.getFirstName());
        cv.put(COLUMN_C_LASTNAME, userRegistrationModel.getLastName());
        cv.put(COLUMN_C_USERNAME, userRegistrationModel.getUsername());
        cv.put(COLUMN_C_PASSWORD, userRegistrationModel.getPassword());
        cv.put(COLUMN_C_EMAIL,userRegistrationModel.getUserEmail());

        long insert = db.insert(CREDENTIALS_TABLE, null, cv);

        return insert != -1;
    }

    public boolean deleteOne(UserRegistrationModel userRegistrationModel){
        // Find userRegistrationModel in the database. If it is found, delete it and return true.
        // If it is not found, return false

        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + CREDENTIALS_TABLE + " WHERE " + COLUMN_C_USER_ID + " = " + userRegistrationModel.getId();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        else {
            cursor.close();
            return false;
        }
    }

    public List<UserRegistrationModel> getEveryoneRegistered(){
        List<UserRegistrationModel> returnList = new ArrayList<>();

        // Get data from the database

        String queryString = "SELECT * FROM " + CREDENTIALS_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            // Loop through the cursor (result set) and create new user objects. Put them into the return list.

            do {
                int userId = cursor.getInt(0);
                String userFirstName = cursor.getString(1);
                String userLastName = cursor.getString(2);
                String userUsername = cursor.getString(3);
                String userPassword = cursor.getString(4);
                String userEmail = cursor.getString(5);

                UserRegistrationModel newUserRegistrationModel = new UserRegistrationModel(userId, userFirstName, userLastName, userUsername, userPassword, userEmail);
                returnList.add(newUserRegistrationModel);


            } while(cursor.moveToNext());

        }

        // Close both the cursor and the db when done.
        cursor.close();
        db.close();
        return returnList;
    }
}
