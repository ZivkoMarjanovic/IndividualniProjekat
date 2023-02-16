package com.tus.dto;

import com.tus.model.Product;

public class ProductQuantityRequest {
    private String productName;

    private int quantity;

    public ProductQuantityRequest() {
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }
}
