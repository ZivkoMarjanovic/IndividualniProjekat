package com.tus.dto;

import com.tus.model.ProductQuantity;
import java.util.Date;
import java.util.List;

public class OrderDto {

    private long orderId;

    private double orderTotal;

    private String customerName;

    private String customerEmail;

    private boolean paid;

    private List<ProductQuantityRequest> productQuantityRequests;

    public OrderDto() {
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public void setProductQuantityRequests(List<ProductQuantityRequest> productQuantityRequests) {
        this.productQuantityRequests = productQuantityRequests;
    }

    public long getOrderId() {
        return orderId;
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public boolean isPaid() {
        return paid;
    }

    public List<ProductQuantityRequest> getProductQuantityRequests() {
        return productQuantityRequests;
    }
}
