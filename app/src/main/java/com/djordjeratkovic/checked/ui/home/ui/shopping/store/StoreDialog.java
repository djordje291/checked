package com.djordjeratkovic.checked.ui.home.ui.shopping.store;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.djordjeratkovic.checked.R;
import com.djordjeratkovic.checked.databinding.DialogItemBinding;
import com.djordjeratkovic.checked.databinding.DialogStoreBinding;
import com.djordjeratkovic.checked.model.Store;

public class StoreDialog extends DialogFragment {

    private DialogStoreBinding binding;
    private Dialog dialog;
    private StoreViewModel storeViewModel;


    public StoreDialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DialogStoreBinding.inflate(getLayoutInflater());

        storeViewModel = new ViewModelProvider(this).get(StoreViewModel.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(binding.getRoot());

        builder.setPositiveButton(getString(R.string.add), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                checkFields();
            }
        });

        binding.nameEditText.requestFocus();

        //dajes ga dialogu da bi dialog mogao tastaturu da dobije
        dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return dialog;
    }

    private void checkFields() {
        if (!binding.nameEditText.getText().toString().trim().isEmpty()) {
            Store store = new Store(binding.nameEditText.getText().toString().trim(), null);
            addStore(store);
        } else {
            Toast.makeText(getContext(), getString(R.string.fill_in_all_the_fields), Toast.LENGTH_SHORT).show();
        }
    }

    private void addStore(Store store) {
        storeViewModel.addStore(store);
    }
}
