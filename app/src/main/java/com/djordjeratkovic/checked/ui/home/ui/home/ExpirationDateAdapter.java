package com.djordjeratkovic.checked.ui.home.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.djordjeratkovic.checked.R;
import com.djordjeratkovic.checked.databinding.CardProductBinding;
import com.djordjeratkovic.checked.databinding.CardProductExpirationDateBinding;
import com.djordjeratkovic.checked.model.ExpirationDate;
import com.djordjeratkovic.checked.model.Product;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ExpirationDateAdapter extends RecyclerView.Adapter<ExpirationDateAdapter.ViewHolder> {

    private Product product;
    private List<ExpirationDate> expirationDates = new ArrayList<>();
    private HomeViewModel homeViewModel;
    private Context context;
    private HomeAdapter adapter;
    private int adapterPosition;

    public ExpirationDateAdapter(Product product, HomeViewModel homeViewModel, Context context, HomeAdapter adapter, int position) {
        this.product = product;
        this.expirationDates = product.getExpirationDates();
        this.homeViewModel = homeViewModel;
        this.context = context;
        this.adapter = adapter;
        this.adapterPosition = position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardProductExpirationDateBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()
        ), R.layout.card_product_expiration_date, parent, false);
        return new ExpirationDateAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExpirationDate expirationDate = expirationDates.get(position);
        holder.bind(expirationDate);
        checkDate(expirationDate, holder);
        holder.binding.deleteExpirationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context)
                        .setMessage(context.getResources().getString(R.string.are_you_sure_you_want_to_delete))
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (expirationDates.size() != 1) {
                                    expirationDates.remove(expirationDate);
                                    notifyItemRemoved(holder.getAdapterPosition());
                                    homeViewModel.updateProduct(product);
                                } else {
                                    homeViewModel.deleteProduct(product);
                                    adapter.notifyItemRemoved(adapterPosition);
                                }
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                builder.show();
            }
        });
        holder.binding.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (expirationDate.getQuantity() > 1) {
                    expirationDate.setQuantity(expirationDate.getQuantity() - 1);
                    holder.bind(expirationDate);
                    homeViewModel.updateProduct(product);

                    //no?
                    adapter.notifyItemChanged(adapterPosition);
                } else {
                    Toast.makeText(context, R.string.cant_go_lower, Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.binding.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expirationDate.setQuantity(expirationDate.getQuantity() + 1);
                holder.bind(expirationDate);
                homeViewModel.updateProduct(product);

                //no?
                adapter.notifyItemChanged(adapterPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return expirationDates.size();
    }

    private void checkDate(ExpirationDate expirationDate, ViewHolder holder) {
        Calendar week = Calendar.getInstance();
        week.add(Calendar.DAY_OF_MONTH, 7);
        Calendar threeDays = Calendar.getInstance();
        threeDays.add(Calendar.DAY_OF_MONTH, 3);
        Calendar date = Calendar.getInstance();
        date.setTime(expirationDate.getExpirationDate());
        if (date.before(threeDays)){
            //red
            holder.binding.expirationDate.setTextColor(ContextCompat.getColor(context, R.color.red));
        } else if (date.before(week)) {
            //orange
            holder.binding.expirationDate.setTextColor(ContextCompat.getColor(context, R.color.bright_orange));
        } else {
            //green?
            holder.binding.expirationDate.setTextColor(ContextCompat.getColor(context, R.color.blue_light));
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardProductExpirationDateBinding binding;

        public ViewHolder(@NonNull CardProductExpirationDateBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ExpirationDate expirationDate) {
            binding.setExpirationDate(expirationDate);
        }
    }
}
