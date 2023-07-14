package com.example.auctionfinal2.models;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class NewProductsModel implements Serializable {

    String description;
    String name;
    String img_url;
    String price;
    String sellType;
    String hours;
    String rateMove;
    String user;
    String userId;
    String ownerId;

    @Exclude
    private String id;

    public NewProductsModel() {
    }

    /*public NewProductsModel(String description, String name, String rating, String img_url, String price) {
        this.description = description;
        this.name = name;
        this.rating = rating;
        this.img_url = img_url;
        this.price = price;
    }*/

    public NewProductsModel(String description, String name, String img_url, String price, String sellType, String hours, String rateMove, String user, String userId, String ownerId) {
        this.description = description;
        this.name = name;
        this.img_url = img_url;
        this.price = price;
        this.sellType = sellType;
        this.hours = hours;
        this.rateMove = rateMove;
        this.user = user;
        this.userId = userId;
        this.ownerId = ownerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSellType() {
        return sellType;
    }

    public void setSellType(String sellType) {
        this.sellType = sellType;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getRateMove() {
        return rateMove;
    }

    public void setRateMove(String rateMove) {
        this.rateMove = rateMove;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
