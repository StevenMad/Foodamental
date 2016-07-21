package com.foodamental;


import java.util.Date;

/**
 * Created by YOUSSEF on 15/07/2016.
 */
public class FrigoObject {

    private Long id;
    private Long idProduct;
    private Date datePerompt;

    public FrigoObject(Long id, Long idProduct, Date datePerompt) {
        this.id = id;
        this.idProduct = idProduct;
        this.datePerompt = datePerompt;
    }
    public FrigoObject(Long idProduct,Date datePerompt) {
        this.idProduct = idProduct;
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

    public Date getDatePerompt() {
        return datePerompt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIdProduct(Long idProduct) {
        this.idProduct = idProduct;
    }

    public void setDatePerompt(Date datePerompt) {
        this.datePerompt = datePerompt;
    }
}
