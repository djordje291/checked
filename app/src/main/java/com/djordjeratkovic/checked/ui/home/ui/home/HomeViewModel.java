package com.djordjeratkovic.checked.ui.home.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.djordjeratkovic.checked.model.Product;
import com.djordjeratkovic.checked.repository.CheckedRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private CheckedRepository repository;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        repository = CheckedRepository.getInstance();
        repository.setApplication(application);
    }


    public LiveData<List<Product>> getProducts() {
        return repository.getProducts();
    }

    public void updateProduct(Product product) {
        repository.updateProduct(product);
    }

    public void deleteProduct(Product product) {
        repository.deleteProduct(product);
    }
}