package com.example.myapplication.retrofit;

import com.example.myapplication.models.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiRequest {
    @GET("products")
    Call<List<Product>> getProducts();
}
