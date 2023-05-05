package com.djordjeratkovic.checked.ui.home.ui.home.scan.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.bumptech.glide.Glide;
import com.djordjeratkovic.checked.R;
import com.djordjeratkovic.checked.databinding.DialogProductBinding;
import com.djordjeratkovic.checked.model.ExpirationDate;
import com.djordjeratkovic.checked.model.Product;
import com.djordjeratkovic.checked.util.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductDialog extends DialogFragment implements View.OnClickListener{
    // error namesti da iskoci kad ti treba za edit text

    private DialogProductBinding binding;

    private Product product;

    private ProductDialogViewModel productDialogViewModel;
    private CodeScanner codeScanner;

    private List<ExpirationDate> expirationDates = new ArrayList<>();

    private String lastDate;

    private ProductDialogAdapter adapter;

    private int category = -1;

    public ProductDialog(Product product, CodeScanner codeScanner) {
        this.product = product;
        this.codeScanner = codeScanner;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
        productDialogViewModel = new ViewModelProvider(this).get(ProductDialogViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DialogProductBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.toolbar.setNavigationOnClickListener(v -> {
            codeScannerStart();
            dismiss();
        });
        binding.toolbar.setTitle("New Product");
        binding.toolbar.inflateMenu(R.menu.dialog_item);
        binding.toolbar.setOnMenuItemClickListener(item -> {
            if (addProduct()) {
                codeScannerStart();
                dismiss();
            }
            return true;
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
        }
        if (product != null) {
            bind();
        }
        setExpirationDate();
        setListeners();

    }

    private void setListeners() {
        binding.addExpirationDateBtn.setOnClickListener(this);
        binding.categoryBtn1.setOnClickListener(this);
        binding.categoryBtn2.setOnClickListener(this);
        binding.categoryBtn3.setOnClickListener(this);
        binding.categoryBtn4.setOnClickListener(this);
        binding.categoryBtn5.setOnClickListener(this);
        binding.categoryBtn6.setOnClickListener(this);
        binding.categoryBtn7.setOnClickListener(this);
        binding.categoryBtn8.setOnClickListener(this);
    }


    private void bind() {
        binding.setProduct(product);
        if (product.getImageUrl() != null) {
            Glide.with(this).load(product.getImageUrl()).into(binding.image);
        }
    }

    private void setExpirationDate() {
        ExpirationDate expirationDate = new ExpirationDate(new Date(), 1);
        expirationDates.add(expirationDate);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.expirationDateRV.setLayoutManager(layoutManager);
        adapter = new ProductDialogAdapter(expirationDates, getContext());
        binding.expirationDateRV.setAdapter(adapter);
    }

    private String getDate() {
        Date today = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
        lastDate = simpleDateFormat.format(today);
        return lastDate;
    }

    private Date addDayToDate(Date date) {
        return new Date(date.getTime() + (1000 * 60 * 60 * 24));
    }

    private boolean addProduct() {
        if (binding.brandEditText.getText().toString().trim().isEmpty() ||
                binding.weightEditText.getText().toString().trim().isEmpty() ||
                binding.nameEditText.getText().toString().trim().isEmpty() ||
                getCategory() == -1) {
            Toast.makeText(getContext(), R.string.fill_in_all_the_fields, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (product != null) {
                product.setBrand(binding.brandEditText.getText().toString().trim());
                product.setWeight(binding.weightEditText.getText().toString().trim());
                product.setName(binding.nameEditText.getText().toString().trim());
                product.setExpirationDates(expirationDates);
                product.setCategory(getCategory());
            } else {
                product = new Product(null, binding.weightEditText.getText().toString().trim(),
                        binding.brandEditText.getText().toString().trim(),
                        binding.nameEditText.getText().toString().trim(),
                        null, getCategory(),
                        expirationDates, null);
            }
            productDialogViewModel.addProduct(product);
            return true;
        }
    }


    private void codeScannerStart() {
        if (codeScanner != null) {
            codeScanner.startPreview();
        }
    }

    private int getCategory() {
        return category;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.addExpirationDateBtn:
            ExpirationDate expirationDate = new ExpirationDate(addDayToDate(
                    expirationDates.get(expirationDates.size() - 1).getExpirationDate()),
                    1);
            expirationDates.add(expirationDate);
            adapter.notifyItemInserted(expirationDates.size() - 1);
            break;
            case R.id.categoryBtn1:
                category = 1;
                setRadioButtons(binding.categoryBtn1);
                break;
            case R.id.categoryBtn2:
                category = 2;
                setRadioButtons(binding.categoryBtn2);
                break;
            case R.id.categoryBtn3:
                category = 3;
                setRadioButtons(binding.categoryBtn3);
                break;
            case R.id.categoryBtn4:
                category = 4;
                setRadioButtons(binding.categoryBtn4);
                break;
            case R.id.categoryBtn5:
                category = 5;
                setRadioButtons(binding.categoryBtn5);
                break;
            case R.id.categoryBtn6:
                category = 6;
                setRadioButtons(binding.categoryBtn6);
                break;
            case R.id.categoryBtn7:
                category = 7;
                setRadioButtons(binding.categoryBtn7);
                break;
            case R.id.categoryBtn8:
                category = 8;
                setRadioButtons(binding.categoryBtn8);
                break;
        }
    }

    private void setRadioButtons(RadioButton radioButton) {
        radioButton.setChecked(true);
        checkRadioButtons(radioButton, binding.categoryBtn1);
        checkRadioButtons(radioButton, binding.categoryBtn2);
        checkRadioButtons(radioButton, binding.categoryBtn3);
        checkRadioButtons(radioButton, binding.categoryBtn4);
        checkRadioButtons(radioButton, binding.categoryBtn5);
        checkRadioButtons(radioButton, binding.categoryBtn6);
        checkRadioButtons(radioButton, binding.categoryBtn7);
        checkRadioButtons(radioButton, binding.categoryBtn8);
    }

    private void checkRadioButtons(RadioButton radioButton, RadioButton radioButton2) {
        if (radioButton != radioButton2) {
            radioButton2.setChecked(false);
        }
    }
}