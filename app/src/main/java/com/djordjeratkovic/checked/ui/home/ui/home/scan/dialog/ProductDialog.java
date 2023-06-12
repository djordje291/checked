package com.djordjeratkovic.checked.ui.home.ui.home.scan.dialog;

import static android.view.inputmethod.EditorInfo.IME_ACTION_NEXT;

import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.bumptech.glide.Glide;
import com.djordjeratkovic.checked.R;
import com.djordjeratkovic.checked.databinding.DialogProductBinding;
import com.djordjeratkovic.checked.model.ExpirationDate;
import com.djordjeratkovic.checked.model.Product;
import com.djordjeratkovic.checked.util.CommonUtils;
import com.djordjeratkovic.checked.util.Constants;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductDialog extends DialogFragment implements View.OnClickListener, TextView.OnEditorActionListener {
    private static final String TAG = "OBOZE";
    // error namesti da iskoci kad ti treba za edit text
    //TODO: refresh homeFragment when added
    //TODO: make imageUri null, and when picture taken make the file, and also create imageUrl when file created
    //TODO: delete old picture if in storage, and shrink the size of the new picture
    //TODO: loading image not working

    private DialogProductBinding binding;
    private Product product;
    private ProductDialogViewModel productDialogViewModel;
    private CodeScanner codeScanner;
    private List<ExpirationDate> expirationDates = new ArrayList<>();

    private String lastDate;

    private ProductDialogAdapter adapter;

    private int category = -1;

    private boolean isEdit;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ActivityResultLauncher<Uri> takePictureLauncher;
    private Uri photoUri;
    private Uri imageUri;

    private Activity activity;

    public ProductDialog(Product product, CodeScanner codeScanner, boolean isEdit, Activity activity) {
        this.product = product;
        if (product != null && product.getExpirationDates() != null) {
            this.expirationDates = product.getExpirationDates();
        }
        this.codeScanner = codeScanner;
        this.isEdit = isEdit;
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
        productDialogViewModel = new ViewModelProvider(this).get(ProductDialogViewModel.class);

        registerPictureLauncher();
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

        if (product != null) {
            bind();
        } else {
            binding.lottieImage.setVisibility(View.VISIBLE);
        }
        if (product == null || product.getExpirationDates() == null) {
            setExpirationDate();
        }
        setAdapter();
        setListeners();
        if (isEdit) {
            setupEdit();
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.toolbar.setNavigationOnClickListener(v -> {
            codeScannerStart();
            dismiss();
        });
        if (isEdit) {
            binding.toolbar.setTitle(getString(R.string.update_product));
            binding.toolbar.inflateMenu(R.menu.dialog_item_update);
        } else {
            binding.toolbar.setTitle(getString(R.string.new_product));
            binding.toolbar.inflateMenu(R.menu.dialog_item);
        }
        binding.toolbar.setOnMenuItemClickListener(item -> {
            if (isEdit) {
                if (updateProduct()) {
                    //maybe get adapter and position to notify
                    dismiss();
                }
                if (imageUri != null) {
                    Log.d(TAG, "onViewCreated: " + imageUri.toString() + isImageTaken());
                }
            } else {
                if (addProduct()) {
                    codeScannerStart();
                    dismiss();
                }
                if (imageUri != null) {
                    Log.d(TAG, "onViewCreated: " + imageUri.toString() + isImageTaken());
                }
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
//        if (product != null) {
//            bind();
//        } else {
//            binding.lottieImage.setVisibility(View.VISIBLE);
//        }
//        if (product == null || product.getExpirationDates() == null) {
//            setExpirationDate();
//        }
//        setAdapter();
//        setListeners();
//        if (isEdit) {
//            setupEdit();
//        }

    }

    private void setupEdit() {
        category = product.getCategory();
        bind();
        //maybe -1
        bindPrice(Integer.toString(product.getExpirationDates().get(product.getExpirationDates().size() - 1).getPrice()));
    }

    private void bind() {
        binding.setProduct(product);
        if (product.getImageUrl() != null) {
            if (CommonUtils.isItStorage(product.getImageUrl())) {
                Glide.with(this)
                        .load(productDialogViewModel.getStorageReference(product.getImageUrl()))
                        .addListener(CommonUtils.imageLoadingListener(binding.lottieLoadingImage))
                        .into(binding.image);
            } else {
                Glide.with(this)
                        .load(product.getImageUrl())
                        .addListener(CommonUtils.imageLoadingListener(binding.lottieLoadingImage))
                        .into(binding.image);
            }
            Glide.with(this).load(product.getImageUrl()).into(binding.image);
            binding.lottieImage.setVisibility(View.GONE);
        } else {
            binding.lottieImage.setVisibility(View.VISIBLE);
            binding.lottieLoadingImage.setVisibility(View.GONE);
        }
    }

    private void bindPrice(String price) {
        binding.setPrice(price);
    }

    private void setExpirationDate() {
        ExpirationDate expirationDate = new ExpirationDate(addDayToDate(new Date()), 1);
        expirationDates.add(expirationDate);
    }

    private void setAdapter() {
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
                binding.priceEditText.getText().toString().trim().isEmpty() ||
                getCategory() == -1) {
            Toast.makeText(getContext(), R.string.fill_in_all_the_fields, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            setPrices(Integer.parseInt(binding.priceEditText.getText().toString().trim()));
            if (product != null) {
                product.setBrand(binding.brandEditText.getText().toString().trim());
                product.setWeight(binding.weightEditText.getText().toString().trim());
                product.setName(binding.nameEditText.getText().toString().trim());
                product.setExpirationDates(expirationDates);
                product.setCategory(getCategory());
                product.setHasLow(false);
            } else {
                product = new Product(null, binding.weightEditText.getText().toString().trim(),
                        binding.brandEditText.getText().toString().trim(),
                        binding.nameEditText.getText().toString().trim(),
                        null, getCategory(),
                        expirationDates, null, false);
            }
            if (imageUri != null) {
                //TODO: Check if there is file image
                product.setImageUrl("gs://checked-7b32c.appspot.com/" + imageUri.getLastPathSegment());
            }
            productDialogViewModel.addProduct(product, imageUri);
            return true;
        }
    }

    private boolean isImageTaken() {
        File imageFile = new File(imageUri.getPath());
        return imageFile.exists();
    }

    private boolean updateProduct() {
        if (binding.brandEditText.getText().toString().trim().isEmpty() ||
                binding.weightEditText.getText().toString().trim().isEmpty() ||
                binding.nameEditText.getText().toString().trim().isEmpty() ||
                binding.priceEditText.getText().toString().trim().isEmpty() ||
                getCategory() == -1) {
            Toast.makeText(getContext(), R.string.fill_in_all_the_fields, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            product.setBrand(binding.brandEditText.getText().toString().trim());
            product.setWeight(binding.weightEditText.getText().toString().trim());
            product.setName(binding.nameEditText.getText().toString().trim());
            setPrices(Integer.parseInt(binding.priceEditText.getText().toString().trim()));
            product.setExpirationDates(expirationDates);
            product.setCategory(getCategory());
            if (imageUri != null) {
                //TODO: Check if there is file image
                product.setImageUrl("gs://checked-7b32c.appspot.com/" + imageUri.getLastPathSegment());
            }
            productDialogViewModel.updateProduct(product, imageUri);
            return true;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addExpirationDateBtn:
                ExpirationDate expirationDate = new ExpirationDate(addDayToDate(
                        expirationDates.get(expirationDates.size() - 1).getExpirationDate()),
                        1);
                expirationDates.add(expirationDate);
                adapter.notifyItemInserted(expirationDates.size() - 1);
                break;
            case R.id.image:
            case R.id.lottieImage:
                checkCameraPermissionAndOpenCamera();
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

    private Uri createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPG_" + timeStamp + "_" + CommonUtils.getDatabaseId(activity.getApplication());

        // Get the directory where the image will be saved
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // Create the image file
        File imageFile = null;
        try {
            imageFile = File.createTempFile(
                    imageFileName,  // Prefix for the file name
                    ".jpg",         // Suffix for the file name
                    storageDir      // Directory to save the file
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return the file URI
        return imageFile != null ? Uri.fromFile(imageFile) : null;
    }

    private Uri createImageUri() {
        //add database id to the name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File imageFile = new File(activity.getApplicationContext().getFilesDir(), imageFileName);
        return FileProvider.getUriForFile(
                activity.getApplicationContext(),
                "com.djordjeratkovic.checked.fileProvider",
                imageFile
        );
    }

    private void registerPictureLauncher() {
        takePictureLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (result) {
                    binding.image.setImageURI(null);
                    binding.image.setImageURI(imageUri);
                    binding.lottieImage.setVisibility(View.GONE);
                    Toast.makeText(activity, "picture taken", Toast.LENGTH_SHORT).show();
                } else {
                    //maybe get glide uri photo
                    binding.image.setImageURI(null);
                    binding.lottieImage.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void checkCameraPermissionAndOpenCamera() {
        //TODO: check if you delete app and say first no what happens
        if (CommonUtils.requestCamera(activity)) {
            imageUri = createImageUri();
            takePictureLauncher.launch(imageUri);
        }
    }


    private void setPrices(int price) {
        for (ExpirationDate expirationDate : expirationDates) {
            if (expirationDate.getPrice() == 0) {
                expirationDate.setPrice(price);
            }
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
        binding.weightEditText.setOnEditorActionListener(this);

        binding.lottieImage.setOnClickListener(this);
        binding.image.setOnClickListener(this);
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

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == IME_ACTION_NEXT) {
            switch (textView.getId()) {
                case R.id.weightEditText:
                    binding.nameEditText.requestFocus();
                    return true;
            }
        }
        return false;
    }
}