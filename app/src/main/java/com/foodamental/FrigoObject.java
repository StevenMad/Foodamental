package com.foodamental;


import java.util.Date;

/**
 * Created by YOUSSEF on 15/07/2016.
 */
public class FrigoObject {

    private Integer id;
    private Integer idProduct;
    private Integer category;
    private Date datePerompt;

    public FrigoObject(Integer id, Integer idProduct, Integer category, Date datePerompt) {
        this.id = id;
        this.idProduct = idProduct;
        this.category = category;
        this.datePerompt = datePerompt;
    }

    public FrigoObject() {
    }

    public Integer getId() {
        return id;
    }

    public Integer getIdProduct() {
        return idProduct;
    }

    public Integer getCategory() {
        return category;
    }

    public Date getDatePerompt() {
        return datePerompt;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setIdProduct(Integer idProduct) {
        this.idProduct = idProduct;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public void setDatePerompt(Date datePerompt) {
        this.datePerompt = datePerompt;
    }
}
