package com.tasteofbengal.backend.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/v1/admin/orders")
public class AdminOrderController {

	@Autowired
	private AdminOrderService adminOrderService;

	@GetMapping("/count-total")
	public ResponseEntity<OrderCountResponse> getTotalOrderCount() {
		return ResponseEntity.status(HttpStatus.OK).body(adminOrderService.getTotalOrderCount());
	}

	@GetMapping("/count-pending")
	public ResponseEntity<OrderCountResponse> getPendingOrderCount() {
		return ResponseEntity.status(HttpStatus.OK).body(adminOrderService.getPendingOrderCount());
	}

	@GetMapping("/total-revenue")
	public ResponseEntity<TotalRevenueResponse> getTotalRevenue() {
		return ResponseEntity.status(HttpStatus.OK).body(adminOrderService.getTotalRevenue());
	}

	@GetMapping("/recent")
	public ResponseEntity<List<RecentOrderResponse>> getRecentOrders() {
		return ResponseEntity.status(HttpStatus.OK).body(adminOrderService.getRecentOrders());
	}

	@GetMapping("")
	public ResponseEntity<List<AdminOrderResponse>> getAllOrders() {
		return ResponseEntity.status(HttpStatus.OK).body(adminOrderService.getAllOrders());
	}

}
