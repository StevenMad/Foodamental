package com.foodamental;

import java.util.Date;

/**
 * Created by YOUSSEF on 15/07/2016.
 */
public class ProductDTO {

    private Long id;
    private Integer category;
    private Date datePerempt;
    private String name;
    private String image;
    private String brand;

    public Long getId() {
        return id;
    }

    public Integer getCategory() {
        return category;
    }

    public Date getDatePerempt() {
        return datePerempt;
    }

    public String getName() {
        return name;
    }

    public ProductDTO(Long id, Integer category, Date datePerempt, String name, String image, String brand) {
        this.id = id;
        this.category = category;
        this.datePerempt = datePerempt;
        this.name = name;
        this.image = image;
        this.brand = brand;
    }

    public String getImage() {
        return image;
    }

    public String getBrand() {
        return brand;
    }



    public void setCategory(Integer category) {
        this.category = category;
    }

    public void setDatePerempt(Date datePerompt) {
        this.datePerempt = datePerempt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }


}
