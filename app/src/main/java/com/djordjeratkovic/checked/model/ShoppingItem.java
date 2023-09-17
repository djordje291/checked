package com.djordjeratkovic.checked.model;

import java.util.List;

public class ShoppingItem {

    private String name;
    private int category;
    private List<StoreReceipt> storeReceipts;
    private String databaseId;
    private String docRef;
    private int quantity;

    public ShoppingItem() {
    }

    public ShoppingItem(String name, int category, List<StoreReceipt> storeReceipts, String databaseId, int quantity) {
        this.name = name;
        this.category = category;
        this.storeReceipts = storeReceipts;
        this.databaseId = databaseId;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public List<StoreReceipt> getStoreReceipts() {
        return storeReceipts;
    }

    public void setStoreReceipts(List<StoreReceipt> storeReceipts) {
        this.storeReceipts = storeReceipts;
    }

    public String getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(String databaseId) {
        this.databaseId = databaseId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDocRef() {
        return docRef;
    }

    public void setDocRef(String docRef) {
        this.docRef = docRef;
    }
}
