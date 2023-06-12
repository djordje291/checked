package com.djordjeratkovic.checked.util;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.djordjeratkovic.checked.R;
import com.djordjeratkovic.checked.model.ExpirationDate;
import com.djordjeratkovic.checked.model.Product;

import java.util.Calendar;
import java.util.List;

public class CommonUtils {
    private static final int REQUEST_IMAGE_CAPTURE = 1;


    public static void loading(View v, boolean b) {
        if (b) {
            v.setVisibility(View.VISIBLE);
        } else {
            v.setVisibility(View.GONE);
        }
    }

    public static boolean requestCamera(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
            return false;
        } else {
            return true;
        }
    }

    public static boolean isItStorage(String imageUrl) {
        return imageUrl.contains(Constants.KEY_STORAGE_DIR);
    }

    public static String removePrefix(String string) {
        return string.replace(Constants.KEY_STORAGE_DIR, "");
    }

    public static RequestListener<Drawable> imageLoadingListener(final LottieAnimationView loadingImage) {
        return new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                //hide the animation
                loadingImage.pauseAnimation();
                loadingImage.setVisibility(View.GONE);
                return false;//let Glide handle everything else
            }


        };
    }

    public static String getDatabaseId(Application application) {
        SharedPreferences sharedPreferences = application.getSharedPreferences(Constants.KEY_SHARED_PREFERENCES, MODE_PRIVATE);
        return sharedPreferences.getString(Constants.KEY_DATABASE_ID, null);
    }

    public static void setProductTextColorAndSize(Context context, List<ExpirationDate> list, TextView view, int textSize) {
        Calendar calendar = Calendar.getInstance();
        Calendar week = Calendar.getInstance();
        week.add(Calendar.DAY_OF_MONTH, 7);
        Calendar threeDays = Calendar.getInstance();
        threeDays.add(Calendar.DAY_OF_MONTH, 3);
        for (ExpirationDate expirationDate : list) {
            if (expirationDate.getExpirationDate().before(calendar.getTime())) {
                view.setTextColor(ContextCompat.getColor(context, R.color.purple));
                view.setTextSize(textSize + 2);
                break;
            } else if (expirationDate.getExpirationDate().before(threeDays.getTime())){
                view.setTextColor(ContextCompat.getColor(context, R.color.red));
                view.setTextSize(textSize + 1);
            } else if (expirationDate.getExpirationDate().before(week.getTime())
            && view.getTextSize() != textSize + 1) {
                view.setTextColor(ContextCompat.getColor(context, R.color.orange));
                view.setTextSize(textSize);
            }
        }
    }
}
