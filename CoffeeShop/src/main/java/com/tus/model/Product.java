package com.tus.model;

import javax.persistence.*;

@Entity
@Table (name="product")
public class Product {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column (name="id")
	private long id;
	@Column(name = "product_name")
	private String product_name;
	@Column (name="description")
	private String description;
	@Column (name="product_price")
	private double price;
	@Column (name="is_active")
	private boolean isActive;

	public Product() {
	}

	public Product(String product_name, String description, double price, boolean isActive) {
		this.product_name = product_name;
		this.description = description;
		this.price = price;
		this.isActive = isActive;
	}

	public long getId() {
		return id;
	}

	public String getProduct_name() {
		return product_name;
	}

	public String getDescription() {
		return description;
	}

	public double getPrice() {
		return price;
	}

	public boolean isActive() {
		return isActive;
	}
}
