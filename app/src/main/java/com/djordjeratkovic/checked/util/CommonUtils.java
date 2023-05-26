package com.djordjeratkovic.checked.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;

public class CommonUtils {

    public static void loading(View v, boolean b) {
        if (b) {
            v.setVisibility(View.VISIBLE);
        } else {
            v.setVisibility(View.GONE);
        }
    }

    public static void requestCamera(Context context, Activity activity) {
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

//    public static void getCamera(Activity activity) {
//        ActivityResultLauncher<Uri> takePictureLauncher;
//        Uri photoUri;
//
//        takePictureLauncher = activity.registerForActivityResult(new ActivityResultContracts.TakePicture(),
//                new ActivityResultCallback<Boolean>() {
//                    @Override
//                    public void onActivityResult(Boolean isPhotoTaken) {
//                        if (isPhotoTaken) {
//                            // Photo was taken successfully, you can process the photo here
//                            // The photo can be accessed using the 'photoUri' variable
//                            // Process the photo here
//                        }
//                    }
//                });
//
//        // Start capturing the photo
//        dispatchTakePictureIntent();
//    }
}
