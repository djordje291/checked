package com.djordjeratkovic.checked.ui.home.ui.home.scan.dialog;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.djordjeratkovic.checked.model.Product;
import com.djordjeratkovic.checked.repository.CheckedRepository;
import com.google.firebase.storage.StorageReference;

public class ProductDialogViewModel extends AndroidViewModel {

    private CheckedRepository repository;

    public ProductDialogViewModel(@NonNull Application application) {
        super(application);
        repository = CheckedRepository.getInstance();
        repository.setApplication(application);
    }

    public void addProduct(Product product, Uri imageUri) {
        repository.checkIfThereIsAProduct(product, imageUri);
    }

    public void updateProduct(Product product, Uri imageUri) {
        repository.updateProduct(product, imageUri);
    }

    public StorageReference getStorageReference(String url) {
        return repository.getStorageReference(url);
    }
}
