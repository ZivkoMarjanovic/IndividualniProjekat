package com.tus.individual.project.dto;

public class OrderSaleDto {
	
	private String total;
	
	private String number;
	
	private String day;

	public OrderSaleDto() {
	}

	public OrderSaleDto(String total, String number, String day) {
		super();
		this.total = total;
		this.number = number;
		this.day = day;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}
}
