package com.foodamental;

/**
 * Created by YOUSSEF on 16/07/2016.
 */
public class Tweet {
    public int getColor() {
        return color;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getText() {
        return text;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setText(String text) {
        this.text = text;
    }

    private int color;
    private String pseudo;
    private String text;

    public Tweet(int color, String pseudo, String text) {
        this.color = color;
        this.pseudo = pseudo;
        this.text = text;
    }


}
