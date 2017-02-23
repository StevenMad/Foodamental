package com.foodamental.dao.interfaces;

import com.foodamental.dao.model.FrigoObject;

import java.text.ParseException;
import java.util.List;

/**
 * Created by YOUSSEF on 01/12/2016.
 */

public interface IOtherFrigoProductDB {

    public void addOtherProduct(FrigoObject frigo);
    public FrigoObject getOtherProduct(Integer id) throws ParseException;
    public List<FrigoObject> getALLOtherProduct() throws ParseException;
    public int getOtherProductCount();
    public int updateOtherProduct(FrigoObject frigo);
    public void deleteOtherProduct(FrigoObject frigo);
    public void deleteOtherProductWithId(Long id);
    public List<FrigoObject> getAllOtherProductOrderBy(String order);
}
