package com.example.myapplication.retrofit;

import com.example.myapplication.models.api.ProductApiModel;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import retrofit2.http.GET;

public interface ApiService {
    @GET("products")
    Flowable<List<ProductApiModel>> getProducts();
}
