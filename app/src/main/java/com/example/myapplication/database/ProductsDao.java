package com.example.myapplication.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.myapplication.models.Product;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface ProductsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProducts(List<Product> products);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProducts(Product product);

    @Query("SELECT * FROM PRODUCTS")
    Flowable<List<Product>> getProducts();

    @Delete
    void deleteProduct(Product product);
}
