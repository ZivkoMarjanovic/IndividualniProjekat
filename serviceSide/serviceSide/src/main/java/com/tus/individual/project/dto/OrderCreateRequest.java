package com.tus.individual.project.dto;

import java.util.List;

public class OrderCreateRequest {

    private String customerName;

    private String customerEmail;

    private List<ProductQuantityRequest> productsWithQuantities;

    public OrderCreateRequest() {
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
