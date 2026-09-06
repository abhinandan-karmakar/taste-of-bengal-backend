package com.tasteofbengal.backend.order;

import java.time.LocalDateTime;

public class AdminOrderResponse {
	private Integer id;
	private String customerName;
	private String customerEmail;
	private int totalAmount;
	private String orderStatus;
	private LocalDateTime orderTime;

	public AdminOrderResponse() {
		super();
	}

	public AdminOrderResponse(Integer id, String customerName, String customerEmail, int totalAmount,
			String orderStatus, LocalDateTime orderTime) {
		super();
		this.id = id;
		this.customerName = customerName;
		this.customerEmail = customerEmail;
		this.totalAmount = totalAmount;
		this.orderStatus = orderStatus;
		this.orderTime = orderTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public int getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public LocalDateTime getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(LocalDateTime orderTime) {
		this.orderTime = orderTime;
	}

}
