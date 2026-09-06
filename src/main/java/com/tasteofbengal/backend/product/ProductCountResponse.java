package com.tasteofbengal.backend.product;

public class ProductCountResponse {
	private long totalProducts;

	public ProductCountResponse() {
		super();
	}

	public ProductCountResponse(long totalProducts) {
		super();
		this.totalProducts = totalProducts;
	}

	public long getTotalProducts() {
		return totalProducts;
	}

	public void setTotalProducts(long totalProducts) {
		this.totalProducts = totalProducts;
	}

}
