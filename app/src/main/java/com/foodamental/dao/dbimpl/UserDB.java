package com.foodamental.dao.dbimpl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.foodamental.dao.DatabaseManager;
import com.foodamental.dao.interfaces.IUserDB;
import com.foodamental.dao.model.FoodUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by YOUSSEF on 14/07/2016.
 */

/**
 * Classe pour la table user
 */
public class UserDB implements IUserDB {

    public static final String FOODB_TABLE_NAME = "FoodUser";
    //FoodUser table Columns names
    public static final String FOODB_COLUMN_ID = "ID";
    public static final String FOODB_COLUMN_USERNAME = "USERNAME";
    public static final String FOODB_COLUMN_PASSWORD = "PASSWORD";
    public static final String FOODB_COLUMN_BIRTHDAY = "BIRTHDAY";
    public static final String FOODB_COLUMN_EMAIL = "MAIL";
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public UserDB() {
        super();
    }

    /**
     * Fonction qui ajoute un user
     * @param Fooduser
     */
    @Override
    public void addUser(FoodUser Fooduser) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();

        values.put(FOODB_COLUMN_USERNAME, Fooduser.getUsername()); // user name
        values.put(FOODB_COLUMN_PASSWORD, Fooduser.getPassword() == null ? "" :Fooduser.getPassword() ); // user password
        values.put(FOODB_COLUMN_BIRTHDAY, dateFormat.format(Fooduser.getBirthday())); // user birthday
        values.put(FOODB_COLUMN_EMAIL, Fooduser.getEmail()  == null ? "" :Fooduser.getEmail()); // user email

        // Insert Row
        db.insert(FOODB_TABLE_NAME, null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    /**
     * Fonction qui renvoie un user à partir de l'ID
     * @param id
     * @return
     */
    @Override
    public FoodUser getFoodUser(int id) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        Cursor cursor = db.query(FOODB_TABLE_NAME, new String[]{FOODB_COLUMN_ID, FOODB_COLUMN_USERNAME,
                        FOODB_COLUMN_PASSWORD, FOODB_COLUMN_BIRTHDAY, FOODB_COLUMN_EMAIL}, FOODB_COLUMN_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        FoodUser contact = null;
        try {
            contact = new FoodUser(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), (Date) dateFormat.parse(cursor.getString(3)), cursor.getString(4));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Return user
        DatabaseManager.getInstance().closeDatabase();

        return contact;
    }

    /**
     * Fonction qui renvoie tous les users
     * @return
     */
    @Override
    public List<FoodUser> getALLUser() {
        List<FoodUser> userList = new ArrayList<FoodUser>();
        // Select all Query
        String selectQuery = "SELECT * FROM " + FOODB_TABLE_NAME;

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                FoodUser user = new FoodUser();
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setUsername(cursor.getString(1));
                user.setPassword(cursor.getString(2));
                try {
                    user.setBirthday((Date) dateFormat.parse(cursor.getString(3)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                user.setEmail(cursor.getString(4));
                // Adding contact to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        // return contact list
        DatabaseManager.getInstance().closeDatabase();

        return userList;
    }

    /**
     * Fonction qui renvoie le nombre de users
     * @return
     */
    @Override
    public int getUserCount() {
        String countQuery = "SELECT * FROM" + FOODB_TABLE_NAME;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        DatabaseManager.getInstance().closeDatabase();

        return cursor.getCount();
    }

    /**
     * Fonction qui update un user
     * @param user
     * @return
     */
    @Override
    public int updateUser(FoodUser user) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put(FOODB_COLUMN_USERNAME, user.getUsername());
        values.put(FOODB_COLUMN_PASSWORD, user.getPassword());
        values.put(FOODB_COLUMN_BIRTHDAY, dateFormat.format(user.getBirthday()));
        values.put(FOODB_COLUMN_EMAIL, user.getEmail());

        //updating row
        int result = db.update(FOODB_TABLE_NAME, values, FOODB_COLUMN_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        DatabaseManager.getInstance().closeDatabase();
        return result;
    }

    /**
     * Fonction qui efface un user
     * @param user
     */
    @Override
    public void deleteUser(FoodUser user) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(FOODB_TABLE_NAME, FOODB_COLUMN_ID + " = ? ",
                new String[]{String.valueOf(user.getId())});
        DatabaseManager.getInstance().closeDatabase();
    }
}
