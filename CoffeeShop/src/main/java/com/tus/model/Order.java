package com.tus.model;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Table (name="coffee_order")
public class Order {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private long id;
	
	@Column(name="order_total")
	private double order_total;

	@Column(name="customer_name")
	private String customer_name;

	@Column(name="customer_email")
	private String customer_email;

	@Column(name="paid")
	private boolean paid;

	@Column(name="created")
	private Date created;

	@Column(name="finished")
	private Date finished;
	
	@OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonIgnore
	private List <ProductQuantity> productsWithQuantities;

	public Order() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Order)) return false;
		Order order = (Order) o;
		return Double.compare(order.getOrder_total(), getOrder_total()) == 0 && isPaid() == order.isPaid() && Objects.equals(getCustomer_name(), order.getCustomer_name()) && Objects.equals(getCustomer_email(), order.getCustomer_email()) && Objects.equals(getCreated(), order.getCreated()) && Objects.equals(getFinished(), order.getFinished()) && Objects.equals(getProductsWithQuantities(), order.getProductsWithQuantities());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getOrder_total(), getCustomer_name(), getCustomer_email(), isPaid(), getCreated(), getFinished(), getProductsWithQuantities());
	}
}
