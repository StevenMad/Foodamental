package com.foodamental;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Madhow on 06/06/2016.
 */
public class DBHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "Foodb.db";
    public static final String FOODB_TABLE_NAME = "FoodUser";
    public static final String FOODB_COLUMN_ID = "id";
    public static final String FOODB_COLUMN_USERNAME = "username";
    public static final String FOODB_COLUMN_PASSWORD = "password";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table FoodUser" +
                "(" +
                        "id integer primary key," +
                        "username text," +
                        "password text" +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS FoodUser");
        onCreate(db);
    }

    public boolean updateFoodUser(Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username","Madhow");
        cv.put("password","Mike");
        db.update("FoodUser",cv,"id= ?",new String[]{Integer.toString(id)});
        return true;
    }

    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from FoodUser where id="+id+"", null );
        return res;
    }

    public ArrayList<String> displayUsers()
    {
        ArrayList<String> users = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("Select * from FoodUser",null);
        res.moveToFirst();
        while(res.isAfterLast()== false)
        {
            users.add(res.getString(res.getColumnIndex(FOODB_COLUMN_USERNAME)));
            res.moveToNext();
        }
        return users;
    }
}
