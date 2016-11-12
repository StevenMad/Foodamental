package com.foodamental.util;

/**
 * Created by YOUSSEF on 18/07/2016.
 */
public class Tweet {
    public int getAvatar() {
        return avatar;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getText() {
        return text;
    }

    public void setAvatar(int color) {
        this.avatar = color;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setText(String text) {
        this.text = text;
    }

    private int avatar;
    private String pseudo;
    private String text;

    public int getCircle() {
        return circle;
    }

    public void setCircle(int circle) {
        this.circle = circle;
    }

    private int circle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Long id;

    public Tweet(int avatar, String pseudo, String text, Long id, int circle) {
        this.avatar = avatar;
        this.pseudo = pseudo;
        this.text = text;
        this.id = id;
        this.circle = circle;
    }


}