package com.djordjeratkovic.checked.repository.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BarcodeAPIClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://barcode.monster/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
