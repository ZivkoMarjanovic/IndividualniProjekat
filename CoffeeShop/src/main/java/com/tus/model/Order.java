package com.tus.model;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Table (name="orders")
public class Order {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column(name="order_id")
	private long order_id;
	
	@Column(name="order_total")
	private double order_total;
	
	@ManyToMany (mappedBy =  "products")
	@JsonIgnore
	public List <Product> products;


	public Order(long order_id, double order_total) {
		super();
		this.order_id = order_id;
		this.order_total = order_total;
		
	}
	
	
	

}
