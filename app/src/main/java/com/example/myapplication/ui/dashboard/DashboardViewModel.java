package com.example.myapplication.ui.dashboard;

import static com.example.myapplication.mappers.ProductMapper.mapDbToUiProducts;
import static com.example.myapplication.mappers.ProductMapper.mapUiToDbProduct;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.models.ui.ProductUiModel;
import com.example.myapplication.repositories.ProductsRepository;

import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class DashboardViewModel extends AndroidViewModel {

    private final MutableLiveData<Integer> progressBarLiveData;
    private final MutableLiveData<List<ProductUiModel>> productsLiveData;
    private final MutableLiveData<String> errorMessage;
    private final ProductsRepository productsRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        this.productsRepository = new ProductsRepository(application.getApplicationContext());
        this.productsLiveData = new MutableLiveData<>();
        this.progressBarLiveData = new MutableLiveData<>();
        this.errorMessage = new MutableLiveData<>();
    }

    public LiveData<List<ProductUiModel>> getProductsLiveData() {
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

        disposables.add(
                productsRepository.getProducts()
                        .subscribe(products -> {
                            productsLiveData.postValue(mapDbToUiProducts(products));
                            setProgressBarLiveData(View.GONE);
                        }, throwable -> {
                            errorMessage.postValue(throwable.getMessage());
                            setProgressBarLiveData(View.GONE);
                        })
        );
    }


    public void insertProduct(ProductUiModel product) {
        disposables.add(
                productsRepository.insertProduct(product)
                        .subscribe(this::fetchProducts, throwable -> errorMessage.postValue(throwable.getMessage()))
        );
    }

    public void deleteProduct(ProductUiModel product) {
        disposables.add(
                productsRepository.deleteProduct(mapUiToDbProduct(product))
                        .subscribe(this::fetchProducts, throwable -> errorMessage.postValue(throwable.getMessage()))
        );
    }

    public void setProgressBarLiveData(Integer visibility) {
        progressBarLiveData.postValue(visibility);
    }

    public boolean isProductValid(ProductUiModel product, Map<String, String> errorMessages) {
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

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}

