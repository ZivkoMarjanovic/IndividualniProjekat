package com.tus.individual.project.model;

import javax.persistence.*;

@Entity
@Table(name="user")
public class User {
	@Id
	@ GeneratedValue ( strategy = GenerationType.IDENTITY)
	@Column (name="id")
	private long id;
	
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

	public User() {
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getLast_name() {
		return last_name;
	}

	public String getE_mail() {
		return e_mail;
	}

	public String getAddress() {
		return address;
	}

	public String getType() {
		return type;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public String getPassword() {
		return password;
	}
}
