package com.tasteofbengal.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tasteofbengal.backend.model.Category;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Integer> {

	Category findByName(String name);

}
