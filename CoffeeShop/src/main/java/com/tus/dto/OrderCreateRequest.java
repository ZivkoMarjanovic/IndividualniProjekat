package com.tus.dto;

import java.util.List;

public class OrderCreateRequest {

    private String customerName;

    private String customerEmail;

    private List<ProductQuantityRequest> productsWithQuantities;

    public OrderCreateRequest() {
    }

    public OrderCreateRequest(String customerName, String customerEmail, List<ProductQuantityRequest> productsWithQuantities) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.productsWithQuantities = productsWithQuantities;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public List<ProductQuantityRequest> getProductsWithQuantities() {
        return productsWithQuantities;
    }
}
