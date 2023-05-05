package com.djordjeratkovic.checked.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

public class Item {

    @SerializedName("company")
    private String company;

    @SerializedName("description")
    private String desctription;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("size")
    private String size;

    private int state;
    private int price;
    private HashMap<Store, Integer> stores;

    public Item(String company, String desctription, String imageUrl, String size, int state, int price, HashMap<Store, Integer> stores) {
        this.company = company;
        this.desctription = desctription;
        this.imageUrl = imageUrl;
        this.size = size;
        this.state = state;
        this.price = price;
        this.stores = stores;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDesctription() {
        return desctription;
    }

    public void setDesctription(String desctription) {
        this.desctription = desctription;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public HashMap<Store, Integer> getStores() {
        return stores;
    }

    public void setStores(HashMap<Store, Integer> stores) {
        this.stores = stores;
    }
}
