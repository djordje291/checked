package com.djordjeratkovic.checked.model;

import com.google.gson.annotations.SerializedName;

public class BarcodeAPI {

//    @SerializedName("code")
//    private int code;

    @SerializedName("product")
    private Product product;

    public BarcodeAPI(Product innerProduct) {
        this.product = innerProduct;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "BarcodeAPI{" +
                "product=" + product +
                '}';
    }
}


