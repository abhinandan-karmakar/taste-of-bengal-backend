package com.tasteofbengal.backend.order;

public class OrderCountResponse {
	private long totalOrders;

	public OrderCountResponse() {
		super();
	}

	public OrderCountResponse(long totalOrders) {
		super();
		this.totalOrders = totalOrders;
	}

	public long getTotalOrders() {
		return totalOrders;
	}

	public void setTotalOrders(long totalOrders) {
		this.totalOrders = totalOrders;
	}

}
