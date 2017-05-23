package com.eatelligent.dao.model;


import java.util.Date;

/**
 * Created by YOUSSEF on 15/07/2016.
 */

/**
 * Classe pour table Frigo
 */
public class FrigoObject extends ProductObject{

    private Long idFrigo;
    private Date datePerempt;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    private int quantity;

    public int getTypeOFBase() {
        return typeOFBase;
    }

    public void setTypeOFBase(int typeOFBase) {
        this.typeOFBase = typeOFBase;
    }

    private int typeOFBase;

    public FrigoObject(){

    }
    public FrigoObject(Long idFrigo,Long idProduct, Date datePerempt, int quantity) {
        this.idFrigo = idFrigo;
        this.setIdProduct(idProduct);
        this.datePerempt = datePerempt;
        this.quantity = quantity;
    }
    public FrigoObject(Long idFrigo, Integer category, Date datePerempt, String name, String image, String brand, int qunatity) {
        this.idFrigo = idFrigo;
        this.setCategory(category);
        this.datePerempt = datePerempt;
        this.setName(name);
        this.setImage(image);
        this.setBrand(brand);
        this.quantity = qunatity;
    }

    public FrigoObject(Long idFrigo, String Name,Date datePerempt,Integer category,int qunatity) {
        this.idFrigo = idFrigo;
        this.setName(Name);
        this.datePerempt = datePerempt;
        this.setCategory(category);
        this.quantity = qunatity;

    }
    public FrigoObject(String Name,Date datePerempt,Integer category,int qunatity) {
        this.setName(Name);
        this.datePerempt = datePerempt;
        this.setCategory(category);
        this.quantity = qunatity;

    }

    public FrigoObject(Long idProduct, String name,String brand, String image, Integer category, Date datePerempt, int qunatity) {
        this.setIdProduct(idProduct);
        this.setName(name);
        this.setImage(image);
        this.setBrand(brand);
        this.setCategory(category);
        this.datePerempt = datePerempt;
        this.quantity = qunatity;
    }

    public Long getIdFrigo() {
        return idFrigo;
    }

    public Date getDatePerempt() {
        return datePerempt;
    }

    public void setIdFrigo(Long id) {
        this.idFrigo = id;
    }

    public void setDatePerempt(Date datePerompt) {
        this.datePerempt = datePerompt;
    }
}
