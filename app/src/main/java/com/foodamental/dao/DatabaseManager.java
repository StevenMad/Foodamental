package com.foodamental.dao;

/**
 * Created by YOUSSEF on 14/07/2016.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Classe qui gère les opérations sur la base
 */
public class DatabaseManager {
    private Integer mOpenCounter = 0;

    private static DatabaseManager instance;
    private static SQLiteOpenHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;
    private static DBHelper dbHelper;


    private DatabaseManager(SQLiteOpenHelper helper){
        initializeInstance(helper);

    }
    /**
     * Fonction qui initialise le database Manager
     * @param helper
     */
    public static synchronized void initializeInstance(SQLiteOpenHelper helper) {
        if (instance == null) {


            mDatabaseHelper = helper;
        }
    }

    /**
     * Fonction qui renvoie l'instance du database Manager
     * @return
     */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            dbHelper = new DBHelper();
            instance = new DatabaseManager(dbHelper);

        }

        return instance;
    }

    /**
     * Fonction qui ouvre la database
     * @return
     */
    public synchronized SQLiteDatabase openDatabase() {
        mOpenCounter+=1;
        if(mOpenCounter == 1) {
            // Opening new database
            mDatabase = mDatabaseHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    /**
     * Fonction qui ferme la database
     */
    public synchronized void closeDatabase() {
        mOpenCounter-=1;
        if(mOpenCounter == 0) {
            // Closing database
            mDatabase.close();

        }
    }
}