package com.foodamental;


import java.util.Date;

/**
 * Created by YOUSSEF on 15/07/2016.
 */
public class FrigoObject {

    private Long id;
    private Long idProduct;
    private Integer category;
    private Date datePerompt;

    public FrigoObject(Long id, Long idProduct, Integer category, Date datePerompt) {
        this.id = id;
        this.idProduct = idProduct;
        this.category = category;
        this.datePerompt = datePerompt;
    }
    public FrigoObject(Long idProduct, Integer category, Date datePerompt) {
        this.idProduct = idProduct;
        this.category = category;
        this.datePerompt = datePerompt;
    }

    public FrigoObject() {
    }

    public Long getId() {
        return id;
    }

    public Long getIdProduct() {
        return idProduct;
    }

    public Integer getCategory() {
        return category;
    }

    public Date getDatePerompt() {
        return datePerompt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIdProduct(Long idProduct) {
        this.idProduct = idProduct;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public void setDatePerompt(Date datePerompt) {
        this.datePerompt = datePerompt;
    }
}
