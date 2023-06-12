package com.djordjeratkovic.checked.ui.home.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.djordjeratkovic.checked.R;
import com.djordjeratkovic.checked.databinding.FragmentHomeBinding;
import com.djordjeratkovic.checked.model.Product;
import com.djordjeratkovic.checked.ui.home.ui.home.scan.ScanActivity;
import com.djordjeratkovic.checked.ui.home.ui.home.scan.dialog.ProductDialog;
import com.djordjeratkovic.checked.ui.main.MainActivity;
import com.djordjeratkovic.checked.util.Constants;
import com.djordjeratkovic.checked.util.ItemTouchHelperEdit;
import com.djordjeratkovic.checked.util.Sleeper;
import com.djordjeratkovic.checked.util.SwipeToDeleteAndEditCallback;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.UnaryOperator;

public class HomeFragment extends Fragment implements View.OnClickListener, ItemTouchHelperEdit, SearchView.OnQueryTextListener {

    //TODO: make a method that can be called from anywhere where you can add a product to the productsList and also delete, in other words replace

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private Activity activity;
    private Context context;

    private List<Product> productsList = new ArrayList<>();
    private HomeAdapter homeAdapter;

    private List<Product> searchList = new ArrayList<>();

    private SearchView searchView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        activity = getActivity();
        context = getContext();

        setListeners();
        setAdapter();
        setObservers();

        setupMenu();

        loading(true);

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
                ProductDialog productDialog = new ProductDialog(null, null, false, activity);
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
        homeViewModel.getProducts().observe(getViewLifecycleOwner(), products -> {
            Log.d("SOLI", "setObservers: " + products.size());
            if (!products.isEmpty()) {
                if (!productsList.isEmpty()) {
                    productsList.clear();
                }
                binding.empty.setVisibility(View.GONE);
                binding.lottieAnim.setVisibility(View.GONE);

                productsList.addAll(products);

                homeAdapter.saveFullProductsList();

                loading(false);
                refreshAdapter(null);
//                    homeAdapter.notifyDataSetChanged();
            } else {
                productsList.clear();
                new Sleeper(binding.empty, binding.loading, products, productsList, binding.lottieAnim).start();
//                    if (binding.productsRV.getAdapter() != null) {
//                        binding.productsRV.getAdapter().notifyDataSetChanged();
//                    }
                refreshAdapter(null);
            }
        });
        homeViewModel.getProducts().observe(getViewLifecycleOwner(), products -> {
            if (!products.isEmpty()) {
//                if (!productsList.isEmpty()) {
//                    productsList.clear();
//                }
                binding.empty.setVisibility(View.GONE);
                binding.lottieAnim.setVisibility(View.GONE);

                for (Product product : products) {
                    if (!productsList.contains(product)) {

                    }
                }


            } else {
                if (!productsList.isEmpty()) {
                    productsList.clear();
                }
                new Sleeper(binding.empty, binding.loading, products, productsList, binding.lottieAnim).start();
                refreshAdapter(null);
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

        homeAdapter = new HomeAdapter(getContext(), productsList, homeViewModel);
        binding.productsRV.setAdapter(homeAdapter);

        SwipeToDeleteAndEditCallback callback = new SwipeToDeleteAndEditCallback(getContext(), homeAdapter, this, productsList);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(binding.productsRV);
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
        super.onResume();
        refreshAdapter(null);
        changeVisibilityButtons(true);
    }

    @Override
    public void onItemEdit(int position) {
        refreshAdapter(position);
        ProductDialog productDialog = new ProductDialog(productsList.get(position), null, true, activity);
        productDialog.show(getParentFragmentManager(), Constants.PRODUCT_DIALOG_TAG);
    }

    private void loading(boolean b) {
        if (b) {
            binding.loading.setVisibility(View.VISIBLE);
        } else {
            binding.loading.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (query != null) {
            searchProducts(query);
        } else {
            homeAdapter.returnFullProductsList();
            refreshAdapter(null);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText != null) {
            searchProducts(newText);
        } else {
            homeAdapter.returnFullProductsList();
            refreshAdapter(null);
        }
        return true;
    }

    private void searchProducts(String typedIn) {
        searchList.clear();
        for (Product product : productsList) {
            if (product.getName().toLowerCase(Locale.ROOT).contains(typedIn.toLowerCase(Locale.ROOT)) ||
                    product.getBrand().toLowerCase(Locale.ROOT).contains(typedIn.toLowerCase(Locale.ROOT))) {
                searchList.add(product);
            } else if (product.getBarcode() != null && product.getBarcode().contains(typedIn)) {
                searchList.add(product);
            }
        }
        homeAdapter.setSearchList(searchList);
        refreshAdapter(null);
    }

    private void refreshAdapter(Integer position) {
        if (homeAdapter != null) {
            if (position == null) {
                homeAdapter.notifyDataSetChanged();
            } else {
                homeAdapter.notifyItemChanged(position);
            }
        }
    }

    private void setupMenu() {
        requireActivity().addMenuProvider(new MenuProvider() {

            @Override
            public void onPrepareMenu(@NonNull Menu menu) {
                MenuProvider.super.onPrepareMenu(menu);
            }

            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.home_menu, menu);
                MenuItem search = menu.findItem(R.id.search);
                searchView = (SearchView) search.getActionView();
                searchView.setSubmitButtonEnabled(true);
                searchView.setOnQueryTextListener(HomeFragment.this);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.logout:
                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context)
                                .setMessage(context.getResources().getString(R.string.are_you_sure_you_want_to_logout))
                                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        SharedPreferences sharedPreferences = activity.getSharedPreferences(Constants.KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.remove(Constants.KEY_DATABASE_ID);
                                        editor.apply();
                                        Intent intent = new Intent(activity, MainActivity.class);
                                        startActivity(intent);
                                        activity.finish();
                                    }
                                })
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                });
                        builder.show();
                        break;
                    case R.id.search:
                        break;
                    case R.id.share:
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, getShareText());
                        sendIntent.setType("text/plain");
                        Intent shareIntent = Intent.createChooser(sendIntent, null);
                        startActivity(shareIntent);
                        break;
                }
                return true;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private String getDatabaseId() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Constants.KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.KEY_DATABASE_ID, null);
    }

    private String getShareText() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getString(R.string.share_text));
        stringBuilder.append(": \n");
        stringBuilder.append(getDatabaseId());
        return stringBuilder.toString();
    }

    public void replaceProduct(Product oldProduct, Product newProduct) {
        productsList.set(productsList.indexOf(oldProduct), newProduct);
    }
}