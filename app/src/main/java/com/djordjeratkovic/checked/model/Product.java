package com.djordjeratkovic.checked.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Product {
    //kategorije ce biti:
    //pica
    //mlecni proizvodi
    //smrznuti proizvodi
    //osnovne namirnice
    //sve za kucu
    //slatkisi i grickalice
    //meso i pakovano meso
    //voce i povrce

    @SerializedName("_id")
    private String barcode;

    @SerializedName("quantity")
    private String weight;

    @SerializedName("brands")
    private String brand;

    @SerializedName("product_name")
    private String name;

    @SerializedName("image_url")
    private String imageUrl;

    private int category;

    private List<ExpirationDate> expirationDates;

    private String docRef;

    private String databaseId;

    private boolean hasLow;

    public Product() {
    }

    public Product(String barcode, String weight, String brand, String name, String imageUrl, int category, List<ExpirationDate> expirationDates, String databaseId, boolean hasLow) {
        this.barcode = barcode;
        this.weight = weight;
        this.brand = brand;
        this.name = name;
        this.imageUrl = imageUrl;
        this.category = category;
        this.expirationDates = expirationDates;
        this.databaseId = databaseId;
        this.hasLow = hasLow;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<ExpirationDate> getExpirationDates() {
        return expirationDates;
    }

    public void setExpirationDates(List<ExpirationDate> expirationDates) {
        this.expirationDates = expirationDates;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getDocRef() {
        return docRef;
    }

    public void setDocRef(String docRef) {
        this.docRef = docRef;
    }

    public String getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(String databaseId) {
        this.databaseId = databaseId;
    }

    public boolean isHasLow() {
        return hasLow;
    }

    public void setHasLow(boolean hasLow) {
        this.hasLow = hasLow;
    }

    public int getQuantity() {
       int i = 0;
       for (ExpirationDate expirationDate : expirationDates) {
           i = i + expirationDate.getQuantity();
       }
       return i;
    }

    @Override
    public String toString() {
        return "Product{" +
                "barcode='" + barcode + '\'' +
                ", weight='" + weight + '\'' +
                ", brand='" + brand + '\'' +
                ", name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", category=" + category +
                ", expirationDates=" + expirationDates +
                ", docRef='" + docRef + '\'' +
                ", databaseId='" + databaseId + '\'' +
                ", hasLow=" + hasLow +
                '}';
    }
}
