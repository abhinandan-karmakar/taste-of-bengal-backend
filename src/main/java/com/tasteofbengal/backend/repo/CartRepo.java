package com.tasteofbengal.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tasteofbengal.backend.model.Cart;

@Repository
public interface CartRepo extends JpaRepository<Cart, Integer> {

}
