package com.eatelligent.dao.model;

import java.util.Date;

/**
 * Created by Fangyi on 2016/6/16.
 */

/**
 * Classe pour table User
 */
public class FoodUser {
    private int id;
    private String username;
    private String password;
    private Date birthday;
    private String email;

    public FoodUser ()
    {
    }

    public FoodUser(int id, String username, String password, Date birthday, String email)
    {
        this.id = id;
        this.username = username;
        this.password = password;
        this.birthday = birthday;
        this.email = email;
    }

    public FoodUser(String username,Date birthday ) {
        this.username = username;
        this.birthday = birthday;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getEmail() {
        return email;
    }
}

