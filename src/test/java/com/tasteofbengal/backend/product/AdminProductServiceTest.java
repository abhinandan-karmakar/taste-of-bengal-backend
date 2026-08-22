package com.tasteofbengal.backend.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tasteofbengal.backend.category.Category;
import com.tasteofbengal.backend.category.CategoryRepo;

@ExtendWith(MockitoExtension.class)
class AdminProductServiceTest {

	@Mock
	private ProductRepo productRepo;

	@Mock
	private CategoryRepo categoryRepo;

	@InjectMocks
	private AdminProductService adminProductService;

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
		product1.setCreatedAt(LocalDateTime.now());
		product1.setUpdatedAt(LocalDateTime.now());

		product2 = new Product();
		product2.setId(2);
		product2.setName("Spicy Chanachur");
		product2.setPrice(120);
		product2.setCategory(category);
		product2.setDescription("Spicy Bengali chanachur");
		product2.setImage_url("spicy-chanachur.jpg");
		product2.setAvailableStock(30);
		product2.setActive(true);
		product1.setCreatedAt(LocalDateTime.now());
		product1.setUpdatedAt(LocalDateTime.now());
		

	}

	@Test
	void shouldReturnAllTheProducts() {
		
		when(productRepo.findAll()).thenReturn(List.of(product1, product2));

		List<AdminProductResponse> result = adminProductService.getAllProducts();

		assertFalse(result.isEmpty());
		assertEquals(100, result.get(0).getPrice());
		
		verify(productRepo, times(1)).findAll();

	}

	@Test
	void shouldReturnEmptyListWhenNoProductExists() {
		when(productRepo.findAll()).thenReturn(List.of());
		
		List<AdminProductResponse> result = adminProductService.getAllProducts();
		
		assertEquals(List.of(), result);
		assertTrue(result.isEmpty());
		
		verify(productRepo, times(1)).findAll();
	}

	@Test
	void shouldAddTheProduct() {
		AdminProductRequest adminProductRequest = new AdminProductRequest("Sweet Chanachur", 130, "Chanachur",
				"Traditional Bengali chanachur", "bengali-chanachur.jpg", 30, true);

		Category category = new Category();
		category.setId(1);
		category.setName("Chanachur");

		when(categoryRepo.findByNameIgnoreCase("Chanachur")).thenReturn(category);

		String result = adminProductService.addProduct(adminProductRequest);

		assertEquals("Product Created Successfully", result);

		ArgumentCaptor<Product> productCapture = ArgumentCaptor.forClass(Product.class);

		verify(productRepo, times(1)).save(productCapture.capture());

		Product savedValue = productCapture.getValue();

		assertEquals("Sweet Chanachur", savedValue.getName());

	}

}
