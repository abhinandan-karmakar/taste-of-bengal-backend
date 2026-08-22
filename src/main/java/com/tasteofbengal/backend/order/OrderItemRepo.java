package com.tasteofbengal.backend.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem, Integer> {

	boolean existsByProductId(Integer id);

	List<OrderItem> findByOrderId(int orderId);

}
