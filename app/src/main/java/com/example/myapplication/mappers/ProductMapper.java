package com.example.myapplication.mappers;

import com.example.myapplication.models.api.ProductApiModel;
import com.example.myapplication.models.db.ProductDbModel;
import com.example.myapplication.models.ui.ProductUiModel;

import java.util.ArrayList;
import java.util.List;

public class ProductMapper {

    public static ProductDbModel mapApiToDbProduct(ProductApiModel productApiModel) {
        ProductDbModel productDbModel = new ProductDbModel();
        productDbModel.setId(productApiModel.getId());
        productDbModel.setTitle(productApiModel.getTitle());
        productDbModel.setPrice(productApiModel.getPrice());
        productDbModel.setDescription(productApiModel.getDescription());
        productDbModel.setCategory(productApiModel.getCategory());
        productDbModel.setImageUrl(productApiModel.getImageUrl());
        return productDbModel;
    }

    public static ProductUiModel mapDbToUiProduct(ProductDbModel productDbModel) {
        ProductUiModel productUiModel = new ProductUiModel();
        productUiModel.setId(productDbModel.getId());
        productUiModel.setTitle(productDbModel.getTitle());
        productUiModel.setPrice(productDbModel.getPrice());
        productUiModel.setDescription(productDbModel.getDescription());
        productUiModel.setCategory(productDbModel.getCategory());
        productUiModel.setImageUrl(productDbModel.getImageUrl());
        return productUiModel;
    }

    public static ProductDbModel mapUiToDbProduct(ProductUiModel productUiModel) {
        ProductDbModel productDbModel = new ProductDbModel();
        productDbModel.setId(productUiModel.getId());
        productDbModel.setTitle(productUiModel.getTitle());
        productDbModel.setPrice(productUiModel.getPrice());
        productDbModel.setDescription(productUiModel.getDescription());
        productDbModel.setCategory(productUiModel.getCategory());
        productDbModel.setImageUrl(productUiModel.getImageUrl());
        return productDbModel;
    }

    public static List<ProductDbModel> mapApiToDbProducts(List<ProductApiModel> productApiModels) {
        List<ProductDbModel> productDbModels = new ArrayList<>();
        for (ProductApiModel productApiModel : productApiModels) {
            productDbModels.add(mapApiToDbProduct(productApiModel));
        }
        return productDbModels;
    }

    public static List<ProductUiModel> mapDbToUiProducts(List<ProductDbModel> productDbModels) {
        List<ProductUiModel> productUiModels = new ArrayList<>();
        for (ProductDbModel productDbModel : productDbModels) {
            productUiModels.add(mapDbToUiProduct(productDbModel));
        }
        return productUiModels;
    }

}
