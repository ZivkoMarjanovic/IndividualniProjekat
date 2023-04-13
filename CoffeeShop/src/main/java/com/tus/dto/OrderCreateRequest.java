package com.tus.dto;

import java.util.List;

public class OrderCreateRequest {

    private String customerName;

    private String customerEmail;

    private List<ProductQuantityRequest> productsWithQuantities;

    private boolean paid;

    public OrderCreateRequest() {
    }

    public OrderCreateRequest(String customerName, String customerEmail, List<ProductQuantityRequest> productsWithQuantities, boolean paid) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.productsWithQuantities = productsWithQuantities;
        this.paid = paid;
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

    public boolean isPaid() {
        return paid;
    }
}
