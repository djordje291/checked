package com.djordjeratkovic.checked.ui.home.ui.shopping;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.djordjeratkovic.checked.R;
import com.djordjeratkovic.checked.databinding.FragmentShoppingBinding;
import com.djordjeratkovic.checked.model.ShoppingItem;
import com.djordjeratkovic.checked.model.Store;
import com.djordjeratkovic.checked.model.StoreReceipt;
import com.djordjeratkovic.checked.ui.home.ui.home.HomeAdapter;
import com.djordjeratkovic.checked.ui.home.ui.home.scan.dialog.ProductDialog;
import com.djordjeratkovic.checked.ui.home.ui.shopping.item.ItemAdapter;
import com.djordjeratkovic.checked.ui.home.ui.shopping.item.ItemDialog;
import com.djordjeratkovic.checked.ui.home.ui.shopping.store.StoreAdapter;
import com.djordjeratkovic.checked.ui.home.ui.shopping.store.StoreDialog;
import com.djordjeratkovic.checked.util.CommonUtils;
import com.djordjeratkovic.checked.util.Constants;
import com.djordjeratkovic.checked.util.SwipeToDeleteAndEditCallback;

import java.util.ArrayList;
import java.util.List;

public class ShoppingFragment extends Fragment implements View.OnClickListener {

    //TODO: create animation for fab going from corner to center in store

    private FragmentShoppingBinding binding;
    private ShoppingViewModel shoppingViewModel;
    private ItemAdapter itemAdapter;
    private StoreAdapter storeAdapter;

    private List<ShoppingItem> shoppingItemsList = new ArrayList<>();
    private List<Store> storesList = new ArrayList<>();

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
                switchRV(true);
                break;
            case R.id.storesBtn:
                switchRV(false);
                break;
            case R.id.fabAdd:
                if (isStores()) {
                    //stores dialog
                    StoreDialog storeDialog = new StoreDialog();
                    storeDialog.show(getParentFragmentManager(), Constants.PRODUCT_DIALOG_TAG);
                } else {
                    //item dialog
                    ItemDialog itemDialog = new ItemDialog();
                    //TODO: check this
                    itemDialog.show(getParentFragmentManager(), Constants.PRODUCT_DIALOG_TAG);
                }
                break;
        }
    }

    private void setObservers() {
        shoppingViewModel.getShoppingItems().observe(getViewLifecycleOwner(), new Observer<List<ShoppingItem>>() {
            @Override
            public void onChanged(List<ShoppingItem> shoppingItems) {
                if (!shoppingItems.isEmpty()) {
                    if (!shoppingItemsList.isEmpty()) {
                        shoppingItemsList.clear();
                    }

                    shoppingItemsList.addAll(shoppingItems);
                    addShoppingItemsToStores();

                    CommonUtils.refreshAdapter(itemAdapter, null);
                } else {
                    shoppingItemsList.clear();
                    CommonUtils.refreshAdapter(itemAdapter, null);
                }
            }
        });
        shoppingViewModel.getStores().observe(getViewLifecycleOwner(), new Observer<List<Store>>() {
            @Override
            public void onChanged(List<Store> stores) {
                if (!stores.isEmpty()) {
                    if (!storesList.isEmpty()) {
                        storesList.clear();
                    }

                    storesList.addAll(stores);

                    CommonUtils.refreshAdapter(storeAdapter, null);
                } else {
                    storesList.clear();
                    CommonUtils.refreshAdapter(storeAdapter, null);
                }
            }
        });
    }

    private void setAdapters() {
        LinearLayoutManager layoutManagerItem = new LinearLayoutManager(getContext());
        binding.itemsRV.setLayoutManager(layoutManagerItem);

        itemAdapter = new ItemAdapter(getContext(), shoppingItemsList, shoppingViewModel);
        binding.itemsRV.setAdapter(itemAdapter);

        LinearLayoutManager layoutManagerStore = new LinearLayoutManager(getContext());
        binding.storesRV.setLayoutManager(layoutManagerStore);

        storeAdapter = new StoreAdapter(getContext(), storesList, shoppingViewModel);
        binding.storesRV.setAdapter(storeAdapter);

//        SwipeToDeleteAndEditCallback callback = new SwipeToDeleteAndEditCallback(getContext(), homeAdapter, this, productsList);
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
//        itemTouchHelper.attachToRecyclerView(binding.productsRV);

    }

    private void setListeners() {
        binding.productsBtn.setOnClickListener(this);
        binding.storesBtn.setOnClickListener(this);
        binding.fabAdd.setOnClickListener(this);
    }

    private void setMenu() {


    }

    private boolean isStores() {
        return binding.toggleButton.getCheckedButtonId() == binding.storesBtn.getId();
    }

    private void switchRV(boolean b) {
        if (b) {
            binding.storesRV.setVisibility(View.GONE);
            binding.itemsRV.setVisibility(View.VISIBLE);
        } else {
            binding.storesRV.setVisibility(View.VISIBLE);
            binding.itemsRV.setVisibility(View.GONE);
        }
    }

    private void addShoppingItemsToStores() {
        for (Store store : storesList) {
            store.getShoppingItemList().clear();
        }
        for (ShoppingItem shoppingItem : shoppingItemsList) {
            if (shoppingItem.getStoreReceipts() != null) {
                for (StoreReceipt storeReceipt : shoppingItem.getStoreReceipts()) {
                    for (Store store : storesList) {
                        if (storeReceipt.getStoreDocRef().equals(store.getDocRef())) {
                            store.getShoppingItemList().add(shoppingItem);
                        }
                    }
                }
            }
        }
    }

    private void changeConstraintsFAB(boolean b) {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(binding.cl);
//        constraintSet.connect(R.id.myView, ConstraintSet.TOP, R.id.parentView, ConstraintSet.TOP, 16);
//        constraintSet.connect(R.id.myView, ConstraintSet.LEFT, R.id.parentView, ConstraintSet.LEFT, 32);
//        constraintSet.applyTo(constraintLayout);
        if (b) {
        }
    }
}