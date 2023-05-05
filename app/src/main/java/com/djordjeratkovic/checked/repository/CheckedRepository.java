package com.djordjeratkovic.checked.repository;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.djordjeratkovic.checked.model.Item;
import com.djordjeratkovic.checked.model.BarcodeAPI;
import com.djordjeratkovic.checked.model.Product;
import com.djordjeratkovic.checked.repository.network.BarcodeAPIClient;
import com.djordjeratkovic.checked.repository.network.BarcodeAPIClient2;
import com.djordjeratkovic.checked.repository.network.BarcodeAPIService;
import com.djordjeratkovic.checked.repository.network.BarcodeAPIService2;
import com.djordjeratkovic.checked.ui.home.HomeActivity;
import com.djordjeratkovic.checked.ui.main.MainActivity;
import com.djordjeratkovic.checked.util.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckedRepository {

    private static CheckedRepository instance = null;
    private Application application = null;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private CollectionReference databaseReference;
    private CollectionReference productReference;

    private BarcodeAPIService barcodeAPIService;

    MutableLiveData<Product> product = new MutableLiveData<>();
    MutableLiveData<BarcodeAPI> barcodeAPI = new MutableLiveData<>();
    MutableLiveData<List<Product>> products = new MutableLiveData<>();

    private CheckedRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        databaseReference = db.collection(Constants.KEY_DATABASE_REFERENCE);
        productReference = db.collection(Constants.KEY_PRODUCT_REFERENCE);
    }

    public static CheckedRepository getInstance() {
        if (instance == null) {
            instance = new CheckedRepository();
        }
        return instance;
    }

    public void setApplication(Application application) {
        if (this.application == null) {
            this.application = application;
        }
    }

    public MutableLiveData<Boolean> connectToADatabase(String databaseId) {
        MutableLiveData<Boolean> b = new MutableLiveData<>();
        databaseReference.whereEqualTo(Constants.KEY_DATABASE_ID, databaseId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) {
                    b.setValue(false);
                } else {
                    b.setValue(true);
                }
            }
        });
        return b;
    }

    public MutableLiveData<String> signInAsAGuest() {
        MutableLiveData<String> id = new MutableLiveData<>();
        firebaseAuth.signInAnonymously().addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                id.setValue(authResult.getUser().getUid());
            }
        });
        return id;
    }

    public MutableLiveData<String> signInWithGoogle() {
        return null;
    }

    public MutableLiveData<String> signInWithFacebook() {
        return null;
    }

    public MutableLiveData<Boolean> logout() {
        MutableLiveData<Boolean> b = new MutableLiveData<>();
        return b;
    }

    public MutableLiveData<Item> getBarcodeInfo(String barcode) {
        //TODO: barcode.monster make it look it up there with retrofit
        MutableLiveData<Item> item = new MutableLiveData<>();
        Call<Item> itemCall = BarcodeAPIClient.getClient().create(BarcodeAPIService.class).getProductInfo(barcode);

        itemCall.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                if (!response.isSuccessful()) {
                    Log.d("OMADIRASU", "onResponse:  failed");
                    return;
                }
                if (response.body() != null) {
                    item.setValue(response.body());
                }
                Log.d("OMADIRASU", response.body().getCompany() + " \n"
                        + "desc" + response.body().getDesctription() + "\n"
                        + "url" + response.body().getImageUrl() + "\n"
                        + "size" + response.body().getSize());
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                Log.d("OMADIRASU", "onFailure:  failed");

            }
        });
        return item;
    }

    public void checkBarcode(String barcode) {
        Call<BarcodeAPI> productCall = BarcodeAPIClient2.getClient().create(BarcodeAPIService2.class).getProductInfo(barcode);
        productCall.enqueue(new Callback<BarcodeAPI>() {
            @Override
            public void onResponse(Call<BarcodeAPI> call, Response<BarcodeAPI> response) {
                if (!response.isSuccessful()) {
                    Log.d("OMADIRASU", "onResponse2:  failed");
                    return;
                }
                if (response.body() != null) {
                    if (response.body().getProduct() != null) {
                        Log.d("OMADIRASU", response.body().getProduct().toString());
                    }
                    barcodeAPI.setValue(response.body());
//                    product.setValue(response.body().getProduct());
                }
            }

            @Override
            public void onFailure(Call<BarcodeAPI> call, Throwable t) {
                Log.d("OMADIRASU", "onFailure2:  failed");
            }
        });
    }


    //    public MutableLiveData<Product> getProductFromBarcode() {
//        return product;
//    }
    public MutableLiveData<BarcodeAPI> getProductFromBarcode() {
        return barcodeAPI;
    }

    public void addProduct(Product product) {
        //TODO: iz nekog razloga ne stavlja databaseId
        Log.d("STOTO", "getProducts: " + getDatabaseId());
        product.setDatabaseId(getDatabaseId());
        productReference.add(product).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public MutableLiveData<List<Product>> getProducts() {
        //TODO: orderBy pa snapshot
        List<Product> productsList = new ArrayList<>();
        productReference.whereEqualTo(Constants.KEY_DATABASE_ID, getDatabaseId())
                .orderBy(Constants.KEY_CATEGORY)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }

                        if (value != null && !value.isEmpty()) {
                            for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                                Product product = documentSnapshot.toObject(Product.class);
                                product.setDocRef(documentSnapshot.getId());
                                productsList.add(product);
                            }
                        }
                        products.setValue(productsList);
                    }
                });
        return products;
    }

    private String getDatabaseId() {
        //TODO: checkThis
        SharedPreferences sharedPreferences = application.getSharedPreferences(Constants.KEY_SHARED_PREFERENCES, MODE_PRIVATE);
//        if  (sharedPreferences.getString(Constants.KEY_USER_ID, null) != null) {
//            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//            startActivity(intent);
//        }
        return sharedPreferences.getString(Constants.KEY_DATABASE_ID, null);
    }
}
