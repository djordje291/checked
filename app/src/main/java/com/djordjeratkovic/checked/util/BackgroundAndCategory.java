package com.djordjeratkovic.checked.util;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.appcompat.content.res.AppCompatResources;

import com.djordjeratkovic.checked.R;
import com.djordjeratkovic.checked.model.Product;

import java.util.ArrayList;
import java.util.List;

public class BackgroundAndCategory {
    //TODO: ili sve ovde se namesti ili da uzmes da vratis samo 1, 2, 3 ili 4 pa po tome setujes drawable

    private Drawable drawable;
    private Drawable first;
    private Drawable middle;
    private Drawable last;
    private Drawable only;
    private boolean b;
    private int position;
    private List<Product> products = new ArrayList<>();
    private Context context;

    private int i;

    public BackgroundAndCategory(int position, List<Product> products, Context context) {
        this.position = position;
        this.products = products;
        this.context = context;
        b = false;
        set();
    }

    private void set() {
        if (position != 0 && products.size() > 1 && position != products.size() - 1) {
            if (products.get(position).getCategory() == products.get(position - 1).getCategory() &&
                    products.get(position).getCategory() == products.get(position + 1).getCategory()) {
                drawable = AppCompatResources.getDrawable(context, R.drawable.product_card_middle_background);
                i = 2;
            } else if (products.get(position).getCategory() == products.get(position - 1).getCategory()) {
                drawable = AppCompatResources.getDrawable(context, R.drawable.product_card_last_background);
                i = 3;
            } else if (products.get(position).getCategory() == products.get(position + 1).getCategory()) {
                drawable = AppCompatResources.getDrawable(context, R.drawable.product_card_first_background);
                i = 1;
                b = true;
            } else {
                drawable = AppCompatResources.getDrawable(context, R.drawable.product_card_only_background);
                i = 4;
                b = true;
            }
        } else if (position != 0 && position == products.size() - 1) {
            if (products.get(position).getCategory() == products.get(position - 1).getCategory()) {
                drawable = AppCompatResources.getDrawable(context, R.drawable.product_card_last_background);
                i = 3;
            } else {
                drawable = AppCompatResources.getDrawable(context, R.drawable.product_card_only_background);
                i = 4;
                b = true;
            }
        } else if (products.size() <= 1){
            drawable = AppCompatResources.getDrawable(context, R.drawable.product_card_only_background);
            i = 4;
            b = true;
        } else {
            b = true;
            if (products.get(position).getCategory() == products.get(position + 1).getCategory()) {
                drawable = AppCompatResources.getDrawable(context, R.drawable.product_card_first_background);
                i = 1;
            } else {
                drawable = AppCompatResources.getDrawable(context, R.drawable.product_card_only_background);
                i = 4;
            }
        }
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public int getPosition() {
        return i;
    }

    public boolean isB() {
        return b;
    }
}
