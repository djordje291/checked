package com.djordjeratkovic.checked.model;

import java.util.List;

public class ShoppingItem {

    private String name;
    private ProductCategory category;
    private List<StoreReceipt> storeReceipts;

    public ShoppingItem() {
    }

    public ShoppingItem(String name, ProductCategory category, List<StoreReceipt> storeReceipts) {
        this.name = name;
        this.category = category;
        this.storeReceipts = storeReceipts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public List<StoreReceipt> getStoreReceipts() {
        return storeReceipts;
    }

    public void setStoreReceipts(List<StoreReceipt> storeReceipts) {
        this.storeReceipts = storeReceipts;
    }
}
