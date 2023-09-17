package com.djordjeratkovic.checked.model;

import java.util.ArrayList;
import java.util.List;

public class Store {

    private String docRef;
    private String name;
    private String databaseId;
    private List<ShoppingItem> shoppingItemList;

    public Store() {
    }

    public Store(String name, List<ShoppingItem> shoppingItems) {
        this.name = name;
        this.shoppingItemList = shoppingItems;
    }

    public String getDocRef() {
        return docRef;
    }

    public void setDocRef(String docRef) {
        this.docRef = docRef;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(String databaseId) {
        this.databaseId = databaseId;
    }

    public List<ShoppingItem> getShoppingItemList() {
        return shoppingItemList;
    }

    public void setShoppingItemList(List<ShoppingItem> shoppingItemList) {
        this.shoppingItemList = shoppingItemList;
    }
}
