package com.djordjeratkovic.checked.util;

import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.List;

public class Sleeper {

    //TODO: DEPRICATED?

    private TextView emptyTV;
    private CircularProgressIndicator loading;
    private List list;
    private List list2;
    private LottieAnimationView lottie;

    public Sleeper(TextView emptyTV, CircularProgressIndicator loading, List list, List list2, LottieAnimationView lottie) {
        this.emptyTV = emptyTV;
        this.loading = loading;
        this.list = list;
        this.list2 = list2;
        this.lottie = lottie;
    }


    public void start() {
        loading.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                } // Just catch the InterruptedException
                handler.post(new Runnable() {
                    public void run() {
                        if (list.isEmpty() && list2.isEmpty()) {
                            loading.setVisibility(View.GONE);
                            if (loading.getVisibility() == View.GONE) {
                                emptyTV.setVisibility(View.VISIBLE);
                                lottie.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
            }
        }).start();
    }
}