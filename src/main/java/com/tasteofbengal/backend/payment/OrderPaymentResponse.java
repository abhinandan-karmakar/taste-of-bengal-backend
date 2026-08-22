package com.tasteofbengal.backend.payment;

public class OrderPaymentResponse {

	private Integer orderId;
	private Integer amount;
	private String currency;
	private String razorpayOrderId;

	public OrderPaymentResponse(Integer orderId, Integer amount, String currency, String razorpayOrderId) {

		this.orderId = orderId;
		this.amount = amount;
		this.currency = currency;
		this.razorpayOrderId = razorpayOrderId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getRazorpayOrderId() {
		return razorpayOrderId;
	}

	public void setRazorpayOrderId(String razorpayOrderId) {
		this.razorpayOrderId = razorpayOrderId;
	}

}
