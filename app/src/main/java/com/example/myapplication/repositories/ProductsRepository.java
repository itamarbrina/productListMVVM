package com.example.myapplication.repositories;

import static com.example.myapplication.mappers.ProductMapper.mapApiToDbProducts;
import static com.example.myapplication.mappers.ProductMapper.mapUiToDbProduct;

import android.content.Context;

import com.example.myapplication.database.ProductsDatabase;
import com.example.myapplication.managers.FirstLoadManager;
import com.example.myapplication.models.db.ProductDbModel;
import com.example.myapplication.models.ui.ProductUiModel;
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

    public Flowable<List<ProductDbModel>> getProducts() {
        if (!firstLoadManager.isFirstLoad()) {
            return productDatabase.productsDao().getProducts();
        } else {
            return apiService.getProducts()
                    .doOnNext(products -> {
                        productDatabase.productsDao().insertProducts(mapApiToDbProducts(products));
                        firstLoadManager.setFirstLoadComplete();
                    })
                    .flatMap(products -> productDatabase.productsDao().getProducts())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }

    public Completable insertProduct(ProductUiModel product) {
        return Completable.fromAction(() -> productDatabase.productsDao().insertProducts(mapUiToDbProduct(product)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Completable deleteProduct(ProductDbModel product) {
        return Completable.fromAction(() -> productDatabase.productsDao().deleteProduct(product))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

