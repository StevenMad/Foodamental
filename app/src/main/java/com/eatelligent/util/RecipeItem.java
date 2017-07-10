package com.eatelligent.util;

import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;

import org.json.JSONObject;

/**
 * Created by Madhow on 18/07/2016.
 */
public class RecipeItem {

    private Bitmap image;
    private JSONObject json;
    private Integer id;
    private String name;
    private Integer cookingTime;
    private Integer nbServe;

    public Bitmap getImage() {
        return image;
    }

    public JSONObject getJson() {
        return json;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getCookingTime()
    {
        return cookingTime;
    }

    public Integer getNbServe(){return nbServe;}

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setJson(JSONObject json) {
        this.json = json;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCookingTime(Integer cookingTime)
    {
        this.cookingTime=cookingTime;
    }

    public void setNbServe(Integer nbServe)
    {
        this.nbServe = nbServe;
    }
}
