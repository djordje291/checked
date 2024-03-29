package com.djordjeratkovic.checked.ui.home.ui.home;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;

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
import com.djordjeratkovic.checked.util.CommonUtils;
import com.djordjeratkovic.checked.util.ItemTouchHelperDelete;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> implements ItemTouchHelperDelete {

    //TODO: add lottie anims for no images and in dialog, and add 3sek delay for different cards
    //TODO: maybe load when your close to the position of the card so you dont load all at once, the images
    //TODO: make a list of open cards so if its updated all get open
    //TODO: create a button so you can add a product to the shopping list
    // napravi listu i kad se updejtuje da proveris koji je smao updejtovan i da ga dodas na glavnu listu i samo njemu da se refresuje position
    //TODO: set on hold constrainLayout add dialog for adding to shopping list

    private List<Product> products;
    private Context context;
    private ExpirationDateAdapter expirationDateAdapter;
    private HomeViewModel homeViewModel;

    private List<Product> save;

    Animation dropdown;
    Animation slideUpScale;

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

        dropdown = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.dropdown);
        slideUpScale = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_up_scale);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.bind(product);

        setAdapter(holder.binding, product, position);

        setBackgroundAndCategory(holder, position);
        CommonUtils.setProductTextColorAndSize(context,product.getExpirationDates(),holder.binding.name, 15);

        if (product.getImageUrl() != null) {
            if (CommonUtils.isItStorage(product.getImageUrl())) {
                Glide.with(context)
                        .load(homeViewModel.getStorageReference(product.getImageUrl()))
                        .addListener(CommonUtils.imageLoadingListener(holder.binding.lottieLoadingImage))
                        .into(holder.binding.image);
            } else {
                Glide.with(context).load(product.getImageUrl()).into(holder.binding.image);
            }

            holder.binding.lottieImage.setVisibility(View.GONE);
        } else {
            holder.binding.lottieImage.setVisibility(View.VISIBLE);
            holder.binding.lottieImage.pauseAnimation();
            holder.binding.lottieImage.setFrame((int) (Math.random() * 149));

        }

        holder.binding.cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchRV(holder.binding);
            }
        });
        holder.binding.fullLow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //TODO: nesto ovde nece, nece xml
                product.setHasLow(b);
                Log.d("DIOSMIHO", "onCheckedChanged: " + b + product.isHasLow());
                homeViewModel.updateProduct(product, null);
//                holder.binding.fullLow.setChecked(b);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    private void switchRV(CardProductBinding binding) {
        if (binding.expirationDatesRV.getVisibility() == View.VISIBLE) {
            binding.expirationDatesRV.startAnimation(slideUpScale);
            binding.expirationDatesRV.setVisibility(View.GONE);
            //TODO: maybe on end animation for expiration dates start animation for fullLow
            binding.fullLow.setVisibility(View.GONE);
        } else {
            binding.expirationDatesRV.startAnimation(dropdown);
            binding.expirationDatesRV.setVisibility(View.VISIBLE);
            //TODO: maybe on end animation for expiration dates start animation for fullLow
            binding.fullLow.setVisibility(View.VISIBLE);
        }
    }

    private void setBackgroundAndCategory(ViewHolder holder, int position) {
        BackgroundAndCategory backgroundAndCategory = new BackgroundAndCategory(position, products, context);
        holder.binding.cl.setBackground(backgroundAndCategory.getDrawable());
        if (backgroundAndCategory.isB()) {
            holder.binding.category.setVisibility(View.VISIBLE);
        } else {
            holder.binding.category.setVisibility(View.GONE);
        }

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
            Log.d("DIOSMIHO", "bind: " + product.isHasLow());
            binding.setCategory(new ProductCategory(product.getCategory(), context).getCategory());
            binding.executePendingBindings();
        }
    }
}
