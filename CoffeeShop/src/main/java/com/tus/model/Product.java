package com.tus.model;

import javax.persistence.*;

@Entity
@Table (name="products")
public class Product {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column (name="product_id")
	private long id;
	@Column(name = "product_name")
	private String product_name;
	@Column (name="description")
	private String description;
	@Column (name="product_type")
	private Product type;
	@Column (name="product_price")
	private double price;
	@Column
	private int quantity;
	public Product(long id, String name, String description, Product type, double price, int quantity) {
		super();
		this.id = id;
		this.product_name = name;
		this.description = description;
		this.type = type;
		this.price = price;
		this.quantity = quantity;
	}
	
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Product() {
		super();
	}

	public long getId() {
		return id;
	}
	public String getName() {
		return product_name;
	}
	public void setName(String name) {
		this.product_name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Product getType() {
		return type;
	}
	public void setType(Product type) {
		this.type = type;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	

}
