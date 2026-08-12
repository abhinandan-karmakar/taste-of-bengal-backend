package com.tasteofbengal.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tasteofbengal.backend.dto.response.ProductResponse;
import com.tasteofbengal.backend.exception.ResourceNotFoundException;
import com.tasteofbengal.backend.model.Product;
import com.tasteofbengal.backend.repo.ProductRepo;

@Service
public class ProductService {

	@Autowired
	private ProductRepo productRepo;

	public List<ProductResponse> getAllProducts() {
		List<Product> products = productRepo.findByIsActiveTrue();
		List<ProductResponse> productsResponse = new ArrayList<>();
		for (Product product : products) {
			productsResponse.add(mapToProductResponse(product));
			}
			
			return productsResponse;

	}

	public ProductResponse getProductById(Integer id) {
		
		Product product = productRepo.findById(id).orElseThrow(() -> {
			throw new ResourceNotFoundException("Product not found with id : " + id);
		});

		ProductResponse productResponse = mapToProductResponse(product);
		
		return productResponse;
	}

	public List<ProductResponse> searchProducts(String keyword) {
		List<Product> products = productRepo.searchByKeyword(keyword);
		List<ProductResponse> productResponses = new ArrayList<>();
		for (Product product : products) {
			productResponses.add(mapToProductResponse(product));
		}
		return productResponses;
	}

	private ProductResponse mapToProductResponse(Product product) {
		return new ProductResponse(product.getId(), product.getName(), product.getPrice(),
				product.getCategory().getName(), product.getDescription(), product.getImage_url(),
				product.getAvailableStock());
	}

}
