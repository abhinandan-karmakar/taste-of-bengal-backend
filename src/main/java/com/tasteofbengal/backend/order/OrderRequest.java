package com.tasteofbengal.backend.order;

public class OrderRequest {
	private Integer addressId;

	public OrderRequest() {
		super();
	}

	public OrderRequest(Integer addressId) {
		super();
		this.addressId = addressId;
	}

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

}
