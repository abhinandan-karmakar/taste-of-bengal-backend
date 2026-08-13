package com.tasteofbengal.backend.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {

	List<Product> findByIsActiveTrue();

	@Query("""
			    SELECT p
			    FROM Product p
			    JOIN p.category c
			    WHERE p.isActive = true
			      AND (
			          LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
			          OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
			          OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
			          OR LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
			      )
			""")
	List<Product> searchByKeyword(String keyword);

	boolean existsByCategoryId(Integer id);

	boolean existsByCategoryIdAndIsActiveTrue(Integer id);
}
