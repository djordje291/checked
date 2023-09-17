package com.djordjeratkovic.checked.ui.home.ui.shopping.store;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.djordjeratkovic.checked.R;
import com.djordjeratkovic.checked.databinding.CardStoreBinding;
import com.djordjeratkovic.checked.model.ShoppingItem;
import com.djordjeratkovic.checked.model.Store;
import com.djordjeratkovic.checked.model.StoreReceipt;
import com.djordjeratkovic.checked.ui.home.ui.shopping.ShoppingViewModel;

import org.checkerframework.checker.units.qual.C;

import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {

    private Context context;
    private List<Store> stores;
    private ShoppingViewModel shoppingViewModel;

    public StoreAdapter(Context context, List<Store> stores, ShoppingViewModel shoppingViewModel) {
        this.context = context;
        this.stores = stores;
        this.shoppingViewModel = shoppingViewModel;
    }

    @NonNull
    @Override
    public StoreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardStoreBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.card_store, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreAdapter.ViewHolder holder, int position) {
        Store store = stores.get(position);
        holder.bind(store);
    }

    @Override
    public int getItemCount() {
        return stores.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardStoreBinding binding;

        public ViewHolder(CardStoreBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Store store) {
            binding.setStore(store);
            binding.setSum(getSum(store));
            binding.executePendingBindings();
        }


        private int getSum(Store store) {
            int sum = 0;
            if (store.getShoppingItemList() != null) {
                for (ShoppingItem shoppingItem : store.getShoppingItemList()) {
                    for (StoreReceipt storeReceipt : shoppingItem.getStoreReceipts()) {
                        if (storeReceipt.getStoreDocRef().equals(store.getDocRef())) {
                            sum += storeReceipt.getPrice();
                        }
                    }
                }
            }
            return sum;
        }
    }
}
