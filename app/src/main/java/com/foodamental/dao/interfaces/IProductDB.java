package com.foodamental.dao.interfaces;

import com.foodamental.dao.model.ProductObject;

import java.util.List;

/**
 * Created by YOUSSEF on 01/09/2016.
 */

/**
 * Interface Product
 */
public interface IProductDB {
    public void addProduct(ProductObject product);
    public ProductObject getProduct(Integer id);
    public List<ProductObject> getALLProduct();
    public int getProductCount();
    public int updateProduct(ProductObject product);
    public void deleteProduct(ProductObject product);
}
