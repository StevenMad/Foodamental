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
    //frigo table Columns names
    public static final String FRIGODB_COLUMN_ID = "ID_FRIGO";
    public static final String FRIGODB_COLUMN_ID_PRODUCT = "IDPRODUCT";
    public static final String FRIGODB_COLUMN_DATE_PEROMPT= "EXPIRY_DATE";
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



    public FrigoDB() {
        super();
    }





    // Adding new product in frigo

    public void addProduct(FrigoObject frigo) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();

        values.put(FRIGODB_COLUMN_ID, frigo.getIdFrigo()); // frigo id
        values.put(FRIGODB_COLUMN_ID_PRODUCT, frigo.getIdProduct()); // product id in frigo
        values.put(FRIGODB_COLUMN_DATE_PEROMPT, dateFormat.format(frigo.getDatePerompt())); // expiry date

        // Insert Row
        db.insert(FRIGODB_TABLE_NAME, null, values);
        DatabaseManager.getInstance().closeDatabase();

    }

    // Get one product in frigo
    public FrigoObject getProduct(Integer id) throws ParseException {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        Cursor cursor = db.query(FRIGODB_TABLE_NAME, new String[]{FRIGODB_COLUMN_ID, FRIGODB_COLUMN_ID_PRODUCT
                , FRIGODB_COLUMN_DATE_PEROMPT}, FRIGODB_COLUMN_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        FrigoObject product = new FrigoObject(Long.parseLong(cursor.getString(0)), Long.parseLong(cursor.getString(1)), (Date) dateFormat.parse(cursor.getString(2)));
        // Return frigo object
        DatabaseManager.getInstance().closeDatabase();

        return product;
    }

    // Getting All product in frigo

    public List<FrigoObject> getALLProduct() throws ParseException {
        List<FrigoObject> frigoList = new ArrayList<FrigoObject>();
        // Select all Query
        String selectQuery = "SELECT * FROM " + FRIGODB_TABLE_NAME;

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                FrigoObject product = new FrigoObject(Long.parseLong(cursor.getString(0)), Long.parseLong(cursor.getString(1)), (Date) dateFormat.parse(cursor.getString(2)));
                // Adding product to list
                frigoList.add(product);
            } while (cursor.moveToNext());
        }
        // return frigo products list
        DatabaseManager.getInstance().closeDatabase();
        return frigoList;
    }

    //getting frigo products Count
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
        values.put(FRIGODB_COLUMN_ID, frigo.getIdFrigo()); // frigo id
        values.put(FRIGODB_COLUMN_ID_PRODUCT, frigo.getIdProduct()); // product id in frigo
        values.put(FRIGODB_COLUMN_DATE_PEROMPT, dateFormat.format(frigo.getDatePerompt())); // expiry date
        int result = db.update(FRIGODB_TABLE_NAME, values, FRIGODB_COLUMN_ID + " = ?",
                new String[]{String.valueOf(frigo.getIdFrigo())});
        //updating row
        DatabaseManager.getInstance().closeDatabase();

        return result;
    }

    // Deleting a product in frigo
    public void deleteProduct(FrigoObject frigo) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.delete(FRIGODB_TABLE_NAME, FRIGODB_COLUMN_ID + " = ? ",
                new String[]{String.valueOf(frigo.getIdFrigo())});
        DatabaseManager.getInstance().closeDatabase();
    }

    // Deleting a product with id
    public void deleteProductWithId(Long id) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.delete(FRIGODB_TABLE_NAME, FRIGODB_COLUMN_ID + " = ? ",
                new String[]{String.valueOf(id)});
        DatabaseManager.getInstance().closeDatabase();
    }

    public List<FrigoObject> getAllProduct() {
        List<FrigoObject> listproduct = new ArrayList<>();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = "SELECT " + FRIGODB_COLUMN_ID + ", " +  " CATEGORY, " + FRIGODB_COLUMN_DATE_PEROMPT + " , " + "NAME, IMAGE_URL, BRAND FROM " + FRIGODB_TABLE_NAME + " JOIN PRODUCT ON ID_PRODUCT =  " + FRIGODB_COLUMN_ID_PRODUCT;
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                FrigoObject product = null;
                try {
                    product = new FrigoObject(Long.parseLong(cursor.getString(0)),Integer.parseInt(cursor.getString(1)),(Date) dateFormat.parse(cursor.getString(2)),cursor.getString(3) ,cursor.getString(4) , cursor.getString(5));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                // Adding contact to list
                listproduct.add(product);
            } while (cursor.moveToNext());
        }
        return listproduct;
    }


    public List<FrigoObject> getDistinctProductList() throws ParseException
    {
        List<FrigoObject> list = getAllProduct();
        List<FrigoObject> newList = new ArrayList<>();
        boolean flag=false;
        for(FrigoObject produit:list)
        {
            for(int i=0;i<newList.size();i++) {
                flag = false;
                if (!produit.getName().equals(newList.get(i).getName()) && flag != true) {
                    flag = false;
                } else
                    flag = true;
            }
            if(flag==false)
                newList.add(produit);
        }
        return newList;
    }
}
