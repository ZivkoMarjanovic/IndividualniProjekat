package com.tus.dto;

import com.tus.model.ProductQuantity;
import java.util.Date;
import java.util.List;

public class OrderDto {

    private double order_total;

    private String customer_name;

    private String customer_email;

    private boolean paid;

    private Date created;

    private Date finished;

    private List<ProductQuantity> productsWithQuantities;

    public OrderDto(double order_total, String customer_name, String customer_email, boolean paid, Date created, Date finished, List<ProductQuantity> productsWithQuantities) {
        this.order_total = order_total;
        this.customer_name = customer_name;
        this.customer_email = customer_email;
        this.paid = paid;
        this.created = created;
        this.finished = finished;
        this.productsWithQuantities = productsWithQuantities;
    }

    public double getOrder_total() {
        return order_total;
    }

    public void setOrder_total(double order_total) {
        this.order_total = order_total;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getFinished() {
        return finished;
    }

    public void setFinished(Date finished) {
        this.finished = finished;
    }

    public List<ProductQuantity> getProductsWithQuantities() {
        return productsWithQuantities;
    }

    public void setProductsWithQuantities(List<ProductQuantity> productsWithQuantities) {
        this.productsWithQuantities = productsWithQuantities;
    }
}
