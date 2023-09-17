package com.djordjeratkovic.checked.repository;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.djordjeratkovic.checked.model.Database;
import com.djordjeratkovic.checked.model.ExpirationDate;
import com.djordjeratkovic.checked.model.Item;
import com.djordjeratkovic.checked.model.BarcodeAPI;
import com.djordjeratkovic.checked.model.Product;
import com.djordjeratkovic.checked.model.ShoppingItem;
import com.djordjeratkovic.checked.model.Store;
import com.djordjeratkovic.checked.repository.network.BarcodeAPIClient;
import com.djordjeratkovic.checked.repository.network.BarcodeAPIClient2;
import com.djordjeratkovic.checked.repository.network.BarcodeAPIService;
import com.djordjeratkovic.checked.repository.network.BarcodeAPIService2;
import com.djordjeratkovic.checked.util.CommonUtils;
import com.djordjeratkovic.checked.util.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckedRepository {

    private static final String TAG = "CHCKDR";
    private static CheckedRepository instance = null;
    private Application application = null;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private CollectionReference databaseReference;
    private CollectionReference productReference;
    private CollectionReference shoppingItemReference;
    private CollectionReference storesReference;
    private StorageReference storageReference;

    private BarcodeAPIService barcodeAPIService;

    MutableLiveData<Product> product = new MutableLiveData<>();
    List<Product> productsList = new ArrayList<>();
    List<ShoppingItem> shoppingItemsList = new ArrayList<>();
    List<Store> storesList = new ArrayList<>();

    MutableLiveData<BarcodeAPI> barcodeAPI = new MutableLiveData<>();
    MutableLiveData<List<Product>> products = new MutableLiveData<>();
    MutableLiveData<List<ShoppingItem>> shoppingItems = new MutableLiveData<>();
    MutableLiveData<List<Store>> stores = new MutableLiveData<>();

    private CheckedRepository() {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
        this.databaseReference = db.collection(Constants.KEY_DATABASE_REFERENCE);
        this.productReference = db.collection(Constants.KEY_PRODUCT_REFERENCE);
        this.shoppingItemReference = db.collection(Constants.KEY_SHOPPING_ITEM_REFERENCE);
        this.storesReference = db.collection(Constants.KEY_STORE_REFERENCE);

        this.storageReference = FirebaseStorage.getInstance().getReference();
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
                addDatabase(authResult.getUser().getUid());
            }
        });
        return id;
    }

    private void addDatabase(String databaseId) {
        Database database = new Database();
        database.setDatabaseId(databaseId);
        databaseReference.whereEqualTo(Constants.KEY_DATABASE_ID, database.getDatabaseId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) {
                    databaseReference.add(database).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "onSuccess: added database");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: failed to add database");
                        }
                    });
                }
            }
        });
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

    public MutableLiveData<BarcodeAPI> getProductFromBarcode() {
        return barcodeAPI;
    }

    public MutableLiveData<List<Product>> getProducts() {
        productReference
                .orderBy(Constants.KEY_CATEGORY)
                .orderBy(Constants.KEY_NAME)
                .whereEqualTo(Constants.KEY_DATABASE_ID, CommonUtils.getDatabaseId(application))
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        Log.d(TAG, "onEvent: ITS CALLED");
                        if (error != null) {
                            Log.d(TAG, error.toString());

                            return;
                        }

                        if (value != null && !value.isEmpty()) {
                            if (!productsList.isEmpty()) {
                                productsList.clear();
                            }
                            for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                                Product product = documentSnapshot.toObject(Product.class);
                                if (product != null) {
                                    Log.d(TAG, "onEvent: " + product.isHasLow());
                                    product.setDocRef(documentSnapshot.getId());
                                }
                                productsList.add(product);
                            }
                            products.postValue(productsList);
                        } else {
                            if (!productsList.isEmpty()) {
                                productsList.clear();
                            }
                            products.postValue(productsList);
                        }
                    }
                });
        products.setValue(productsList);
        return products;
    }

//    private String getDatabaseId() {
//        SharedPreferences sharedPreferences = application.getSharedPreferences(Constants.KEY_SHARED_PREFERENCES, MODE_PRIVATE);
//        return sharedPreferences.getString(Constants.KEY_DATABASE_ID, null);
//    }

    private void addProduct(Product product, Uri imageUri) {
        product.setDatabaseId(CommonUtils.getDatabaseId(application));
        productReference.add(product).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "onSuccess: added product");
                if (imageUri != null) {
                    uploadImageFile(product, imageUri);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: failed to add product");
            }
        });
    }

    public void checkIfThereIsAProduct(Product product, Uri imageUri) {
        Query query;
        List<ExpirationDate> tempList = new ArrayList<>();
        if (product.getBarcode() != null) {
            query = productReference
                    .where(Filter.or(
                            Filter.equalTo(Constants.KEY_BARCODE, product.getBarcode())
                            , Filter.and(Filter.equalTo(Constants.KEY_BRAND, product.getBrand()),
                                    Filter.equalTo(Constants.KEY_NAME, product.getName()))));
        } else {
            query = productReference.whereEqualTo(Constants.KEY_NAME, product.getName());
        }
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.getResult() != null && !task.getResult().isEmpty()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                        Product oldProduct = documentSnapshot.toObject(Product.class);
                        if (oldProduct != null) {
                            product.setDocRef(documentSnapshot.getId());
                            tempList.addAll(product.getExpirationDates());
                            product.setExpirationDates(oldProduct.getExpirationDates());
                            product.getExpirationDates().addAll(tempList);
                            updateProduct(product, imageUri);
                        }
                    }
                } else {
                    addProduct(product, imageUri);
                }
            }
        });
    }

    public void updateProduct(Product product, Uri imageUri) {
        Map<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_WEIGHT, product.getWeight());
        updates.put(Constants.KEY_IMAGE_URL, product.getImageUrl());
        updates.put(Constants.KEY_BARCODE, product.getBarcode());
        updates.put(Constants.KEY_BRAND, product.getBrand());
        updates.put(Constants.KEY_NAME, product.getName());
        updates.put(Constants.KEY_EXPIRATION_DATES, product.getExpirationDates());
        updates.put(Constants.KEY_CATEGORY, product.getCategory());
        updates.put(Constants.KEY_HAS_LOW, product.isHasLow());
        productReference.document(product.getDocRef()).update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "onSuccess: updated product");
                if (imageUri != null) {
                    uploadImageFile(product, imageUri);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: failed to update product");
            }
        });
    }

    public void deleteProduct(Product product) {
        productReference.document(product.getDocRef()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "onSuccess: deleted product");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: failed to delete product");
            }
        });
    }

    private void uploadImageFile(Product product, Uri imageUri) {
        storageReference.child(CommonUtils.removePrefix(product.getImageUrl())).putFile(imageUri).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Map<String, Object> updates = new HashMap<>();
                updates.put(Constants.KEY_IMAGE_URL, null);
                if (product.getDocRef() != null) {
                    productReference.document(product.getDocRef()).update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
            }
        });
    }

    public void addShoppingItem(ShoppingItem shoppingItem) {
        shoppingItem.setDatabaseId(CommonUtils.getDatabaseId(application));
        shoppingItemReference.add(shoppingItem).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "onSuccess: added shopping item");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: failed to add shopping item");
            }
        });
    }

    public MutableLiveData<List<ShoppingItem>> getShoppingItems() {
        shoppingItemReference
                .orderBy(Constants.KEY_CATEGORY)
                .orderBy(Constants.KEY_NAME)
                .whereEqualTo(Constants.KEY_DATABASE_ID, CommonUtils.getDatabaseId(application))
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.d(TAG, error.toString());

                            return;
                        }

                        if (value != null && !value.isEmpty()) {
                            if (!shoppingItemsList.isEmpty()) {
                                shoppingItemsList.clear();
                            }
                            for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                                ShoppingItem shoppingItem = documentSnapshot.toObject(ShoppingItem.class);
                                if (shoppingItem != null) {
                                    shoppingItem.setDocRef(documentSnapshot.getId());
                                }
                                shoppingItemsList.add(shoppingItem);
                            }
                            shoppingItems.postValue(shoppingItemsList);
                        } else {
                            if (!shoppingItemsList.isEmpty()) {
                                shoppingItemsList.clear();
                            }
                            shoppingItems.postValue(shoppingItemsList);
                        }
                    }
                });
        shoppingItems.setValue(shoppingItemsList);
        return shoppingItems;
    }

    public void updateShoppingItem(ShoppingItem shoppingItem) {
        Map<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_NAME, shoppingItem.getName());
        updates.put(Constants.KEY_CATEGORY, shoppingItem.getCategory());
        updates.put(Constants.KEY_STORE_RECEIPTS, shoppingItem.getStoreReceipts());
        shoppingItemReference.document(shoppingItem.getDocRef()).update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "onSuccess: updated shopping item");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: failed to update shopping item");
            }
        });
    }

    public void deleteShoppingItem(ShoppingItem shoppingItem) {
        shoppingItemReference.document(shoppingItem.getDocRef()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "onSuccess: deleted shopping item");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: failed to delete shopping item");
            }
        });
    }


    public void addStore(Store store) {
        store.setDatabaseId(CommonUtils.getDatabaseId(application));
        storesReference.add(store).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "onSuccess: added store");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: failed to add store");
            }
        });
    }

    public MutableLiveData<List<Store>> getStores() {
        storesReference
                .orderBy(Constants.KEY_NAME)
                .whereEqualTo(Constants.KEY_DATABASE_ID, CommonUtils.getDatabaseId(application))
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.d(TAG, error.toString());

                            return;
                        }

                        if (value != null && !value.isEmpty()) {
                            if (!storesList.isEmpty()) {
                                storesList.clear();
                            }
                            for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                                Store store = documentSnapshot.toObject(Store.class);
                                if (store != null) {
                                    store.setDocRef(documentSnapshot.getId());
                                }
                                storesList.add(store);
                            }
                            stores.postValue(storesList);
                        } else {
                            if (!storesList.isEmpty()) {
                                storesList.clear();
                            }
                            stores.postValue(storesList);
                        }
                    }
                });
        stores.setValue(storesList);
        return stores;
    }

    public void updateStore(Store store) {
        Map<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_NAME, store.getName());
        storesReference.document(store.getDocRef()).update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "onSuccess: updated store");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: failed to update store");
            }
        });
    }

    public void deleteStore(Store store) {
        storesReference.document(store.getDocRef()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "onSuccess: deleted store");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: failed to delete store");
            }
        });
    }

    public StorageReference getStorageReference(String uri) {
        return storageReference.getStorage().getReferenceFromUrl(uri);
    }
}
