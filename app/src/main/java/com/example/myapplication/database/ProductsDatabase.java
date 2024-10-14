package com.example.myapplication.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myapplication.models.Product;

@Database(entities = {Product.class}, version = 1, exportSchema = false)
public abstract class ProductsDatabase extends RoomDatabase {
    private static volatile ProductsDatabase INSTANCE;

    public abstract ProductsDao productsDao();

    public static ProductsDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ProductsDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ProductsDatabase.class, "product_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
