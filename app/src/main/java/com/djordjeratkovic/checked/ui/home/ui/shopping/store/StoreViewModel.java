package com.djordjeratkovic.checked.ui.home.ui.shopping.store;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.djordjeratkovic.checked.model.ShoppingItem;
import com.djordjeratkovic.checked.model.Store;
import com.djordjeratkovic.checked.repository.CheckedRepository;

import java.util.List;

public class StoreViewModel extends AndroidViewModel {

    private CheckedRepository repository;

    public StoreViewModel(@NonNull Application application) {
        super(application);
        repository = CheckedRepository.getInstance();
        repository.setApplication(application);
    }

    public LiveData<List<Store>> getStores() {
        return repository.getStores();
    }

    public void updateStore(Store store) {
        repository.updateStore(store);
    }

    public void deleteStore(Store store) {
        repository.deleteStore(store);
    }

    public void addStore(Store store) {
        repository.addStore(store);
    }
}
