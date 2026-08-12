package com.tasteofbengal.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tasteofbengal.backend.model.CartItem;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem, Integer> {

	boolean existsByProductId(Integer id);

}
