package com.foodamental;

import java.util.List;

/**
 * Created by YOUSSEF on 19/06/2016.
 */
public class ProductObject {

    private String name;
    private String image;
    private String brand;
    List<String> ingredients;

    public ProductObject(String name, String image, String brand, List<String> ingredients) {
        this.name = name;
        this.image = image;
        this.brand = brand;
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public List<String> getIngredients() {
        return ingredients;
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

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
