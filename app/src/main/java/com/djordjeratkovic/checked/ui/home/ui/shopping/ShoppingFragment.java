package com.djordjeratkovic.checked.ui.home.ui.shopping;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.djordjeratkovic.checked.R;
import com.djordjeratkovic.checked.databinding.FragmentShoppingBinding;

public class ShoppingFragment extends Fragment implements View.OnClickListener {

    private FragmentShoppingBinding binding;
    private ShoppingViewModel shoppingViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        shoppingViewModel = new ViewModelProvider(this).get(ShoppingViewModel.class);

        binding = FragmentShoppingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setListeners();
        setAdapters();
        setObservers();

        setMenu();


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.productsBtn:
                break;
            case R.id.storesBtn:
                break;
            case R.id.fabAdd:
                if (isStores()) {
                    //stores dialog
                } else {
                    //item dialog
                }
                break;
        }
    }

    private void setObservers() {

    }

    private void setAdapters() {

    }

    private void setListeners() {
        binding.productsBtn.setOnClickListener(this);
        binding.storesBtn.setOnClickListener(this);
        binding.fabAdd.setOnClickListener(this);
    }

    private void setMenu() {


    }

    private boolean isStores() {
        return binding.storesBtn.isActivated();
    }
}