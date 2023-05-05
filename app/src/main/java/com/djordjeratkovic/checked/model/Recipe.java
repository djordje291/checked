package com.djordjeratkovic.checked.model;

import java.util.List;

public class Recipe {
    private String description;
    private String name;
    private List<Item> ingredients;

    public Recipe() {

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

    public List<Item> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Item> ingredients) {
        this.ingredients = ingredients;
    }
}
