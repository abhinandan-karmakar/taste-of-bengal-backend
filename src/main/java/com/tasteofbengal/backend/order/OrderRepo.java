package com.tasteofbengal.backend.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<Order, Integer> {

	List<Order> findByUserId(Integer userId);

	List<Order> findByUserIdAndOrderStatusInOrderByOrderedAtDesc(Integer userId, List<OrderStatus> statuses);

	long countByOrderStatusNotIn(List<OrderStatus> statuses);

	@Query("""
			    SELECT COALESCE(SUM(o.totalAmount), 0)
			    FROM Order o
			    WHERE o.orderStatus IN :statuses
			""")
	long sumTotalAmountByOrderStatusIn(@Param("statuses") List<OrderStatus> statuses);

	List<Order> findTop5ByOrderByOrderedAtDesc();

	List<Order> findAllByOrderByOrderedAtDesc();

}