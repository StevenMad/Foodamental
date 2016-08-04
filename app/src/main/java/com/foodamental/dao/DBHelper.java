package com.foodamental.dao;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.foodamental.activity.MyMainPage;

import java.util.ArrayList;

/**
 * Created by Madhow on 06/06/2016.
 * Modified by Fangyi on 16/06/2016.
 */

public class DBHelper extends SQLiteOpenHelper {

    // Data base version
    private static final int DATABASE_VERSION = 1;
    // Data base name
    public static final String DATABASE_NAME = "Foodb.db";
    // Contacts table name
    public static final String FOODB_TABLE_NAME = "FoodUser";
    //FoodUser table Columns names
    public static final String FOODB_COLUMN_ID = "ID";
    public static final String FOODB_COLUMN_USERNAME = "USERNAME";
    public static final String FOODB_COLUMN_PASSWORD = "PASSWORD";
    public static final String FOODB_COLUMN_BIRTHDAY = "BIRTHDAY";
    public static final String FOODB_COLUMN_EMAIL = "MAIL";

    public static final String PRODUCTDB_TABLE_NAME = "PRODUCT";
    //product table Columns names
    public static final String PRODUCTDB_COLUMN_ID = "ID_PRODUCT";
    public static final String PRODUCTDB_COLUMN_NAME = "NAME";
    public static final String PRODUCTDB_COLUMN_BRAND = "BRAND";
    public static final String PRODUCTDB_COLUMN_IMAGE_URL= "IMAGE_URL";
    public static final String PRODUCTDB_COLUMN_CATEGORY = "CATEGORY";


    public static final String FRIGODB_TABLE_NAME = "FRIGO";
    //frigo table Columns names
    public static final String FRIGODB_COLUMN_ID = "ID_FRIGO";
    public static final String FRIGODB_COLUMN_ID_PRODUCT = "IDPRODUCT";
    public static final String FRIGODB_COLUMN_DATE_PEROMPT= "EXPIRY_DATE";

    private static final String TAG = DBHelper.class.getSimpleName().toString();

    public DBHelper() {
        super(MyMainPage.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    // Create the data base
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + FOODB_TABLE_NAME + "("
                + FOODB_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FOODB_COLUMN_USERNAME + " TEXT,"
                + FOODB_COLUMN_PASSWORD + " TEXT,"
                + FOODB_COLUMN_BIRTHDAY + " TEXT,"
                + FOODB_COLUMN_EMAIL + " TEXT"
                + ")";
        String CREATE_PRODUCT_TABLE = "CREATE TABLE " + PRODUCTDB_TABLE_NAME + "("
                + PRODUCTDB_COLUMN_ID + " INTEGER PRIMARY KEY, "
                + PRODUCTDB_COLUMN_NAME + " TEXT,"
                + PRODUCTDB_COLUMN_BRAND + " TEXT,"
                + PRODUCTDB_COLUMN_CATEGORY + " INTEGER,"
                + PRODUCTDB_COLUMN_IMAGE_URL + " TEXT"
                + ")";
        String CREATE_FRIGO_TABLE = "CREATE TABLE " + FRIGODB_TABLE_NAME + "("
                + FRIGODB_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FRIGODB_COLUMN_ID_PRODUCT + " INTEGER, "

                + FRIGODB_COLUMN_DATE_PEROMPT + " TEXT,"
                +" FOREIGN KEY(" + FRIGODB_COLUMN_ID_PRODUCT +") REFERENCES PRODUIT(ID_PRODUCT) " + ")";
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_PRODUCT_TABLE);
        db.execSQL(CREATE_FRIGO_TABLE);
    }
    @Override
    // Upgrade the data base
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, String.format("SQLiteDatabase.onUpgrade(%d -> %d)", oldVersion, newVersion));

        // Drop older table if existed ;
        db.execSQL("DROP TABLE IF EXISTS FoodUser");
        db.execSQL("DROP TABLE IF EXISTS PRODUCT");
        db.execSQL("DROP TABLE IF EXISTS FRIGO");

        // Create the new table ;
        onCreate(db);
    }
    public Cursor getData(int id) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor res = db.rawQuery("select * from FoodUser where id=" + id + "", null);
        DatabaseManager.getInstance().closeDatabase();

        return res;
    }

    public ArrayList<String> displayUsers() {
        ArrayList<String> users = new ArrayList<String>();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor res = db.rawQuery("Select * from FoodUser", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            users.add(res.getString(res.getColumnIndex(FOODB_COLUMN_USERNAME)));
            res.moveToNext();
        }
        DatabaseManager.getInstance().closeDatabase();

        return users;
    }


}