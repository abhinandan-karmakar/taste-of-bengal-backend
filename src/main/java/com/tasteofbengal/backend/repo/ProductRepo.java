package com.tasteofbengal.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tasteofbengal.backend.model.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {

}
