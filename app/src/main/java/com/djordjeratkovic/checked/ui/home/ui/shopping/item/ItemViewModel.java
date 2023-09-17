package com.djordjeratkovic.checked.ui.home.ui.shopping.item;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.djordjeratkovic.checked.model.ShoppingItem;
import com.djordjeratkovic.checked.repository.CheckedRepository;

import java.util.List;

public class ItemViewModel extends AndroidViewModel {

    private CheckedRepository repository;

    public ItemViewModel(@NonNull Application application) {
        super(application);
        repository = CheckedRepository.getInstance();
        repository.setApplication(application);
    }

    public LiveData<List<ShoppingItem>> getShoppingItems() {
        return repository.getShoppingItems();
    }

    public void updateShoppingItem(ShoppingItem shoppingItem) {
        repository.updateShoppingItem(shoppingItem);
    }

    public void deleteShoppingItem(ShoppingItem shoppingItem) {
        repository.deleteShoppingItem(shoppingItem);
    }

    public void addShoppingItem(ShoppingItem shoppingItem) {
        repository.addShoppingItem(shoppingItem);
    }
}
