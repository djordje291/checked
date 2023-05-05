package com.djordjeratkovic.checked.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.djordjeratkovic.checked.R;
import com.djordjeratkovic.checked.databinding.ActivityMainBinding;
import com.djordjeratkovic.checked.ui.home.HomeActivity;
import com.djordjeratkovic.checked.util.Constants;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding binding;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        checkIfLoggedIn();
        setListeners();
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.createNew) {
            viewModel.signInAsAGuest().observe(this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    if (s != null) {
                        saveID(s);
                        changeIntent();
                    }
                }
            });
        } else if (view.getId() == R.id.connect) {
            if (!binding.inviteCode.getText().toString().trim().isEmpty()) {
                String databaseId = binding.inviteCode.getText().toString().trim();
                viewModel.connectToDatabase(databaseId).observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if (aBoolean) {
                            binding.inviteCodeLayout.setError(null);
                            saveID(databaseId);
                            changeIntent();
                        } else {
                            binding.inviteCodeLayout.setError(getString(R.string.invalid_invite_code));
                        }
                    }
                });
            } else {
                binding.inviteCodeLayout.setError(getString(R.string.no_invite_code));
            }
        }
    }

    private void checkIfLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.KEY_SHARED_PREFERENCES, MODE_PRIVATE);
        if  (sharedPreferences.getString(Constants.KEY_DATABASE_ID, null) != null) {
            changeIntent();
        }
    }

    private void saveID(String s) {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.KEY_SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.KEY_DATABASE_ID, s);
        editor.apply();
    }

    private void setListeners() {
        binding.createNew.setOnClickListener(this);
        binding.connect.setOnClickListener(this);
    }

    private void changeIntent() {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}