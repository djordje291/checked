package com.djordjeratkovic.checked.model;

import java.util.List;

public class Store {

    private int docRef;
    private String name;
    private String databaseId;

    public Store() {
    }

    public Store(String name) {
        this.name = name;
    }

    public int getDocRef() {
        return docRef;
    }

    public void setDocRef(int docRef) {
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
}
