package com.djordjeratkovic.checked.ui.home.ui.shopping.item;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.djordjeratkovic.checked.R;
import com.djordjeratkovic.checked.databinding.DialogItemBinding;
import com.djordjeratkovic.checked.model.ShoppingItem;
import com.djordjeratkovic.checked.model.StoreReceipt;

import java.util.List;

public class ItemDialog extends DialogFragment implements View.OnClickListener {


    private DialogItemBinding binding;
    private Dialog dialog;
    private ItemViewModel itemViewModel;
    private int category = -1;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DialogItemBinding.inflate(getLayoutInflater());

        itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);

        setListeners();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(binding.getRoot());

        builder.setPositiveButton(getString(R.string.add), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                checkFields();
            }
        });


        //dajes ga dialogu da bi dialog mogao tastaturu da dobije
        dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return dialog;
    }

    private void checkFields() {
        if (!binding.nameEditText.getText().toString().trim().isEmpty() &&
                category != -1) {
            ShoppingItem shoppingItem = new ShoppingItem(binding.nameEditText.getText().toString().trim(),
                    category,
                    createStoreReceipts(),
                    null,
                    1);
            //TODO: set qunatity here
            addShoppingItem(shoppingItem);
        }
//        addShoppingItem();
    }

    private List<StoreReceipt> createStoreReceipts() {
        //TODO: create store receipts
        return null;
    }

    private void addShoppingItem(ShoppingItem shoppingItem) {
        itemViewModel.addShoppingItem(shoppingItem);
    }

    private void setListeners() {
        binding.categoryBtn1.setOnClickListener(this);
        binding.categoryBtn2.setOnClickListener(this);
        binding.categoryBtn3.setOnClickListener(this);
        binding.categoryBtn4.setOnClickListener(this);
        binding.categoryBtn5.setOnClickListener(this);
        binding.categoryBtn6.setOnClickListener(this);
        binding.categoryBtn7.setOnClickListener(this);
        binding.categoryBtn8.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
