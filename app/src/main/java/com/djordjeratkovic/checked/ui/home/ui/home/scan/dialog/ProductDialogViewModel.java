package com.djordjeratkovic.checked.ui.home.ui.home.scan.dialog;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.djordjeratkovic.checked.model.Product;
import com.djordjeratkovic.checked.repository.CheckedRepository;

public class ProductDialogViewModel extends AndroidViewModel {

    private CheckedRepository repository;

    public ProductDialogViewModel(@NonNull Application application) {
        super(application);
        repository = CheckedRepository.getInstance();
        repository.setApplication(application);
    }

    public void addProduct(Product product) {
        repository.addProduct(product);
    }

    public void updateProduct(Product product) {
        repository.updateProduct(product);
    }
}
