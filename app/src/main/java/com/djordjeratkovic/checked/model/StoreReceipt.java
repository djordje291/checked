package com.djordjeratkovic.checked.model;

public class StoreReceipt {

    private int storeDocRef;
    private int price;
    private int quantity;

    public StoreReceipt() {
    }

    public StoreReceipt(int storeDocRef, int price, int quantity) {
        this.storeDocRef = storeDocRef;
        this.price = price;
        this.quantity = quantity;
    }

    public int getStoreDocRef() {
        return storeDocRef;
    }

    public void setStoreDocRef(int storeDocRef) {
        this.storeDocRef = storeDocRef;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
