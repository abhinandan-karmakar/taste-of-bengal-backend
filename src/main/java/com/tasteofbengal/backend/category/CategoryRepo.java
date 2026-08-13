package com.tasteofbengal.backend.category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Integer> {

	Category findByNameIgnoreCase(String name);

	List<Category> findByIsActiveTrue();

	boolean existsByNameIgnoreCase(String name);


}
