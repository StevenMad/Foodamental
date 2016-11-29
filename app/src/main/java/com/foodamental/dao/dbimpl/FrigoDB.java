package com.foodamental.dao.dbimpl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.foodamental.dao.DatabaseManager;
import com.foodamental.dao.interfaces.IFrigoDB;
import com.foodamental.dao.model.FrigoObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by YOUSSEF on 15/07/2016.
 */

/**
 * Classe pour la table frigo
 */
public class FrigoDB implements IFrigoDB {


    public static final String FRIGODB_TABLE_NAME = "FRIGO";
    //frigo table Columns names
    public static final String FRIGODB_COLUMN_ID = "ID_FRIGO";
    public static final String FRIGODB_COLUMN_ID_PRODUCT = "IDPRODUCT";
    public static final String FRIGODB_COLUMN_DATE_PEREMPT = "EXPIRY_DATE";
    public static final String FRIGODB_COLUMN_QUANTITY = "QUANTITY";

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    public FrigoDB() {
        super();
    }


    /**
     * Fonction qui ajoute un produit dans bdd
     * @param frigo
     */
    @Override
    public void addProduct(FrigoObject frigo) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues valuesForFrigo = new ContentValues();
        ContentValues valuesForProduct = new ContentValues();

        valuesForProduct.put(ProductDB.PRODUCTDB_COLUMN_ID, frigo.getIdProduct()); // product id
        valuesForProduct.put(ProductDB.PRODUCTDB_COLUMN_NAME, frigo.getName()); // product name
        valuesForProduct.put(ProductDB.PRODUCTDB_COLUMN_BRAND, frigo.getBrand()); // product brand
        valuesForProduct.put(ProductDB.PRODUCTDB_COLUMN_IMAGE_URL, frigo.getImage()); // product image
        valuesForProduct.put(ProductDB.PRODUCTDB_COLUMN_CATEGORY, frigo.getCategory()); // product category

        db.insert(ProductDB.PRODUCTDB_TABLE_NAME, null, valuesForProduct);

        valuesForFrigo.put(FRIGODB_COLUMN_ID, frigo.getIdFrigo()); // frigo id
        valuesForFrigo.put(FRIGODB_COLUMN_ID_PRODUCT, frigo.getIdProduct()); // product id in frigo
        valuesForFrigo.put(FRIGODB_COLUMN_DATE_PEREMPT, dateFormat.format(frigo.getDatePerempt())); // expiry date
        valuesForFrigo.put(FRIGODB_COLUMN_QUANTITY, frigo.getQuantity()); // expiry date

        // Insert Row
        db.insert(FRIGODB_TABLE_NAME, null, valuesForFrigo);


        DatabaseManager.getInstance().closeDatabase();

    }

    /**
     * Fonction qui renvoie un produit par rapport à un ID
     * @param id
     * @return
     * @throws ParseException
     */
    @Override
    public FrigoObject getProduct(Integer id) throws ParseException {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        Cursor cursor = db.query(FRIGODB_TABLE_NAME, new String[]{FRIGODB_COLUMN_ID, FRIGODB_COLUMN_ID_PRODUCT
                , FRIGODB_COLUMN_DATE_PEREMPT, FRIGODB_COLUMN_QUANTITY}, FRIGODB_COLUMN_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        FrigoObject product = new FrigoObject(Long.parseLong(cursor.getString(0)), Long.parseLong(cursor.getString(1)), (Date) dateFormat.parse(cursor.getString(2)),Integer.parseInt(cursor.getString(3)) );
        // Return frigo object
        DatabaseManager.getInstance().closeDatabase();

        return product;
    }

    /**
     * Fonction qui envoie tous les produits
     * @return
     * @throws ParseException
     */
    @Override
    public List<FrigoObject> getALLProduct() throws ParseException {
        List<FrigoObject> frigoList = new ArrayList<FrigoObject>();
        // Select all Query
        String selectQuery = "SELECT * FROM " + FRIGODB_TABLE_NAME;

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                FrigoObject product = new FrigoObject(Long.parseLong(cursor.getString(0)), Long.parseLong(cursor.getString(1)), (Date) dateFormat.parse(cursor.getString(2)),Integer.parseInt(cursor.getString(3)));
                // Adding product to list
                frigoList.add(product);
            } while (cursor.moveToNext());
        }
        // return frigo products list
        DatabaseManager.getInstance().closeDatabase();
        return frigoList;
    }

    /**
     * Fonction qui renvoie le nombre de produits
     * @return
     */
    @Override
    public int getProductCount() {
        String countQuery = "SELECT * FROM" + FRIGODB_TABLE_NAME;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        DatabaseManager.getInstance().closeDatabase();
        return cursor.getCount();
    }

    /**
     * Fonction qui met à jour un produit
     * @param frigo
     * @return
     */
    @Override
    public int updateProduct(FrigoObject frigo) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put(FRIGODB_COLUMN_ID, frigo.getIdFrigo()); // frigo id
        values.put(FRIGODB_COLUMN_ID_PRODUCT, frigo.getIdProduct()); // product id in frigo
        values.put(FRIGODB_COLUMN_DATE_PEREMPT, dateFormat.format(frigo.getDatePerempt())); // expiry date
        values.put(FRIGODB_COLUMN_QUANTITY, frigo.getQuantity()); // expiry date

        int result = db.update(FRIGODB_TABLE_NAME, values, FRIGODB_COLUMN_ID + " = ?",
                new String[]{String.valueOf(frigo.getIdFrigo())});
        //updating row
        DatabaseManager.getInstance().closeDatabase();

        return result;
    }

    /**
     * Fonction qui efface un produit
     * @param frigo
     */
    @Override
    public void deleteProduct(FrigoObject frigo) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.delete(FRIGODB_TABLE_NAME, FRIGODB_COLUMN_ID + " = ? ",
                new String[]{String.valueOf(frigo.getIdFrigo())});
        DatabaseManager.getInstance().closeDatabase();
    }

    /**
     * Fonction qui efface un produit avec son ID
     * @param id
     */
    @Override
    public void deleteProductWithId(Long id) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.delete(FRIGODB_TABLE_NAME, FRIGODB_COLUMN_ID + " = ? ",
                new String[]{String.valueOf(id)});
        DatabaseManager.getInstance().closeDatabase();
    }

    /**
     * Fonction qui renvoie les produits du frigo
     * @return
     */
    @Override
    public List<FrigoObject> getAllProduct() {
        List<FrigoObject> listproduct = new ArrayList<>();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = "SELECT " + FRIGODB_COLUMN_ID + ", " +  " CATEGORY_ID, " + FRIGODB_COLUMN_DATE_PEREMPT + " , " +  "NAME, IMAGE_URL, BRAND " + " , " +FRIGODB_COLUMN_QUANTITY + " FROM " + FRIGODB_TABLE_NAME + " JOIN PRODUCT ON ID_PRODUCT =  " + FRIGODB_COLUMN_ID_PRODUCT;
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                FrigoObject product = null;
                try {
                    product = new FrigoObject(Long.parseLong(cursor.getString(0)),Integer.parseInt(cursor.getString(1)),(Date) dateFormat.parse(cursor.getString(2)),cursor.getString(3) ,cursor.getString(4) , cursor.getString(5),Integer.parseInt(cursor.getString(6)) );
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                // Adding contact to list
                listproduct.add(product);
            } while (cursor.moveToNext());
        }
        return listproduct;
    }

    /**
     * Fonction qui renvoie les produits frigo selon un order by
     * @param order
     * @return
     */
    @Override
    public List<FrigoObject> getAllProductOrderBy(String order) {
        List<FrigoObject> listproduct = new ArrayList<>();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = "SELECT " + FRIGODB_COLUMN_ID + ", " +  " CATEGORY_ID, " + FRIGODB_COLUMN_DATE_PEREMPT + " , " +  "NAME, IMAGE_URL, BRAND " + " , " +FRIGODB_COLUMN_QUANTITY + " FROM " + FRIGODB_TABLE_NAME + " JOIN PRODUCT ON ID_PRODUCT =  " + FRIGODB_COLUMN_ID_PRODUCT
                + " ORDER BY " + order + ";";
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                FrigoObject product = null;
                try {
                    product = new FrigoObject(Long.parseLong(cursor.getString(0)),Integer.parseInt(cursor.getString(1)),(Date) dateFormat.parse(cursor.getString(2)),cursor.getString(3) ,cursor.getString(4) , cursor.getString(5),Integer.parseInt(cursor.getString(6)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                // Adding contact to list
                listproduct.add(product);
            } while (cursor.moveToNext());
        }
        return listproduct;
    }

    /**
     *
     * @return
     * @throws ParseException
     */
    @Override
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
