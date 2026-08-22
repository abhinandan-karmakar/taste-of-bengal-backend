package com.tasteofbengal.backend.cart;

public class UpdateCartRequest {
	private int quantity;

	public UpdateCartRequest() {
		super();
	}

	public UpdateCartRequest(int quantity) {
		super();
		this.quantity = quantity;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
