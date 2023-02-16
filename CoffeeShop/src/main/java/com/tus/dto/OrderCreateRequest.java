package com.tus.dto;

import java.util.List;

public class OrderCreateRequest {

    private String customer_name;

    private String customer_email;

    private List<ProductQuantityRequest> productsWithQuantities;

    public OrderCreateRequest() {
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public List<ProductQuantityRequest> getProductsWithQuantities() {
        return productsWithQuantities;
    }
}
