package com.djordjeratkovic.checked.ui.home.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.djordjeratkovic.checked.R;
import com.djordjeratkovic.checked.databinding.FragmentHomeBinding;
import com.djordjeratkovic.checked.model.Product;
import com.djordjeratkovic.checked.ui.home.ui.home.scan.ScanActivity;
import com.djordjeratkovic.checked.ui.home.ui.home.scan.dialog.ProductDialog;
import com.djordjeratkovic.checked.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener {
    //TODO: dodaj na product barcode ako je tako skenirano i ako skeniras da doda na taj proizvod da ne pravi novi
    // ako nema barcode neka pretrazi po brandu i imenu pa ako se totalno podudara da opet updejtuje umesto aduje

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private Activity activity;
    private Context context;

    private List<Product> productsList = new ArrayList<>();
    private HomeAdapter homeAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        activity = getActivity();
        context = getContext();

        setListeners();
        setObservers();
        setAdapter();

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
            case R.id.fabExpand:
                changeVisibilityButtons(binding.fabManual.getVisibility() == View.VISIBLE);
                setAnimations(binding.fabManual.getVisibility() == View.VISIBLE);
                break;
            case R.id.fabManual:
                ProductDialog productDialog = new ProductDialog(null, null);
                //TODO: check this
                productDialog.show(getParentFragmentManager(), Constants.PRODUCT_DIALOG_TAG);
                break;
            case R.id.fabScan:
                Intent intent = new Intent(activity, ScanActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void setObservers() {
        homeViewModel.getProducts().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                if (!products.isEmpty()) {
                    if (!productsList.isEmpty()) {
                        productsList.clear();
                    }
                    productsList.addAll(products);
                    homeAdapter.notifyDataSetChanged();
                } else {
                    productsList.clear();
                    if (binding.productsRV.getAdapter() != null) {
                        binding.productsRV.getAdapter().notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void setListeners() {
        binding.fabExpand.setOnClickListener(this);
        binding.fabManual.setOnClickListener(this);
        binding.fabScan.setOnClickListener(this);
    }

    private void setAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        binding.productsRV.setLayoutManager(layoutManager);

        homeAdapter = new HomeAdapter(getContext(), productsList);
        binding.productsRV.setAdapter(homeAdapter);
    }

    private void changeVisibilityButtons(boolean b) {
        if (b) {
            binding.fabManual.setVisibility(View.GONE);
            binding.fabScan.setVisibility(View.GONE);
        } else {
            binding.fabManual.setVisibility(View.VISIBLE);
            binding.fabScan.setVisibility(View.VISIBLE);
        }
    }

    private void setAnimations(boolean b) {
        if (b) {
            binding.fabManual.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.from_right_anim));
            binding.fabScan.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.from_bottom_anim));
            binding.fabExpand.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.rotate_open_anim));
        } else {
            binding.fabExpand.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.rotate_close_anim));
            binding.fabManual.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.to_right_anim));
            binding.fabScan.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.to_bottom_anim));

        }
    }

    @Override
    public void onResume() {
        changeVisibilityButtons(true);
        super.onResume();
    }
}