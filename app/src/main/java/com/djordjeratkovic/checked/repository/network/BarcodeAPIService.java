package com.djordjeratkovic.checked.repository.network;

import com.djordjeratkovic.checked.model.Item;

import org.json.JSONArray;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BarcodeAPIService {

    @GET("{barcode}")
    Call<Item> getProductInfo(@Path("barcode") String barcode);
}
