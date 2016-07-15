package com.foodamental;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by YOUSSEF on 15/07/2016.
 */
public class FrigoDB {
    public static final String FRIGODB_TABLE_NAME = "FRIGO";
    //FoodUser table Columns names
    public static final String FRIGODB_COLUMN_ID = "ID_FRIGO";
    public static final String FRIGODB_COLUMN_ID_PRODUCT = "IDPRODUCT";
    public static final String FRIGODB_COLUMN_CATEGORIE = "CATEGORIE";
    public static final String FRIGODB_COLUMN_DATE_PEROMPT= "DATEPEROMPT";
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



    public FrigoDB() {
        super();
    }





    // Adding new user

    public void addProduct(FrigoObject frigo) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();

        values.put(FRIGODB_COLUMN_ID, frigo.getId()); // user name
        values.put(FRIGODB_COLUMN_ID_PRODUCT, frigo.getIdProduct()); // user name
        values.put(FRIGODB_COLUMN_CATEGORIE, frigo.getCategory()); // user brand
        values.put(FRIGODB_COLUMN_DATE_PEROMPT, dateFormat.format(frigo.getDatePerompt())); // user image

        // Insert Row
        db.insert(FRIGODB_TABLE_NAME, null, values);
        DatabaseManager.getInstance().closeDatabase();

    }

    // Get one user
    public FrigoObject getProduct(Integer id) throws ParseException {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        Cursor cursor = db.query(FRIGODB_TABLE_NAME, new String[]{FRIGODB_COLUMN_ID, FRIGODB_COLUMN_ID_PRODUCT,
                        FRIGODB_COLUMN_CATEGORIE, FRIGODB_COLUMN_DATE_PEROMPT}, FRIGODB_COLUMN_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        FrigoObject product = new FrigoObject(Long.parseLong(cursor.getString(0)), Long.parseLong(cursor.getString(1)), Integer.parseInt(cursor.getString(2)), (Date) dateFormat.parse(cursor.getString(3)));
        // Return user
        DatabaseManager.getInstance().closeDatabase();

        return product;
    }

    // Getting All users

    public List<FrigoObject> getALLProduct() throws ParseException {
        List<FrigoObject> frigoList = new ArrayList<FrigoObject>();
        // Select all Query
        String selectQuery = "SELECT * FROM " + FRIGODB_TABLE_NAME;

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                FrigoObject product = new FrigoObject(Long.parseLong(cursor.getString(0)), Long.parseLong(cursor.getString(1)), Integer.parseInt(cursor.getString(2)), (Date) dateFormat.parse(cursor.getString(3)));
                // Adding contact to list
                frigoList.add(product);
            } while (cursor.moveToNext());
        }
        // return contact list
        DatabaseManager.getInstance().closeDatabase();
        return frigoList;
    }

    //getting users Count
    public int getProductCount() {
        String countQuery = "SELECT * FROM" + FRIGODB_TABLE_NAME;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        DatabaseManager.getInstance().closeDatabase();
        return cursor.getCount();
    }

    //Updating a product
    public int updateProduct(FrigoObject frigo) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put(FRIGODB_COLUMN_ID, frigo.getId()); // user name
        values.put(FRIGODB_COLUMN_ID_PRODUCT, frigo.getIdProduct()); // user name
        values.put(FRIGODB_COLUMN_CATEGORIE, frigo.getCategory()); // user brand
        values.put(FRIGODB_COLUMN_DATE_PEROMPT, dateFormat.format(frigo.getDatePerompt())); // user image
        int result = db.update(FRIGODB_TABLE_NAME, values, FRIGODB_COLUMN_ID + " = ?",
                new String[]{String.valueOf(frigo.getId())});
        //updating row
        DatabaseManager.getInstance().closeDatabase();

        return result;
    }

    // Deleting a product
    public void deleteProduct(FrigoObject frigo) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.delete(FRIGODB_TABLE_NAME, FRIGODB_COLUMN_ID + " = ? ",
                new String[]{String.valueOf(frigo.getId())});
        DatabaseManager.getInstance().closeDatabase();
    }

    public List<ProductDTO> getAllProduct() throws ParseException {
        List<ProductDTO> listproduct = new ArrayList<>();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = "SELECT ID_FRIGO, CATEGORIE, DATEPEROMPT, NAME, IMAGE_URL, BRAND FROM FRIGO JOIN PRODUIT ON ID_PRODUCT = IDPRODUCT;";
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ProductDTO product = new ProductDTO(Long.parseLong(cursor.getString(0)),Integer.parseInt(cursor.getString(1)),(Date) dateFormat.parse(cursor.getString(2)),cursor.getString(3) ,cursor.getString(4) , cursor.getString(5));
                // Adding contact to list
                listproduct.add(product);
            } while (cursor.moveToNext());
        }
        return listproduct;
    }
}
