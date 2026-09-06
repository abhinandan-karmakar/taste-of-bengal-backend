package com.tasteofbengal.backend.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {

	List<Product> findByIsActiveTrue();

	List<Product> findByCategoryId(Integer categoryId);

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

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT p FROM Product p WHERE p.id = :id")
	Optional<Product> findByIdForUpdate(@Param("id") Integer id);

	long countByIsActiveTrue();
}
