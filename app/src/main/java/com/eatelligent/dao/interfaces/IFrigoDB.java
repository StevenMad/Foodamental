package com.eatelligent.dao.interfaces;

import com.eatelligent.dao.model.FrigoObject;

import java.text.ParseException;
import java.util.List;

/**
 * Created by YOUSSEF on 01/09/2016.
 */

/**
 * Interface frigo
 */
public interface IFrigoDB {
    public void addProduct(FrigoObject frigo);
    public FrigoObject getProduct(Integer id) throws ParseException;
    public List<FrigoObject> getALLProduct() throws ParseException;
    public int getProductCount();
    public int updateProduct(FrigoObject frigo);
    public void deleteProduct(FrigoObject frigo);
    public void deleteProductWithId(Long id);
    public List<FrigoObject> getAllProduct();
    public List<FrigoObject> getDistinctProductList() throws ParseException;
    public List<FrigoObject> getAllProductOrderBy(String order);
    public List<FrigoObject> getAllProduct(int date);
}
