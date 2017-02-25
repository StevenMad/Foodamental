package com.foodamental.dao.dbimpl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ParseException;

import com.foodamental.dao.DatabaseManager;
import com.foodamental.dao.interfaces.ICategoryDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YOUSSEF on 12/11/2016.
 */

public class CategoryDB implements ICategoryDB {

    public static final String CATEGORYDB_TABLE_NAME = "CATEGORY";
    //CATEGORY table Columns names
    public static final String CATEGORYDB_COLUMN_ID = "ID_CATEGORY";
    public static final String CATEGORYDB_COLUMN_NAME = "NAME_CATEGORY";

    public CategoryDB() {
        super();
    }

    /**
     * Fonction qui ajoute une categorie dans bdd
     *
     * @param category
     */
    @Override
    public void addCategory(String category) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues valuesForCategory = new ContentValues();

        valuesForCategory.put(CategoryDB.CATEGORYDB_COLUMN_NAME, category); // category id


        db.insert(CategoryDB.CATEGORYDB_TABLE_NAME, null, valuesForCategory);


        DatabaseManager.getInstance().closeDatabase();

    }

    /**
     * Fonction qui renvoie une categorie par rapport à un ID
     *
     * @param id
     * @return
     * @throws ParseException
     */
    @Override
    public String getCategory(Integer id) throws ParseException {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        Cursor cursor = db.query(CATEGORYDB_TABLE_NAME, new String[]{CATEGORYDB_COLUMN_ID, CATEGORYDB_COLUMN_NAME
                }, CATEGORYDB_COLUMN_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        String result = cursor.getString(1);
        // Return category
        DatabaseManager.getInstance().closeDatabase();

        return result;
    }

    /**
     * Fonction qui envoie toutes les categories
     *
     * @return
     * @throws ParseException
     */
    @Override
    public List<String> getALLProduct() throws ParseException {
        List<String> categoryList = new ArrayList<String>();
        // Select all Query
        String selectQuery = "SELECT * FROM " + CATEGORYDB_TABLE_NAME;

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String category = cursor.getString(1);
                // Adding category to list
                categoryList.add(category);
            } while (cursor.moveToNext());
        }
        // return frigo products list
        DatabaseManager.getInstance().closeDatabase();
        return categoryList;
    }

    /**
     * Fonction qui renvoie le nombre de produits
     *
     * @return
     */
    @Override
    public int getProductCount() {
        String countQuery = "SELECT * FROM" + CATEGORYDB_TABLE_NAME;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        DatabaseManager.getInstance().closeDatabase();
        return cursor.getCount();
    }

    /**
     * Fonction qui met à jour une categorie
     *
     * @param category
     * @return
     */
    @Override
    public int updateCategory(int number, String category) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put(CATEGORYDB_COLUMN_ID, number); // frigo id
        values.put(CATEGORYDB_COLUMN_NAME, category); // product id in frigo
        int result = db.update(CATEGORYDB_TABLE_NAME, values, CATEGORYDB_COLUMN_ID + " = ?",
                new String[]{String.valueOf(number)});
        //updating row
        DatabaseManager.getInstance().closeDatabase();

        return result;
    }

    /**
     * Fonction qui efface une categorie
     *
     * @param number
     */
    @Override
    public void deleteCategoryWithId(int number) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.delete(CATEGORYDB_TABLE_NAME, CATEGORYDB_COLUMN_ID + " = ? ",
                new String[]{String.valueOf(number)});
        DatabaseManager.getInstance().closeDatabase();
    }


}
