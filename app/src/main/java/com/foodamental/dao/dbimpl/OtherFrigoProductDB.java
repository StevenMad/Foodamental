package com.foodamental.dao.dbimpl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.foodamental.dao.DatabaseManager;
import com.foodamental.dao.interfaces.IOtherFrigoProductDB;
import com.foodamental.dao.model.FrigoObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by YOUSSEF on 01/12/2016.
 */

/**
 * Table pour autres produits
 */
public class OtherFrigoProductDB implements IOtherFrigoProductDB {

    public static final String OTHERFRIGOPRODUCTDB_TABLE_NAME = "OTHERPRODUCT";
    //other product table Columns names
    public static final String OTHERFRIGOPRODUCTDB_COLUMN_ID = "ID_OTHER_PRODUCT";
    public static final String OTHERFRIGOPRODUCTDB_COLUMN_NAME = "NAME";
    public static final String OTHERFRIGOPRODUCTDB_COLUMN_DATE_PEREMPT = "EXPIRY_DATE";
    public static final String OTHERFRIGOPRODUCTDB_COLUMN_CATEGORY = "CATEGORY_ID";
    public static final String OTHERFRIGOPRODUCTDB_COLUMN_QUANTITY = "QUANTITY";

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    public OtherFrigoProductDB() {
        super();
    }

    /**
     * Fonction qui ajoute un autre produit dans bdd
     *
     * @param frigo
     */
    @Override
    public void addOtherProduct(FrigoObject frigo) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues valuesForOtherProduct = new ContentValues();

        valuesForOtherProduct.put(OtherFrigoProductDB.OTHERFRIGOPRODUCTDB_COLUMN_ID, frigo.getIdFrigo()); // product id
        valuesForOtherProduct.put(OtherFrigoProductDB.OTHERFRIGOPRODUCTDB_COLUMN_NAME, frigo.getName()); // product name
        valuesForOtherProduct.put(OtherFrigoProductDB.OTHERFRIGOPRODUCTDB_COLUMN_DATE_PEREMPT, dateFormat.format(frigo.getDatePerempt())); // product brand
        valuesForOtherProduct.put(OtherFrigoProductDB.OTHERFRIGOPRODUCTDB_COLUMN_CATEGORY, frigo.getCategory()); // product image
        valuesForOtherProduct.put(OtherFrigoProductDB.OTHERFRIGOPRODUCTDB_COLUMN_QUANTITY, frigo.getQuantity()); // product category

        db.insert(OTHERFRIGOPRODUCTDB_TABLE_NAME, null, valuesForOtherProduct);


        DatabaseManager.getInstance().closeDatabase();
    }

    /**
     * Fonction qui renvoie un autre produit par rapport à un ID
     *
     * @param id
     * @return
     * @throws ParseException
     */
    @Override
    public FrigoObject getOtherProduct(Integer id) throws ParseException {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        Cursor cursor = db.query(OTHERFRIGOPRODUCTDB_TABLE_NAME, new String[]{OTHERFRIGOPRODUCTDB_COLUMN_ID, OTHERFRIGOPRODUCTDB_COLUMN_NAME
                        , OTHERFRIGOPRODUCTDB_COLUMN_DATE_PEREMPT, OTHERFRIGOPRODUCTDB_COLUMN_CATEGORY, OTHERFRIGOPRODUCTDB_COLUMN_QUANTITY}, OTHERFRIGOPRODUCTDB_COLUMN_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        FrigoObject product = new FrigoObject(Long.parseLong(cursor.getString(0)), cursor.getString(1), ((Date) dateFormat.parse(cursor.getString(2))), Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)));
        product.setTypeOFBase(1);
        // Return frigo object
        DatabaseManager.getInstance().closeDatabase();

        return product;
    }

    /**
     * Fonction qui envoie tous les autres produits
     *
     * @return
     * @throws ParseException
     */
    @Override
    public List<FrigoObject> getALLOtherProduct() throws ParseException {
        List<FrigoObject> frigoList = new ArrayList<FrigoObject>();
        // Select all Query
        String selectQuery = "SELECT * FROM " + OTHERFRIGOPRODUCTDB_TABLE_NAME;

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                FrigoObject product = new FrigoObject(Long.parseLong(cursor.getString(0)), cursor.getString(1), ((Date) dateFormat.parse(cursor.getString(2))), Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)));
                product.setTypeOFBase(1);
                // Adding product to list
                frigoList.add(product);
            } while (cursor.moveToNext());
        }
        // return frigo other products list
        DatabaseManager.getInstance().closeDatabase();
        return frigoList;
    }

    @Override
    public int getOtherProductCount() {
        String countQuery = "SELECT * FROM" + OTHERFRIGOPRODUCTDB_TABLE_NAME;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        DatabaseManager.getInstance().closeDatabase();
        return cursor.getCount();
    }

    /**
     * Fonction qui met à jour un autre produit
     *
     * @param frigo
     * @return
     */
    @Override
    public int updateOtherProduct(FrigoObject frigo) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put(OtherFrigoProductDB.OTHERFRIGOPRODUCTDB_COLUMN_ID, frigo.getIdFrigo()); // product id
        values.put(OtherFrigoProductDB.OTHERFRIGOPRODUCTDB_COLUMN_NAME, frigo.getName()); // product name
        values.put(OtherFrigoProductDB.OTHERFRIGOPRODUCTDB_COLUMN_DATE_PEREMPT, dateFormat.format(frigo.getDatePerempt())); // product brand
        values.put(OtherFrigoProductDB.OTHERFRIGOPRODUCTDB_COLUMN_CATEGORY, frigo.getCategory()); // product image
        values.put(OtherFrigoProductDB.OTHERFRIGOPRODUCTDB_COLUMN_QUANTITY, frigo.getQuantity()); // product category

        int result = db.update(OTHERFRIGOPRODUCTDB_TABLE_NAME, values, OTHERFRIGOPRODUCTDB_COLUMN_ID + " = ?",
                new String[]{String.valueOf(frigo.getIdFrigo())});
        //updating row
        DatabaseManager.getInstance().closeDatabase();

        return result;
    }

    /**
     * Fonction qui efface un autre produit
     *
     * @param frigo
     */
    @Override
    public void deleteOtherProduct(FrigoObject frigo) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.delete(OTHERFRIGOPRODUCTDB_TABLE_NAME, OTHERFRIGOPRODUCTDB_COLUMN_ID + " = ? ",
                new String[]{String.valueOf(frigo.getIdFrigo())});
        DatabaseManager.getInstance().closeDatabase();
    }

    /**
     * Fonction qui efface un produit avec son ID
     *
     * @param id
     */
    @Override
    public void deleteOtherProductWithId(Long id) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.delete(OTHERFRIGOPRODUCTDB_TABLE_NAME, OTHERFRIGOPRODUCTDB_COLUMN_ID + " = ? ",
                new String[]{String.valueOf(id)});
        DatabaseManager.getInstance().closeDatabase();
    }


    @Override
    public List<FrigoObject> getAllOtherProductOrderBy(String order) {
        List<FrigoObject> listproduct = new ArrayList<>();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = "SELECT " + "*" + " FROM " + OTHERFRIGOPRODUCTDB_TABLE_NAME
                + " ORDER BY " + order + ";";
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                FrigoObject product = null;
                try {
                    product = new FrigoObject(Long.parseLong(cursor.getString(0)), cursor.getString(1), ((Date) dateFormat.parse(cursor.getString(2))), Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)));
                    product.setTypeOFBase(1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                // Adding contact to list
                listproduct.add(product);
            } while (cursor.moveToNext());
        }
        return listproduct;
    }
}
