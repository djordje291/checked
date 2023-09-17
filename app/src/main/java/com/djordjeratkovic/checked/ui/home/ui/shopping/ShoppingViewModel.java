package com.djordjeratkovic.checked.ui.home.ui.shopping;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.djordjeratkovic.checked.model.ShoppingItem;
import com.djordjeratkovic.checked.model.Store;
import com.djordjeratkovic.checked.repository.CheckedRepository;

import java.util.List;

public class ShoppingViewModel extends AndroidViewModel {

    private CheckedRepository repository;

    public ShoppingViewModel(@NonNull Application application) {
        super(application);
        repository = CheckedRepository.getInstance();
        repository.setApplication(application);
    }

    public LiveData<List<ShoppingItem>> getShoppingItems() {
        return repository.getShoppingItems();
    }


    public LiveData<List<Store>> getStores() {
        return repository.getStores();
    }

}