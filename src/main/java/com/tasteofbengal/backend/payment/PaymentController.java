package com.tasteofbengal.backend.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	@PostMapping("/verify")
	public ResponseEntity<String> verifyPayment(@RequestBody PaymentVerifyRequest paymentVerifyRequest) {

		return ResponseEntity.status(HttpStatus.OK).body(paymentService.verifyPayment(paymentVerifyRequest));
	}

}