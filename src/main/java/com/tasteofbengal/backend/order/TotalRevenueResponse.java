package com.tasteofbengal.backend.order;

public class TotalRevenueResponse {
	private long totalRevenue;

	public TotalRevenueResponse() {
		super();
	}

	public TotalRevenueResponse(long totalRevenue) {
		super();
		this.totalRevenue = totalRevenue;
	}

	public long getTotalRevenue() {
		return totalRevenue;
	}

	public void setTotalRevenue(long totalRevenue) {
		this.totalRevenue = totalRevenue;
	}

}
