package com.djordjeratkovic.checked.ui.home.ui.home.scan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.DecodeCallback;
import com.djordjeratkovic.checked.R;
import com.djordjeratkovic.checked.databinding.ActivityScanBinding;
import com.djordjeratkovic.checked.model.BarcodeAPI;
import com.djordjeratkovic.checked.model.Product;
import com.djordjeratkovic.checked.ui.home.ui.home.scan.dialog.ProductDialog;
import com.djordjeratkovic.checked.util.Constants;
import com.google.zxing.Result;

public class ScanActivity extends AppCompatActivity {

    private ActivityScanBinding binding;
    private Context context;
    private Activity activity;

    private CodeScanner codeScanner;

    private ScanViewModel scanViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        scanViewModel = new ViewModelProvider(this).get(ScanViewModel.class);

        context = getApplicationContext();
        activity = this;

        requestCamera();

        codeScanner = new CodeScanner(this, binding.codeScannerView);
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                scanViewModel.checkBarcode(result.getText());
            }
        });

        scanViewModel.getProductFromBarcode().observe(this, new Observer<BarcodeAPI>() {
            @Override
            public void onChanged(BarcodeAPI barcodeAPI) {
                if (barcodeAPI.getProduct() != null) {
                    ProductDialog productDialog = new ProductDialog(barcodeAPI.getProduct(), codeScanner);
                    productDialog.show(getSupportFragmentManager(), Constants.PRODUCT_DIALOG_TAG);
                } else {
                    Toast.makeText(context, R.string.not_in_the_database, Toast.LENGTH_SHORT).show();
                    codeScanner.startPreview();
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constants.PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                startCamera();
            } else {
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void requestCamera() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//            startCamera();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, Constants.PERMISSION_REQUEST_CAMERA);
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, Constants.PERMISSION_REQUEST_CAMERA);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }

    private void setLoading(boolean b) {
        if (b) {
            binding.loading.setVisibility(View.VISIBLE);
        } else {
            binding.loading.setVisibility(View.GONE);
        }
    }
}