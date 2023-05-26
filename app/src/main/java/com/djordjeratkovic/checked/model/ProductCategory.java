package com.djordjeratkovic.checked.model;

import android.content.Context;

import com.djordjeratkovic.checked.R;

public class ProductCategory {

    private int category;
    private Context context;

    public ProductCategory(int category, Context context) {
        this.category = category;
        this.context = context;
    }

    public String getCategory() {
        String s = "";
        switch (category) {
            case 1:
                s = context.getString(R.string.drinks);
                break;
            case 2:
                s = context.getString(R.string.basic_foods);
                break;
            case 3:
                s = context.getString(R.string.dairy);
                break;
            case 4:
                s = context.getString(R.string.meat);
                break;
            case 5:
                s = context.getString(R.string.fruits_and_vegetables);
                break;
            case 6:
                s = context.getString(R.string.sweets_and_snacks);
                break;
            case 7:
                s = context.getString(R.string.frozen_foods);
                break;
            case 8:
                s = context.getString(R.string.household);
                break;
        }
        return s;
    }
}
