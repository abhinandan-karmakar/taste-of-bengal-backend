package com.tasteofbengal.backend.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminOrderService {

	@Autowired
	private OrderRepo orderRepo;

	public OrderCountResponse getTotalOrderCount() {

		long totalOrder = orderRepo.count();

		return new OrderCountResponse(totalOrder);
	}

	public OrderCountResponse getPendingOrderCount() {

		long totalPendingOrder = orderRepo
				.countByOrderStatusNotIn(
						List.of(OrderStatus.PAID, OrderStatus.CASH_ON_DELIVERY, OrderStatus.CANCELLED));

		return new OrderCountResponse(totalPendingOrder);
	}

	public TotalRevenueResponse getTotalRevenue() {

		long totalRevenue = orderRepo
				.sumTotalAmountByOrderStatusIn(List.of(OrderStatus.PAID, OrderStatus.CASH_ON_DELIVERY));

		return new TotalRevenueResponse(totalRevenue);
	}

	public List<RecentOrderResponse> getRecentOrders() {

		List<Order> recentOrders = orderRepo.findTop5ByOrderByOrderedAtDesc();

		return recentOrders.stream()
				.map(recentOrder -> new RecentOrderResponse(recentOrder.getId(), recentOrder.getUser().getName(),
						recentOrder.getOrderStatus().toString(), recentOrder.getOrderedAt(),
						recentOrder.getTotalAmount()))
				.toList();

	}

	public List<AdminOrderResponse> getAllOrders() {

		List<Order> orders = orderRepo.findAllByOrderByOrderedAtDesc();

		return orders.stream()
				.map(order -> new AdminOrderResponse(order.getId(), order.getUser().getName(),
						order.getUser().getEmail(), order.getTotalAmount(), order.getOrderStatus().toString(),
						order.getOrderedAt()))
				.toList();

	}

}
