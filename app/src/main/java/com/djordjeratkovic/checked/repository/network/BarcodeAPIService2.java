package com.djordjeratkovic.checked.repository.network;

import com.djordjeratkovic.checked.model.BarcodeAPI;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BarcodeAPIService2 {

    @GET("{barcode}.json")
    Call<BarcodeAPI> getProductInfo(@Path("barcode") String barcode);
}
