package com.djordjeratkovic.checked.ui.home.ui.home.scan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.djordjeratkovic.checked.model.Item;
import com.djordjeratkovic.checked.model.BarcodeAPI;
import com.djordjeratkovic.checked.model.Product;
import com.djordjeratkovic.checked.repository.CheckedRepository;

public class ScanViewModel extends ViewModel {

    private CheckedRepository repository;

    public ScanViewModel() {
        repository = CheckedRepository.getInstance();
    }

    public LiveData<Item> getBarcodeInfo(String barcode) {
        return repository.getBarcodeInfo(barcode);
    }
    public LiveData<BarcodeAPI> getProductFromBarcode() {
        return repository.getProductFromBarcode();
    }

    public void checkBarcode(String barcode) {
        repository.checkBarcode(barcode);
    }
}
