package com.example.morningstar7;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "morningstar.db";
    private static final int DB_VERSION = 1;
    public static final int SYNC_STATUS_OK = 0;
    public static final int SYNC_STATUS_FAILED = 1;
    public static final String SERVER_URL = "http://10.0.0.195/morningstar/syncinfo.php";
    public static final String UI_UPDATE_BROADCAST = "com.example.morningstar7.uiupdatebroadcast";

    // Database Tables
    public static final String CREDENTIALS_TABLE = "credentials";
    public static final String BARCODE_TABLE = "barcodes";

    // Credentials' Columns
    public static final String COLUMN_C_USER_ID = "c_userId";
    public static final String COLUMN_C_FIRSTNAME = "c_firstname";
    public static final String COLUMN_C_LASTNAME = "c_lastname";
    public static final String COLUMN_C_USERNAME = "c_username";
    public static final String COLUMN_C_PASSWORD = "c_password";
    public static final String COLUMN_C_EMAIL = "c_email";

    // Barcode's Columns
    public static final String COLUMN_B_BARCODEID = "b_barcodeId";
    public static final String COLUMN_B_CONTAINERNAME = "b_containerName";
    public static final String COLUMN_B_LATITUDE = "b_latitude";
    public static final String COLUMN_B_LONGITUDE = "b_longitude";
    public static final String COLUMN_B_ROW = "b_row";
    public static final String COLUMN_B_SECTION = "b_section";
    public static final String COLUMN_B_LASTUPDATED = "b_lastUpdated";
    public static final String COLUMN_B_SYNCSTATUS = "b_syncStatus";


    // Creating Tables SQL
    String createTable_credentials = "CREATE TABLE IF NOT EXISTS " + CREDENTIALS_TABLE + " (" + COLUMN_C_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_C_FIRSTNAME + " TEXT NOT NULL, " + COLUMN_C_LASTNAME + " TEXT NOT NULL, " + COLUMN_C_USERNAME + " Text NOT NULL, " + COLUMN_C_PASSWORD + " TEXT NOT NULL, " + COLUMN_C_EMAIL + " TEXT NOT NULL)";
    String createTable_barcode = "CREATE TABLE IF NOT EXISTS " + BARCODE_TABLE + " (" + COLUMN_B_BARCODEID + " VARCHAR(12) PRIMARY KEY, " + COLUMN_B_CONTAINERNAME + " TEXT NOT NULL, " + COLUMN_B_LATITUDE + " REAL NOT NULL, " + COLUMN_B_LONGITUDE + " REAL NOT NULL, " + COLUMN_B_ROW + " INTEGER NOT NULL, " + COLUMN_B_SECTION + " INTEGER NOT NULL, " + COLUMN_B_LASTUPDATED + " TEXT DEFAULT CURRENT_TIMESTAMP, " + COLUMN_B_SYNCSTATUS + " INTEGER NOT NULL)";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public Cursor readFromLocalDatabase() {
        String query = "SELECT * FROM " + BARCODE_TABLE + ";";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null)
            cursor = db.rawQuery(query, null);

        return cursor;
    }

    public void updateLocalDatabase(String barcode_id, int sync_status){
        if(sync_status == 0) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_B_SYNCSTATUS, sync_status);
            db.update(BARCODE_TABLE, cv, " b_barcodeId = ?", new String[]{barcode_id});
        }
    }

    // This is called the first time a database is accessed. There should be code in here to create a new database.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTable_credentials);
        db.execSQL(createTable_barcode);
    }

    // This is called if the database version number changes. It prevents previous users apps from breaking when you change the database design.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS '" + CREDENTIALS_TABLE + "'");
            db.execSQL("DROP TABLE IF EXISTS '" + BARCODE_TABLE + "'");
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

        cv.put(COLUMN_B_BARCODEID, barcodeModel.getBarcodeId());
        cv.put(COLUMN_B_CONTAINERNAME, barcodeModel.getContainerName());
        cv.put(COLUMN_B_LATITUDE, barcodeModel.getLatitude());
        cv.put(COLUMN_B_LONGITUDE, barcodeModel.getLongitude());
        cv.put(COLUMN_B_ROW, barcodeModel.getRow());
        cv.put(COLUMN_B_SECTION, barcodeModel.getSection());
        cv.put(COLUMN_B_LASTUPDATED, barcodeModel.getLastUpdated());
        cv.put(COLUMN_B_SYNCSTATUS, barcodeModel.getSync_status());

        long insert = db.insert(CREDENTIALS_TABLE, null, cv);

        return insert != -1;
    }

    public void insertOneToBarcodeTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        String q = "SELECT * FROM " + BARCODE_TABLE + " WHERE b_barcodeId = ? OR b_barcodeId = ?;";
        String val = "012345678905";
        String[] args = {val, "012345698905"};
        Cursor cur = db.rawQuery(q, args);

        if(cur.getCount() == 0) {
            String q1 = "INSERT INTO " + BARCODE_TABLE + " VALUES('012345678905', 'Wooden Box', '12.45129', '-167.12746', '5', '1', '2020-11-27 07:05:12.215', '1');";
            String q2 = "INSERT INTO " + BARCODE_TABLE + " VALUES('012345698905', 'Wooden Box', '12.45129', '-167.12746', '5', '1', '2020-11-27 07:05:12.215', '1');";
            db.execSQL(q1);
            db.execSQL(q2);
        }
        cur.close();
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
        String queryString = "DELETE FROM " + BARCODE_TABLE + " WHERE " + COLUMN_B_BARCODEID + " = " + barcodeModel.getBarcodeId();

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

        String queryString = "SELECT * FROM " + CREDENTIALS_TABLE + " WHERE " + COLUMN_C_USERNAME + " = ? AND " + COLUMN_C_PASSWORD + " = ?;";
        String[] args = {userRegistrationModel.getUsername(), userRegistrationModel.getPassword()};

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cur = db.rawQuery(queryString, args);

        if(cur.getCount() == 1){
            cur.close();
            return true;
        }
        else{
            cur.close();
            return false;
        }

    }

    public Cursor readAllDataFromBarcodeTable() {
        String query = "SELECT b_barcodeId, b_row, b_section, b_syncStatus FROM " + BARCODE_TABLE + ";";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null)
            cursor = db.rawQuery(query, null);

        return cursor;
    }
}
