package com.foodamental.dao.interfaces;

import android.net.ParseException;

import java.util.List;

/**
 * Created by YOUSSEF on 12/11/2016.
 */

public interface ICategoryDB {
    public void addCategory(String category);
    public String getCategory(Integer id) throws ParseException;
    public List<String> getALLCategory() throws ParseException;
    public int getProductCount();
    public int updateCategory(int number, String category);
    public void deleteCategoryWithId(int number);
}
