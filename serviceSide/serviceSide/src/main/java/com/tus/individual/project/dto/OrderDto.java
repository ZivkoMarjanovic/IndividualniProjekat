package com.tus.individual.project.dto;

import com.tus.individual.project.model.ProductQuantity;

import java.util.Date;
import java.util.List;

public class OrderDto {
	
	private long id;

    private double orderTotal;

    private String customerName;

    private String customerEmail;

    private boolean paid;

    private Date created;

    private Date finished;
    
    private String status;

    private List<ProductQuantity> productsWithQuantities;

	public OrderDto(long id, double orderTotal, String customerName, String customerEmail, boolean paid, Date created,
			Date finished, String status, List<ProductQuantity> productsWithQuantities) {
		super();
		this.id = id;
		this.orderTotal = orderTotal;
		this.customerName = customerName;
		this.customerEmail = customerEmail;
		this.paid = paid;
		this.created = created;
		this.finished = finished;
		this.status = status;
		this.productsWithQuantities = productsWithQuantities;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(double orderTotal) {
		this.orderTotal = orderTotal;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<ProductQuantity> getProductsWithQuantities() {
		return productsWithQuantities;
	}

	public void setProductsWithQuantities(List<ProductQuantity> productsWithQuantities) {
		this.productsWithQuantities = productsWithQuantities;
	}
}
