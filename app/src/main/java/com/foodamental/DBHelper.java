package com.foodamental;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseCorruptException;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

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
    public static final String FOODB_COLUMN_ID = "id";
    public static final String FOODB_COLUMN_USERNAME = "username";
    public static final String FOODB_COLUMN_PASSWORD = "password";
    public static final String FOODB_COLUMN_BIRTHDAY = "birthday";
    public static final String FOODB_COLUMN_EMAIL = "email";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    // Create the data base
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE" + FOODB_TABLE_NAME + "("
                + FOODB_COLUMN_ID + " INTEGER PRIMARY KEY,"
                + FOODB_COLUMN_USERNAME + " TEXT,"
                + FOODB_COLUMN_PASSWORD + " TEXT,"
                + FOODB_COLUMN_BIRTHDAY + " TEXT,"
                + FOODB_COLUMN_EMAIL + " TEXT,"
                + ")";
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    // Upgrade the data base
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed ;
        db.execSQL("DROP TABLE IF EXISTS FoodUser");
        // Create the new table ;
        onCreate(db);
    }

    // Adding new user

    public void addUser(FoodUser Fooduser)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(FOODB_COLUMN_USERNAME, Fooduser.getUsername()); // user name
        values.put(FOODB_COLUMN_PASSWORD, Fooduser.getPassword()); // user password
        values.put(FOODB_COLUMN_BIRTHDAY, Fooduser.getBirthday()); // user birthday
        values.put(FOODB_COLUMN_EMAIL, Fooduser.getEmail()); // user email

        // Insert Row
        db.insert(FOODB_TABLE_NAME, null, values);
        db.close(); // close database connection
    }

    // Get one user
    public FoodUser getFoodUser (int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(FOODB_TABLE_NAME, new String[]{FOODB_COLUMN_ID,FOODB_COLUMN_USERNAME,
                FOODB_COLUMN_PASSWORD,FOODB_COLUMN_BIRTHDAY,FOODB_COLUMN_EMAIL}, FOODB_COLUMN_ID + "=?", new String[]{String.valueOf(id)},
                null, null,null,null);
        if (cursor != null)
            cursor.moveToFirst();

        FoodUser contact = new FoodUser(Integer.parseInt(cursor.getString(0)),cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4));
        // Return user
        return contact;
    }

    // Getting All users

    public List<FoodUser> getALLUser(){
        List<FoodUser> userList = new ArrayList<FoodUser>();
        // Select all Query
        String selectQuery = "SELECT * FROM " + FOODB_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all rows and adding to list
        if (cursor.moveToFirst()){
            do {
                FoodUser user = new FoodUser();
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setUsername(cursor.getString(1));
                user.setPassword(cursor.getString(2));
                user.setBirthday(cursor.getString(3));
                user.setEmail(cursor.getString(4));
                // Adding contact to list
                userList.add(user);
            }while (cursor.moveToNext());
        }
        // return contact list
        return userList;
    }

    //getting users Count
    public int getUserCount(){
        String countQuery = "SELECT * FROM" + FOODB_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        return cursor.getCount();
    }

    //Updating a user
    public int updateUser(FoodUser user){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FOODB_COLUMN_USERNAME,user.getUsername());
        values.put(FOODB_COLUMN_PASSWORD,user.getPassword());
        values.put(FOODB_COLUMN_BIRTHDAY,user.getBirthday());
        values.put(FOODB_COLUMN_EMAIL,user.getEmail());

        //updating row
        return db.update(FOODB_TABLE_NAME,values,FOODB_COLUMN_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
    }

    // Deleting a user
    public void deleteUser(FoodUser user){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FOODB_TABLE_NAME, FOODB_COLUMN_ID + " = ? ",
                new String[] {String.valueOf(user.getId())});
        db.close();
    }

    public boolean updateFoodUser(Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username","Madhow");
        cv.put("password","Mike");
        cv.put("birthday","16/06/1992");
        cv.put("email","mm@foodamental.com");
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


