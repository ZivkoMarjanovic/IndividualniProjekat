package com.tus.model;

import javax.persistence.Entity;

import java.util.List;

import javax.persistence.*;

@Entity
@Table (name="users")
public class User {
	@Id
	@ GeneratedValue ( strategy = GenerationType.IDENTITY)
	@Column (name="user_id")
	private long user_id;
	
	@Column (name="name")
	private String name;
	
	@Column (name="last_name")
	private String last_name;
	
	@Column (name="e_mail")
	private String e_mail;
	
	@Column (name="address")
	private String address;
	
	@Column (name="user_type")
	private String type;
	
	@Column (name="phone_number")
	private String phone_number;
	
	@Column (name="password")
	private String password;
	
	@OneToMany (cascade = CascadeType.ALL)
	@JoinColumn (name = "orders_id")
	private List <Order> orders;
	
	
	public List<Order> getOrders() {
		return orders;
	}
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
	public User(long user_id, String name, String last_name, String e_mail, String address, String type, String phone_number,
			String password) {
		super();
		this.user_id = user_id;
		this.name = name;
		this.last_name = last_name;
		this.e_mail = e_mail;
		this.address = address;
		this.type = type;
		this.phone_number = phone_number;
		this.password = password;
	}
	public User () {
		
	}
	
	public long getId() {
		return user_id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getE_mail() {
		return e_mail;
	}
	public void setE_mail(String e_mail) {
		this.e_mail = e_mail;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	

}
