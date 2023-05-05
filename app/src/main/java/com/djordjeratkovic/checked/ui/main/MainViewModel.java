package com.djordjeratkovic.checked.ui.main;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.djordjeratkovic.checked.repository.CheckedRepository;

public class MainViewModel extends AndroidViewModel {

    private CheckedRepository repository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = CheckedRepository.getInstance();
        repository.setApplication(application);
    }

    public LiveData<Boolean> connectToDatabase(String databaseId) {
       return  repository.connectToADatabase(databaseId);
    }

    public LiveData<String> signInAsAGuest () {
        return repository.signInAsAGuest();
    }
}
