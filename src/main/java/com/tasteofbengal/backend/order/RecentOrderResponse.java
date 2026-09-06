package com.tasteofbengal.backend.order;

import java.time.LocalDateTime;

public class RecentOrderResponse {
	private Integer id;
	private String customerName;
	private String orderStatus;
	private LocalDateTime orderedAt;
	private int totalAmount;

	public RecentOrderResponse() {
		super();
	}

	public RecentOrderResponse(Integer id, String customerName, String orderStatus, LocalDateTime orderedAt,
			int totalAmount) {
		super();
		this.id = id;
		this.customerName = customerName;
		this.orderStatus = orderStatus;
		this.orderedAt = orderedAt;
		this.totalAmount = totalAmount;
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

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public LocalDateTime getOrderedAt() {
		return orderedAt;
	}

	public void setOrderedAt(LocalDateTime orderedAt) {
		this.orderedAt = orderedAt;
	}

	public int getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}

}
