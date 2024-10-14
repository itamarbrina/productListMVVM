package com.example.myapplication.repositories;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.database.ProductsDatabase;
import com.example.myapplication.managers.FirstLoadManager;
import com.example.myapplication.models.Product;
import com.example.myapplication.retrofit.ApiRequest;
import com.example.myapplication.retrofit.RetrofitRequest;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductsRepository {
    private final ApiRequest apiRequest;
    private final ProductsDatabase productDatabase;
    private final FirstLoadManager firstLoadManager;
    private final ExecutorService executorService;

    public ProductsRepository(Context context) {
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        productDatabase = ProductsDatabase.getDatabase(context);
        firstLoadManager = new FirstLoadManager(context);
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Product>> getProducts() {
        MutableLiveData<List<Product>> data = new MutableLiveData<>();
        executorService.execute(() -> {
            if (!firstLoadManager.isFirstLoad()) {
                data.postValue(productDatabase.productsDao().getProduct());
            } else {
                fetchProducts(data);
            }
        });
        return data;
    }

    public void insertProduct(Product product) {
        executorService.execute(() -> productDatabase.productsDao().insertProducts(product));
    }

    public void deleteProduct(Product product) {
        executorService.execute(() -> productDatabase.productsDao().deleteProduct(product));
    }

    private void fetchProducts(MutableLiveData<List<Product>> data) {
        apiRequest.getProducts()
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                        if (response.body() != null && response.body() != null) {
                            List<Product> products = response.body();

                            executorService.execute(() -> {
                                productDatabase.productsDao().insertProducts(products);

                                executorService.execute(() -> {
                                    List<Product> allUsers = productDatabase.productsDao().getProduct();
                                    data.postValue(allUsers);
                                });
                            });

                            firstLoadManager.setFirstLoadComplete();
                        } else {
                            data.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                        data.setValue(null);
                    }

                });
    }
}

