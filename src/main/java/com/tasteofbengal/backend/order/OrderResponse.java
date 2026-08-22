package com.tasteofbengal.backend.order;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

	private Integer id;
	private int totalAmount;
	private String orderStatus;
	private LocalDateTime orderedAt;
	private List<OrderedProductResponse> items;

	public OrderResponse() {
		super();
	}

	public OrderResponse(Integer id, int totalAmount, String orderStatus, LocalDateTime orderedAt,
			List<OrderedProductResponse> items) {
		super();
		this.id = id;
		this.totalAmount = totalAmount;
		this.orderStatus = orderStatus;
		this.orderedAt = orderedAt;
		this.items = items;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public LocalDateTime getOrderedAt() {
		return orderedAt;
	}

	public void setOrderedAt(LocalDateTime orderedAt) {
		this.orderedAt = orderedAt;
	}

	public List<OrderedProductResponse> getItems() {
		return items;
	}

	public void setItems(List<OrderedProductResponse> items) {
		this.items = items;
	}

}
