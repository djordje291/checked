package com.djordjeratkovic.checked.ui.home.ui.shopping.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.djordjeratkovic.checked.R;
import com.djordjeratkovic.checked.databinding.CardShoppingItemBinding;
import com.djordjeratkovic.checked.model.ProductCategory;
import com.djordjeratkovic.checked.model.ShoppingItem;
import com.djordjeratkovic.checked.model.Store;
import com.djordjeratkovic.checked.ui.home.ui.shopping.ShoppingViewModel;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    //TODO: add qunatity on end and + make in the recycler view

    private Context context;
    private List<ShoppingItem> shoppingItems;
    private ShoppingViewModel shoppingViewModel;

    public ItemAdapter(Context context, List<ShoppingItem> shoppingItems, ShoppingViewModel shoppingViewModel) {
        this.context = context;
        this.shoppingItems = shoppingItems;
        this.shoppingViewModel = shoppingViewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardShoppingItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.card_shopping_item, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShoppingItem shoppingItem = shoppingItems.get(position);
        holder.bind(shoppingItem);
    }

    @Override
    public int getItemCount() {
        return shoppingItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardShoppingItemBinding binding;

        public ViewHolder(CardShoppingItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ShoppingItem shoppingItem) {
            binding.setShoppingItem(shoppingItem);
            binding.setCategory(new ProductCategory(shoppingItem.getCategory(), context).getCategory());
            binding.executePendingBindings();
        }
    }
}
