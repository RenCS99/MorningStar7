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

    private static final String DB_NAME = "MorningStar7.db";
    private static final int DB_VERSION = 1;
    public static final int SYNC_STATUS_OK = 0;
    public static final int SYNC_STATUS_FAILED = 1;
    public static final String SERVER_URL = "http://10.0.2.2/syncdemo/syncinfo.php";
    public static final String UI_UPDATE_BROADCAST = "com.example.morningstar7.uiupdatebroadcast";

    // Database Tables
    public static final String CREDENTIALS_TABLE = "CREDENTIALS_TABLE";
    public static final String BARCODE_TABLE = "BARCODE_TABLE";
    public static final String LOCATIONDATA_TABLE = "LOCATIONDATA_TABLE";
    public static final String ROWSANDSECTIONS_TABLE = "ROWSANDSECTIONS_TABLE";
    public static final String SYNC_INFO_TABLE = "SYNC_INFO";

    // Sync's Columns
    public static final String COLUMN_S_SCAN_ID = "s_scanId";
    public static final String COLUMN_S_SYNC_STATUS = "s_syncStatus";

    // Credentials' Columns
    public static final String COLUMN_C_USER_ID = "c_userId";
    public static final String COLUMN_C_FIRSTNAME = "c_firstname";
    public static final String COLUMN_C_LASTNAME = "c_lastname";
    public static final String COLUMN_C_USERNAME = "c_username";
    public static final String COLUMN_C_PASSWORD = "c_password";
    public static final String COLUMN_C_EMAIL = "c_email";

    // Barcode's Columns
    public static final String COLUMN_B_CONTAINERID = "b_containerId";
    public static final String COLUMN_B_BARCODEID = "b_barcodeId";
    public static final String COLUMN_B_CONTAINERNAME = "b_containerName";
    public static final String COLUMN_B_DIRTYBIT = "b_dirtyBit";

    // LocationData's Columns
    public static final String COLUMN_LD_CONTAINERID = "ld_containerId";
    public static final String COLUMN_LD_LATITUDE = "ld_latitude";
    public static final String COLUMN_LD_LONGITUDE = "ld_longitude";

    // RowsAndSections' Table Columns
    public static final String COLUMN_RS_CONTAINERID = "rs_containerId";
    public static final String COLUMN_RS_ROW = "rs_row";
    public static final String COLUMN_RS_SECTION = "rs_section";

    // Creating Tables SQL
    String createTable_credentials = "CREATE TABLE IF NOT EXISTS " + CREDENTIALS_TABLE + " (" + COLUMN_C_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_C_FIRSTNAME + " TEXT NOT NULL, " + COLUMN_C_LASTNAME + " TEXT NOT NULL, " + COLUMN_C_USERNAME + " Text NOT NULL, " + COLUMN_C_PASSWORD + " TEXT NOT NULL, " + COLUMN_C_EMAIL + " TEXT NOT NULL)";
    String createTable_barcode = "CREATE TABLE IF NOT EXISTS " + BARCODE_TABLE + " (" + COLUMN_B_CONTAINERID + " INTEGER NOT NULL, " + COLUMN_B_BARCODEID + " INTEGER PRIMARY KEY, " + COLUMN_B_CONTAINERNAME + " TEXT NOT NULL, " + COLUMN_B_DIRTYBIT + " VARCHAR(1) NOT NULL)";
    String createTable_locationdata = "CREATE TABLE IF NOT EXISTS " + LOCATIONDATA_TABLE + " (" + COLUMN_LD_CONTAINERID + " INTEGER PRIMARY KEY, " + COLUMN_LD_LATITUDE + " REAL NOT NULL, " + COLUMN_LD_LONGITUDE + " REAL NOT NULL)";
    String createTable_rowsandsections = "CREATE TABLE IF NOT EXISTS " + ROWSANDSECTIONS_TABLE + " (" + COLUMN_RS_CONTAINERID + " INTEGER PRIMARY KEY, " + COLUMN_RS_ROW + " REAL NOT NULL, " + COLUMN_RS_SECTION + " REAL NOT NULL)";
    String createTable_syncinfo = "CREATE TABLE IF NOT EXISTS " + SYNC_INFO_TABLE + " (" + COLUMN_S_SCAN_ID + " INTEGER PRIMARY KEY, " + COLUMN_S_SYNC_STATUS + " INTEGER NOT NULL)";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static Cursor readFromLocalDatabase(SQLiteDatabase db) {
        String[] projection = {COLUMN_S_SCAN_ID, COLUMN_S_SYNC_STATUS};

        return (db.query(SYNC_INFO_TABLE, projection, null, null, null, null, null));
    }

    public void saveToLocalDatabase(int scan_id, int sync_status, SQLiteDatabase db){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_S_SCAN_ID, scan_id);
        cv.put(COLUMN_S_SYNC_STATUS,sync_status);
        db.insert(SYNC_INFO_TABLE, null, cv);
    }

    public void updateLocalDatabase(int scan_id, int sync_status,SQLiteDatabase db){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_S_SYNC_STATUS,sync_status);
        String selection = COLUMN_S_SCAN_ID + " LIKE ?";
        String id = String.valueOf(scan_id);
        String[] args = {id};
        db.update(SYNC_INFO_TABLE, cv, selection, args);
    }

    // This is called the first time a database is accessed. There should be code in here to create a new database.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTable_credentials);
        db.execSQL(createTable_barcode);
        db.execSQL(createTable_locationdata);
        db.execSQL(createTable_rowsandsections);
        db.execSQL(createTable_syncinfo);
    }

    // This is called if the database version number changes. It prevents previous users apps from breaking when you change the database design.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS '" + CREDENTIALS_TABLE + "'");
            db.execSQL("DROP TABLE IF EXISTS '" + BARCODE_TABLE + "'");
            db.execSQL("DROP TABLE IF EXISTS '" + LOCATIONDATA_TABLE + "'");
            db.execSQL("DROP TABLE IF EXISTS '" + ROWSANDSECTIONS_TABLE + "'");
            onCreate(db);
        }
    }

    // Adding an entry to Registration Table
    public boolean addOneToRegTable(UserRegistrationModel userRegistrationModel){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        boolean b = checkIfEntryInDb(userRegistrationModel);

        if (b) {
            return false;
        }
        else {

            cv.put(COLUMN_C_FIRSTNAME, userRegistrationModel.getFirstName());
            cv.put(COLUMN_C_LASTNAME, userRegistrationModel.getLastName());
            cv.put(COLUMN_C_USERNAME, userRegistrationModel.getUsername());
            cv.put(COLUMN_C_PASSWORD, userRegistrationModel.getPassword());
            cv.put(COLUMN_C_EMAIL,userRegistrationModel.getUserEmail());

            long insert = db.insert(CREDENTIALS_TABLE, null, cv);
            return true;
        }
    }

    // Adding an entry to Barcode Table
    public boolean addOneToBarcodeTable(BarcodeModel barcodeModel){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_B_CONTAINERID, barcodeModel.getContainerId());
        cv.put(COLUMN_B_BARCODEID, barcodeModel.getBarcodeId());
        cv.put(COLUMN_B_CONTAINERNAME, barcodeModel.getContainerName());
        cv.put(COLUMN_B_DIRTYBIT, barcodeModel.getDirtyBit());

        long insert = db.insert(CREDENTIALS_TABLE, null, cv);

        return insert != -1;
    }

    //  Adding an entry to LocationData Table
    public boolean addOneToLocationTable(LocationDataModel locationDataModel){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_LD_CONTAINERID, locationDataModel.getContainerId());
        cv.put(COLUMN_LD_LATITUDE, locationDataModel.getLatitude());
        cv.put(COLUMN_LD_LONGITUDE, locationDataModel.getLongitude());

        long insert = db.insert(LOCATIONDATA_TABLE, null, cv);

        return insert != -1;
    }

    // Adding an entry to RowsAndSections Table
    public boolean addOneToRowSectionsTable(RowsAndSectionsModel rowsAndSectionsModel){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_RS_CONTAINERID, rowsAndSectionsModel.getContainerId());
        cv.put(COLUMN_RS_ROW, rowsAndSectionsModel.getRow());
        cv.put(COLUMN_RS_SECTION, rowsAndSectionsModel.getSection());

        long insert = db.insert(CREDENTIALS_TABLE, null, cv);

        return insert != -1;
    }

    // Delete an entry from the Registration Table
    public boolean deleteOneFromRegTable(UserRegistrationModel userRegistrationModel){
        // Find userRegistrationModel in the database. If it is found, delete it and return true.
        // If it is not found, return false

        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + CREDENTIALS_TABLE + " WHERE " + COLUMN_C_USERNAME + " = " + userRegistrationModel.getUsername();

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

    // Delete an entry from the Barcode Table
    public boolean deleteOneFromBarTable(BarcodeModel barcodeModel){
        // Find userRegistrationModel in the database. If it is found, delete it and return true.
        // If it is not found, return false

        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + BARCODE_TABLE + " WHERE " + COLUMN_B_CONTAINERID + " = " + barcodeModel.getBarcodeId();

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

    // Delete an entry from the LocationData Table
    public boolean deleteOneFromLDTable(LocationDataModel locationDataModel){
        // Find userRegistrationModel in the database. If it is found, delete it and return true.
        // If it is not found, return false

        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + LOCATIONDATA_TABLE + " WHERE " + COLUMN_LD_CONTAINERID + " = " + locationDataModel.getContainerId();

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

    // Delete an entry from the RowsAndSections Table
    public boolean deleteOneFromRSTable(RowsAndSectionsModel rowsAndSectionsModel){
        // Find userRegistrationModel in the database. If it is found, delete it and return true.
        // If it is not found, return false

        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + ROWSANDSECTIONS_TABLE + " WHERE " + COLUMN_RS_CONTAINERID + " = " + rowsAndSectionsModel.getContainerId();

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

    public boolean checkIfEntryInDb(UserRegistrationModel userRegistrationModel){

        String queryString = "SELECT * FROM " + CREDENTIALS_TABLE + " WHERE " + COLUMN_C_USERNAME + " = ?;";
        String[] args = {userRegistrationModel.getUsername()};

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cur = db.rawQuery(queryString, args);

        if(cur.getCount() == 1){
            return true;
        }
        else{
            return false;
        }
    }

//    public List<SyncEntry> getEveryEntryToSync(){
//        List<SyncEntry> returnList = new ArrayList<>();
//    }

    public List<UserRegistrationModel> getEveryoneRegistered(){
        List<UserRegistrationModel> returnList = new ArrayList<>();

        // Get data from the database

        String queryString = "SELECT * FROM " + CREDENTIALS_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            // Loop through the cursor (result set) and create new user objects. Put them into the return list.

            do {
                String userFirstName = cursor.getString(1);
                String userLastName = cursor.getString(2);
                String userUsername = cursor.getString(3);
                String userPassword = cursor.getString(4);
                String userEmail = cursor.getString(5);

                UserRegistrationModel newUserRegistrationModel = new UserRegistrationModel(userFirstName, userLastName, userUsername, userPassword, userEmail);
                returnList.add(newUserRegistrationModel);


            } while(cursor.moveToNext());

        }

        // Close both the cursor and the db when done.
        cursor.close();
        db.close();
        return returnList;
    }
}
