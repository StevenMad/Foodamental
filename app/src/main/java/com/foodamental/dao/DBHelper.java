package com.foodamental.dao;


import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.foodamental.activity.Foodamental;
import com.foodamental.activity.MyMainPage;

/**
 * Created by Madhow on 06/06/2016.
 * Modified by Fangyi on 16/06/2016.
 */

/**
 * Classe pour création de bdd
 */
public class DBHelper extends SQLiteOpenHelper {

    // Data base version
    private static final int DATABASE_VERSION = 1;
    // Data base name
    public static final String DATABASE_NAME = "Foodb.db";

    public static final String PRODUCTDB_TABLE_NAME = "PRODUCT";
    //product table Columns names
    public static final String PRODUCTDB_COLUMN_ID = "ID_PRODUCT";
    public static final String PRODUCTDB_COLUMN_NAME = "NAME";
    public static final String PRODUCTDB_COLUMN_BRAND = "BRAND";
    public static final String PRODUCTDB_COLUMN_IMAGE_URL= "IMAGE_URL";
    public static final String PRODUCTDB_COLUMN_CATEGORY = "CATEGORY_ID";


    public static final String FRIGODB_TABLE_NAME = "FRIGO";
    //frigo table Columns names
    public static final String FRIGODB_COLUMN_ID = "ID_FRIGO";
    public static final String FRIGODB_COLUMN_ID_PRODUCT = "IDPRODUCT";
    public static final String FRIGODB_COLUMN_DATE_PEREMPT = "EXPIRY_DATE";
    public static final String FRIGODB_COLUMN_QUANTITY = "QUANTITY";

    public static final String CATEGORYDB_TABLE_NAME = "CATEGORY";
    //CATEGORY table Columns names
    public static final String CATEGORYDB_COLUMN_ID = "ID_CATEGORY";
    public static final String CATEGORYDB_COLUMN_NAME = "NAME_CATEGORY";

    public static final String OTHERFRIGOPRODUCTDB_TABLE_NAME = "OTHERPRODUCT";
    //OtherFrigoPrudct table Columns names
    public static final String OTHERFRIGOPRODUCTDB_COLUMN_ID = "ID_OTHER_PRODUCT";
    public static final String OTHERFRIGOPRODUCTDB_COLUMN_NAME = "NAME";
    public static final String OTHERFRIGOPRODUCTDB_COLUMN_DATE_PEREMPT = "EXPIRY_DATE";
    public static final String OTHERFRIGOPRODUCTDB_COLUMN_CATEGORY = "CATEGORY_ID";
    public static final String OTHERFRIGOPRODUCTDB_COLUMN_QUANTITY = "QUANTITY";





    private static final String TAG = DBHelper.class.getSimpleName().toString();

    public DBHelper() {
        super(Foodamental.getAppContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    // Create the data base
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCT_TABLE = "CREATE TABLE " + PRODUCTDB_TABLE_NAME + "("
                + PRODUCTDB_COLUMN_ID + " INTEGER PRIMARY KEY, "
                + PRODUCTDB_COLUMN_NAME + " TEXT,"
                + PRODUCTDB_COLUMN_BRAND + " TEXT,"
                + PRODUCTDB_COLUMN_IMAGE_URL + " TEXT,"
                + PRODUCTDB_COLUMN_CATEGORY + " INTEGER,"
                +" FOREIGN KEY(" + PRODUCTDB_COLUMN_CATEGORY  +") REFERENCES CATEGORY(ID_CATEGORY) "
                + ")";
        String CREATE_FRIGO_TABLE = "CREATE TABLE " + FRIGODB_TABLE_NAME + "("
                + FRIGODB_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FRIGODB_COLUMN_ID_PRODUCT + " INTEGER, "
                + FRIGODB_COLUMN_DATE_PEREMPT + " TEXT,"
                + FRIGODB_COLUMN_QUANTITY + " INTEGER,"
                +" FOREIGN KEY(" + FRIGODB_COLUMN_ID_PRODUCT +") REFERENCES PRODUCT(ID_PRODUCT) " + ")";

        String CREATE_CATEGORY_TABLE = "CREATE TABLE " + CATEGORYDB_TABLE_NAME + "("
                + CATEGORYDB_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CATEGORYDB_COLUMN_NAME + " TEXT )";

        String CREATE_OTHER_FRIGO_PRODUCT_TABLE = "CREATE TABLE " + OTHERFRIGOPRODUCTDB_TABLE_NAME + "("
                + OTHERFRIGOPRODUCTDB_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + OTHERFRIGOPRODUCTDB_COLUMN_NAME + " TEXT,"
                + OTHERFRIGOPRODUCTDB_COLUMN_DATE_PEREMPT + " TEXT,"
                + OTHERFRIGOPRODUCTDB_COLUMN_CATEGORY + " INTEGER,"
                + OTHERFRIGOPRODUCTDB_COLUMN_QUANTITY + " INTEGER ) ";
        db.execSQL(CREATE_CATEGORY_TABLE);
        db.execSQL(CREATE_PRODUCT_TABLE);
        db.execSQL(CREATE_FRIGO_TABLE);
        db.execSQL(CREATE_OTHER_FRIGO_PRODUCT_TABLE);


        db.execSQL("insert into " + CATEGORYDB_TABLE_NAME +" ("+CATEGORYDB_COLUMN_NAME  + ") "+" values('fruit');");
        db.execSQL("insert into " + CATEGORYDB_TABLE_NAME + " ("+CATEGORYDB_COLUMN_NAME  + ") "+" values('legume');");
        db.execSQL("insert into " + CATEGORYDB_TABLE_NAME + " ("+CATEGORYDB_COLUMN_NAME  + ")"+" values('beurre-huile');");
        db.execSQL("insert into " + CATEGORYDB_TABLE_NAME + " ("+CATEGORYDB_COLUMN_NAME  + ")"+" values('produit-laitier');");
        db.execSQL("insert into " + CATEGORYDB_TABLE_NAME + " ("+CATEGORYDB_COLUMN_NAME  + ")"+" values('oeuf');");
        db.execSQL("insert into " + CATEGORYDB_TABLE_NAME + " ("+CATEGORYDB_COLUMN_NAME  + ")"+" values('viande');");
        db.execSQL("insert into " + CATEGORYDB_TABLE_NAME + " ("+CATEGORYDB_COLUMN_NAME  + ")"+" values('poisson');");
        db.execSQL("insert into " + CATEGORYDB_TABLE_NAME + " ("+CATEGORYDB_COLUMN_NAME  + ")"+" values('boisson');");
        db.execSQL("insert into " + CATEGORYDB_TABLE_NAME + " ("+CATEGORYDB_COLUMN_NAME  + ")"+" values('céréale');");

    }

    /**
     * Upgrade de bdd
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, String.format("SQLiteDatabase.onUpgrade(%d -> %d)", oldVersion, newVersion));

        // Drop older table if existed ;
        db.execSQL("DROP TABLE IF EXISTS PRODUCT");
        db.execSQL("DROP TABLE IF EXISTS FRIGO");
        db.execSQL("DROP TABLE IF EXISTS CATEGORY");
        db.execSQL("DROP TABLE IF EXISTS OTHERPRODUCT");
        // Create the new table ;
        onCreate(db);
    }



}