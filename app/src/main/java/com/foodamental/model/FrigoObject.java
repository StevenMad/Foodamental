package com.foodamental.model;


import java.util.Date;

/**
 * Created by YOUSSEF on 15/07/2016.
 */
public class FrigoObject extends ProductObject{

    private Long idFrigo;
    private Date datePerompt;

    public FrigoObject(Long idFrigo,Long idProduct, Date datePerompt) {
        this.idFrigo = idFrigo;
        this.setIdProduct(idProduct);
        this.datePerompt = datePerompt;
    }
    public FrigoObject(Long idFrigo, Integer category, Date datePerempt, String name, String image, String brand) {
        this.idFrigo = idFrigo;
        this.setCategory(category);
        this.datePerompt = datePerempt;
        this.setName(name);
        this.setImage(image);
        this.setBrand(brand);
    }

    public FrigoObject(Long idProduct, Date datePerompt) {
        this.setIdProduct(idProduct);
        this.datePerompt = datePerompt;
    }

    public Long getIdFrigo() {
        return idFrigo;
    }

    public Date getDatePerempt() {
        return datePerompt;
    }

    public void setIdFrigo(Long id) {
        this.idFrigo = id;
    }

    public void setDatePerempt(Date datePerompt) {
        this.datePerompt = datePerompt;
    }
}
