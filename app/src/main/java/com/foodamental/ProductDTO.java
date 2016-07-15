package com.foodamental;

import java.util.Date;

/**
 * Created by YOUSSEF on 15/07/2016.
 */
public class ProductDTO {

    private Long id;
    private Integer category;
    private Date datePerompt;
    private String name;
    private String image;
    private String brand;

    public Long getId() {
        return id;
    }

    public Integer getCategory() {
        return category;
    }

    public Date getDatePerompt() {
        return datePerompt;
    }

    public String getName() {
        return name;
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

    public void setDatePerompt(Date datePerompt) {
        this.datePerompt = datePerompt;
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
