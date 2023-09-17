package com.djordjeratkovic.checked.model;

public class StoreReceipt {

    private String storeDocRef;
    private int price;

    public StoreReceipt() {
    }

    public StoreReceipt(String storeDocRef, int price) {
        this.storeDocRef = storeDocRef;
        this.price = price;
    }

    public String getStoreDocRef() {
        return storeDocRef;
    }

    public void setStoreDocRef(String storeDocRef) {
        this.storeDocRef = storeDocRef;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
