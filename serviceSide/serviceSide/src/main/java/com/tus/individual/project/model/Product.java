package com.tus.individual.project.model;

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
