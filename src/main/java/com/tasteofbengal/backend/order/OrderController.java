package com.tasteofbengal.backend.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tasteofbengal.backend.payment.OrderPaymentResponse;




@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@PostMapping("")
	public ResponseEntity<OrderPaymentResponse> createOrder(@RequestBody OrderRequest orderRequest) {
		
		return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(orderRequest));
	}

	@PostMapping("/cod")
	public ResponseEntity<String> createOrderOfCOD(@RequestBody OrderRequest orderRequest) {

		return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrderOfCOD(orderRequest));
	}

	@GetMapping("")
	public ResponseEntity<List<OrderResponse>> getAllOrders() {
		return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllOrders());
	}

	@GetMapping("/{id}")
	public ResponseEntity<OrderResponse> getOrder(@PathVariable Integer id) {
		return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrder(id));
	}

	@PostMapping("/cancel/{id}")
	public ResponseEntity<String> cancelOrder(@PathVariable Integer id) {

		return ResponseEntity.status(HttpStatus.OK).body(orderService.cancelOrder(id));
	}


}
