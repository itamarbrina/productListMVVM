package com.example.myapplication.repositories;

import android.content.Context;

import com.example.myapplication.database.ProductsDatabase;
import com.example.myapplication.managers.FirstLoadManager;
import com.example.myapplication.models.Product;
import com.example.myapplication.retrofit.ApiService;
import com.example.myapplication.retrofit.RetrofitRequest;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class ProductsRepository {
    private final ApiService apiService;
    private final ProductsDatabase productDatabase;
    private final FirstLoadManager firstLoadManager;

    public ProductsRepository(Context context) {
        apiService = RetrofitRequest.getRetrofitInstance().create(ApiService.class);
        productDatabase = ProductsDatabase.getDatabase(context);
        firstLoadManager = new FirstLoadManager(context);
    }

    public Flowable<List<Product>> getProducts() {
        if (!firstLoadManager.isFirstLoad()) {
            return productDatabase.productsDao().getProducts();
        } else {
            return apiService.getProducts()
                    .doOnNext(products -> {
                        productDatabase.productsDao().insertProducts(products);
                        firstLoadManager.setFirstLoadComplete();
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }

    public Completable insertProduct(Product product) {
        return Completable.fromAction(() -> productDatabase.productsDao().insertProducts(product))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Completable deleteProduct(Product product) {
        return Completable.fromAction(() -> productDatabase.productsDao().deleteProduct(product))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

