package com.tasteofbengal.backend.cart;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem, Integer> {

	boolean existsByProductId(Integer id);

	List<CartItem> findByCartId(Integer id);

	CartItem findByProductIdAndCartId(Integer productId, Integer cartId);

	boolean existsByProductIdAndCartId(Integer productId, Integer cartId);

}
