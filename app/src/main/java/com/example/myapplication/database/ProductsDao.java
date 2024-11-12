package com.example.myapplication.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.myapplication.models.db.ProductDbModel;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface ProductsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProducts(List<ProductDbModel> products);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProducts(ProductDbModel product);

    @Query("SELECT * FROM PRODUCTS")
    Flowable<List<ProductDbModel>> getProducts();

    @Delete
    void deleteProduct(ProductDbModel product);
}
