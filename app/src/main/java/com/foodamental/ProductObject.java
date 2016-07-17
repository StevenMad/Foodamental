package com.foodamental;

import java.util.List;

/**
 * Created by YOUSSEF on 19/06/2016.
 */
public class ProductObject {



    private Long id;
    private String name;
    private String image;
    private String brand;

    public ProductObject() {

    }
    public ProductObject(String name,String brand, String image) {
        this.name = name;
        this.image = image;
        this.brand = brand;
    }

    public ProductObject(Long id, String name,String brand, String image ) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.brand = brand;
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

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
