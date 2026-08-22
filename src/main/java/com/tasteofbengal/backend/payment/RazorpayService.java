package com.tasteofbengal.backend.payment;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

@Service
public class RazorpayService {

	@Value("${razorpay.key.id}")
	private String keyId;

	@Value("${razorpay.key.secret}")
	private String keySecret;

	public String createRazorpayOrder(int amount) {

		try {
			RazorpayClient razorpayClient = new RazorpayClient(keyId, keySecret);

			JSONObject orderRequest = new JSONObject();

			orderRequest.put("amount", amount);
			orderRequest.put("currency", "INR");
			orderRequest.put("receipt", "receipt_" + System.currentTimeMillis());

			com.razorpay.Order razorpayOrder = razorpayClient.orders.create(orderRequest);

			return razorpayOrder.get("id");

		} catch (RazorpayException e) {
			throw new RuntimeException("Failed to create Razorpay order", e);
		}
	}

	public boolean verifySignature(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) {
		try {

			JSONObject attributes = new JSONObject();

			attributes.put("razorpay_order_id", razorpayOrderId);
			attributes.put("razorpay_payment_id", razorpayPaymentId);
			attributes.put("razorpay_signature", razorpaySignature);

			return Utils.verifyPaymentSignature(attributes, keySecret);

		} catch (RazorpayException e) {
			throw new RuntimeException("Failed to verify Razorpay payment signature", e);
		}
	}
}