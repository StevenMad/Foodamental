package com.foodamental.dao.interfaces;

/**
 * Created by YOUSSEF on 01/09/2016.
 */

import com.foodamental.dao.model.FoodUser;

import java.util.List;

/**
 * Interface pour la classe UserDB
 */
public interface IUserDB {

    public void addUser(FoodUser Fooduser);
    public FoodUser getFoodUser(int id);
    public List<FoodUser> getALLUser();
    public int getUserCount();
    public int updateUser(FoodUser user);
    public void deleteUser(FoodUser user);

}
