package com.example.myapplication.ui.dashboard;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.models.Product;
import com.example.myapplication.repositories.ProductsRepository;

import java.util.List;
import java.util.Map;

public class DashboardViewModel extends AndroidViewModel {

    private final MutableLiveData<Integer> progressBarLiveData;
    private final MutableLiveData<List<Product>> productsLiveData;
    private final MutableLiveData<String> errorMessage;
    private final ProductsRepository productsRepository;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        this.productsRepository = new ProductsRepository(application.getApplicationContext());
        this.productsLiveData = new MutableLiveData<>();
        this.progressBarLiveData = new MutableLiveData<>();
        this.errorMessage = new MutableLiveData<>();
    }

    public LiveData<List<Product>> getProductsLiveData() {
        return productsLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Integer> getProgressBarLiveData() {
        return progressBarLiveData;
    }

    public void fetchProducts() {
        errorMessage.setValue(null);
        setProgressBarLiveData(View.VISIBLE);

        productsRepository.getProducts().observeForever(products -> {
            setProgressBarLiveData(View.GONE);
            if (products != null) {
                productsLiveData.setValue(products);
            } else {
                errorMessage.setValue("An error occurred while fetching users");
            }
        });
    }

    public void insertUser(Product product) {
        productsRepository.insertProduct(product);
    }

    public void deleteUser(Product product) {
        productsRepository.deleteProduct(product);
    }

    public void setProgressBarLiveData(Integer visibility) {
        progressBarLiveData.setValue(visibility);
    }

    public boolean isUserValid(Product product, Map<String, String> errorMessages) {
        boolean isValid = true;

        if (product.getTitle().isEmpty()) {
            errorMessages.put("titleError", "Title is required");
            isValid = false;
        }

        if (product.getDescription().isEmpty()) {
            errorMessages.put("descriptionError", "Description is required");
            isValid = false;
        }
        if (product.getPrice().isEmpty()) {
            errorMessages.put("priceError", "Price is required");
            isValid = false;
        }

        if (product.getCategory().isEmpty()) {
            errorMessages.put("categoryError", "Category is required");
            isValid = false;
        }
        if (product.getImageUrl().isEmpty()) {
            product.setImageUrl("https://static.vecteezy.com/system/resources/thumbnails/004/141/669/small/no-photo-or-blank-image-icon-loading-images-or-missing-image-mark-image-not-available-or-image-coming-soon-sign-simple-nature-silhouette-in-frame-isolated-illustration-vector.jpg");
        }

        return isValid;
    }
}

