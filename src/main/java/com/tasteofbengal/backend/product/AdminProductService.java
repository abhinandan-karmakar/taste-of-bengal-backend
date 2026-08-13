package com.tasteofbengal.backend.product;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tasteofbengal.backend.cart.CartItemRepo;
import com.tasteofbengal.backend.category.Category;
import com.tasteofbengal.backend.category.CategoryRepo;
import com.tasteofbengal.backend.exception.ConflictException;
import com.tasteofbengal.backend.exception.ResourceNotFoundException;
import com.tasteofbengal.backend.order.OrderItemRepo;

@Service
public class AdminProductService {

	@Autowired
	private ProductRepo productRepo;

	@Autowired
	private CategoryRepo categoryRepo;
	
	@Autowired
	private  CartItemRepo cartItemRepo;
	
	@Autowired
	private OrderItemRepo orderItemRepo;

	private AdminProductResponse mapToAdminProductResponse(Product product) {
		return new AdminProductResponse(product.getId(), product.getName(), product.getPrice(),
				product.getCategory().getName(), product.getDescription(), product.getImage_url(),
				product.getAvailableStock(), product.isActive(), product.getCreatedAt(), product.getUpdatedAt());
	}

	private Product mapFromAdminProductRequest(Product product, AdminProductRequest productRequest) {

		Category category = categoryRepo.findByNameIgnoreCase(productRequest.getCategory());

		if (category == null) {
			throw new ResourceNotFoundException("No Category not found with name : " + productRequest.getCategory());
		}

		if (category.isActive() == false && productRequest.isActive() == true) {
			throw new ConflictException("Product can not to active because the associated category is in-active");
		}

		product.setName(productRequest.getName());
		product.setPrice(productRequest.getPrice());
		product.setCategory(category);
		product.setDescription(productRequest.getDescription());
		product.setImage_url(productRequest.getImageUrl());
		product.setAvailableStock(productRequest.getAvailableStock());
		product.setActive(productRequest.isActive());

		return product;
	}

	public List<AdminProductResponse> getAllProducts() {
		List<Product> products = productRepo.findAll();
		List<AdminProductResponse> adminProductResponses = new ArrayList<>();
		for (Product product : products) {
			adminProductResponses.add(mapToAdminProductResponse(product));
		}

		return adminProductResponses;
	}

	public AdminProductResponse getProductById(Integer id) {
		Product product = productRepo.findById(id).orElseThrow(() -> {
			throw new ResourceNotFoundException("Product not found with Id : " + id);
		});
		return mapToAdminProductResponse(product);
	}

	public String addProduct(AdminProductRequest productRequest) {
		Product product = mapFromAdminProductRequest(new Product(), productRequest);
		productRepo.save(product);
		return "Product Created Successfully";
	}

	public String updateProduct(Integer id, AdminProductRequest productRequest) {
		Product product = productRepo.findById(id).orElseThrow(() -> {
			throw new ResourceNotFoundException("No product found with Id : " + id);
		});


		product = mapFromAdminProductRequest(product, productRequest);
		productRepo.save(product);
		return "Product Updated Successfully";
	}

	public String deleteProduct(Integer id) {
		
		productRepo.findById(id).orElseThrow(() -> {
			throw new ResourceNotFoundException("Product does not exist with Id : " + id);
		});

		if(cartItemRepo.existsByProductId(id)) {
			throw new ConflictException("Cannot be deleted because because product exist in a cart");
		}
		
		if(orderItemRepo.existsByProductId(id)) {
			throw new ConflictException("Cannot be deleted because product exist in ordered item");
		}
		
		productRepo.deleteById(id);

		return "Product deleted successfully";
	}

}
