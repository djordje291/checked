package com.djordjeratkovic.checked.ui.home.ui.home;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.djordjeratkovic.checked.R;
import com.djordjeratkovic.checked.databinding.CardProductBinding;
import com.djordjeratkovic.checked.model.Product;
import com.djordjeratkovic.checked.model.ProductCategory;
import com.djordjeratkovic.checked.util.BackgroundAndCategory;
import com.djordjeratkovic.checked.util.ItemTouchHelperDelete;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> implements ItemTouchHelperDelete {

    //TODO: set full cl on click open, expiration dates to additional information and first card will be prices

    private List<Product> products;
    private Context context;
    private ExpirationDateAdapter expirationDateAdapter;
    private HomeViewModel homeViewModel;

    private List<Product> save;

    public HomeAdapter(Context context, List<Product> products, HomeViewModel homeViewModel) {
        this.context = context;
        this.products = products;
        this.homeViewModel = homeViewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardProductBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()
        ), R.layout.card_product, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.bind(product);

        setAdapter(holder.binding, product, position);

        setBackgroundAndCategory(holder, position);

        if (product.getImageUrl() != null) {
            Glide.with(context).load(product.getImageUrl()).into(holder.binding.image);
        }

        holder.binding.expirationDatesLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchRV(holder.binding);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    private void switchRV(CardProductBinding binding) {
        if (binding.expirationDatesRV.getVisibility() == View.VISIBLE) {
            binding.expirationDatesRV.setVisibility(View.GONE);
        } else {
            binding.expirationDatesRV.setVisibility(View.VISIBLE);
        }
    }

    private void setBackgroundAndCategory(ViewHolder holder, int position) {
        BackgroundAndCategory backgroundAndCategory =  new BackgroundAndCategory(position, products, context);

        holder.binding.cl.setBackground(backgroundAndCategory.getDrawable());
        if (position != 0) {
            if (products.get(position).getCategory() != products.get(position - 1).getCategory()) {
                holder.binding.category.setVisibility(View.VISIBLE);
            }
        } else {
            holder.binding.category.setVisibility(View.VISIBLE);
        }
//        if (backgroundAndCategory.isB()) {
//            holder.binding.category.setVisibility(View.VISIBLE);
//        }
    }

    private void setAdapter(CardProductBinding binding, Product product, int position) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        binding.expirationDatesRV.setLayoutManager(layoutManager);

        expirationDateAdapter = new ExpirationDateAdapter(product, homeViewModel, context, this, position);
        binding.expirationDatesRV.setAdapter(expirationDateAdapter);
    }

    public void setSearchList(List<Product> list) {
        products = list;
    }

    public void saveFullProductsList() {
        save = products;
    }

    public void returnFullProductsList() {
        products = save;
    }

    @Override
    public void onItemDelete(int position) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context)
                .setMessage(context.getResources().getString(R.string.are_you_sure_you_want_to_delete))
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        homeViewModel.deleteProduct(products.get(position));
                        notifyItemRemoved(position);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        notifyItemChanged(position);
                    }
                });
        builder.show();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardProductBinding binding;

        public ViewHolder(CardProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Product product) {
            binding.setProduct(product);
            binding.setCategory(new ProductCategory(product.getCategory(), context).getCategory());
            binding.executePendingBindings();
        }
    }
}
