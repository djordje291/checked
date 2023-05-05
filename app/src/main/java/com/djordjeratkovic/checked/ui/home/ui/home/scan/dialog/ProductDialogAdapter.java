package com.djordjeratkovic.checked.ui.home.ui.home.scan.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.djordjeratkovic.checked.R;
import com.djordjeratkovic.checked.databinding.ExpirationDateCardBinding;
import com.djordjeratkovic.checked.model.ExpirationDate;
import com.djordjeratkovic.checked.util.Constants;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ProductDialogAdapter extends RecyclerView.Adapter<ProductDialogAdapter.ViewHolder> {

    private List<ExpirationDate> expirationDates;
    private Context context;

    public ProductDialogAdapter(List<ExpirationDate> expirationDates, Context context) {
        this.expirationDates = expirationDates;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductDialogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ExpirationDateCardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()
        ), R.layout.expiration_date_card, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductDialogAdapter.ViewHolder holder, int position) {
        if (expirationDates.size() == 1) {
            holder.binding.deleteExpirationDate.setVisibility(View.GONE);
        }
        ExpirationDate expirationDate = expirationDates.get(position);
        holder.bind(expirationDate);
        holder.binding.expirationDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: set constraints not for less than today
                MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker()
                        .setSelection(expirationDate.getExpirationDate().getTime()).build();
//                        .setCalendarConstraints(constraintsBuilderRange.build()).build();
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
                        holder.binding.expirationDateEditText.setText(simpleDateFormat.format(selection));
                        expirationDate.setExpirationDate(new Date(selection));
                    }
                });
                materialDatePicker.show(((FragmentActivity) context).getSupportFragmentManager(), Constants.KEY_DATE_PICKER);
            }
        });
        holder.binding.quantityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence != null && !TextUtils.isEmpty(charSequence)) {
                    if (!String.valueOf(charSequence).equals("0")) {
                        expirationDate.setQuantity(Integer.parseInt(charSequence.toString()));
                    } else {
                        //TODO: make maybe toast for not 0
                    }
                }else {
                    //TODO: maybe make toast for not empty
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        holder.binding.deleteExpirationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expirationDates.remove(expirationDate);
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return expirationDates.size();
    }


    private void showDatePicker(ExpirationDate expirationDate) {
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ExpirationDateCardBinding binding;

        public ViewHolder(ExpirationDateCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ExpirationDate expirationDate) {
            binding.setExpirationDate(expirationDate);
        }
    }
}
