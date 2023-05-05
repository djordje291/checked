package com.djordjeratkovic.checked.ui.home.ui.home;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.djordjeratkovic.checked.R;
import com.djordjeratkovic.checked.databinding.CardProductBinding;
import com.djordjeratkovic.checked.model.Product;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private List<Product> products;
    private Context context;

    public HomeAdapter(Context context,List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardProductBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()
        ), R.layout.card_product, parent, false);
        return new HomeAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.bind(product);
        setBackgroundAndCategory(holder, position);
        if (product.getImageUrl() != null) {
            Glide.with(context).load(product.getImageUrl()).into(holder.binding.image);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    private void setBackgroundAndCategory(ViewHolder holder, int position) {
        Drawable drawable;
        boolean b = false;

        //TODO: make it also for only drawable
        if (position == 0) {
            drawable = context.getDrawable(R.drawable.product_card_first_background);
            b = true;
        } else if (position == products.size() - 1) {
            drawable = context.getDrawable(R.drawable.product_card_last_background);
        } else {
           if (products.get(position).getCategory() == products.get(position - 1).getCategory()) {
               if (products.get(position).getCategory() == products.get(position + 1).getCategory()) {
                   drawable = context.getDrawable(R.drawable.product_card_middle_background);
               } else {
                   drawable = context.getDrawable(R.drawable.product_card_last_background);
               }
           } else {
               drawable = context.getDrawable(R.drawable.product_card_first_background);
               b = true;
           }
        }

        holder.binding.cl.setBackground(drawable);
        if (b) {
            holder.binding.category.setVisibility(View.VISIBLE);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardProductBinding binding;

        public ViewHolder(CardProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Product product) {
            binding.setProduct(product);

        }
    }
}
