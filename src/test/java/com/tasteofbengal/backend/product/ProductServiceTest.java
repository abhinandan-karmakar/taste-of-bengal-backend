package com.tasteofbengal.backend.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tasteofbengal.backend.category.Category;
import com.tasteofbengal.backend.exception.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@Mock
	private ProductRepo productRepo;

	@InjectMocks
	private ProductService productService;

	private Product product1;
	private Product product2;

	@BeforeEach
	void setup() {
		Category category = new Category();
		category.setId(1);
		category.setName("Chanachur");

		product1 = new Product();
		product1.setId(1);
		product1.setName("Bengali Chanachur");
		product1.setPrice(100);
		product1.setCategory(category);
		product1.setDescription("Traditional Bengali chanachur");
		product1.setImage_url("bengali-chanachur.jpg");
		product1.setAvailableStock(50);
		product1.setActive(true);

		product2 = new Product();
		product2.setId(2);
		product2.setName("Spicy Chanachur");
		product2.setPrice(120);
		product2.setCategory(category);
		product2.setDescription("Spicy Bengali chanachur");
		product2.setImage_url("spicy-chanachur.jpg");
		product2.setAvailableStock(30);
		product2.setActive(true);
	}

	@Test
	void shouldReturnAllActiveProductsForUsers() {

		List<Product> products = List.of(product1, product2);

		when(productRepo.findByIsActiveTrue()).thenReturn(products);

		// Act
		List<ProductResponse> result = productService.getAllProducts();

		// Assert
		assertEquals(2, result.size());
		assertEquals("Spicy Chanachur", result.get(1).getName());
		assertEquals("Chanachur", result.get(1).getCategory());

		// verify
		verify(productRepo, times(1)).findByIsActiveTrue();
	}

	@Test
	void shouldReturnEmptyListwhenNoActiveProductsExistsForUsers() {
		List<Product> products = new ArrayList<>();
		when(productRepo.findByIsActiveTrue()).thenReturn(products);

		List<ProductResponse> result = productService.getAllProducts();

		assertTrue(result.isEmpty());
		assertNotNull(result);

		verify(productRepo, times(1)).findByIsActiveTrue();

	}

	@Test
	void shouldReturnTheProductWhenProductExist() {

		when(productRepo.findById(1)).thenReturn(Optional.of(product1));

		ProductResponse result = productService.getProductById(1);

		assertEquals(1, result.getId());

		verify(productRepo, times(1)).findById(1);
	}

	@Test
	void shouldThrowExceptionWhenProductDoesNotExists() {
		Integer productId = 10;
		
		when(productRepo.findById(productId)).thenReturn(Optional.empty());
		
		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
				() -> productService.getProductById(productId));

		assertEquals("Product not found with id : 10", exception.getMessage());

		verify(productRepo, times(1)).findById(productId);
	}

	@Test
	void shouldReturnListOfProductsIfTheKeywordExistsInNameOrCategoryOfProductOrCategory() {
		when(productRepo.searchByKeyword("Spicy")).thenReturn(List.of(product1, product2));
		
		List<ProductResponse> result = productService.searchProducts("Spicy");
		
		assertEquals(2, result.size());
		
		verify(productRepo, times(1)).searchByKeyword("Spicy");
	}

	@Test
	void shouldReturnEmptyListWhenTheKeywordDoesNotExistsInNameOrCategoryOfProductOrCategory() {
		when(productRepo.searchByKeyword("Snacks")).thenReturn(List.of());
		
		List<ProductResponse> result = productService.searchProducts("Snacks");
		
		assertTrue(result.isEmpty());
		
		verify(productRepo, times(1)).searchByKeyword("Snacks");
	}

}
