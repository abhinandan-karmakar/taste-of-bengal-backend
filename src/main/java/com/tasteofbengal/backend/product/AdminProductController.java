package com.tasteofbengal.backend.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/v1/admin/products")
public class AdminProductController {

	@Autowired
	private AdminProductService adminProductService;

	@GetMapping("")
	public ResponseEntity<List<AdminProductResponse>> getAllProducts() {

		return ResponseEntity.ok(adminProductService.getAllProducts());
	}

	@GetMapping("/{id}")
	public ResponseEntity<AdminProductResponse> getProductById(@PathVariable Integer id) {
		return ResponseEntity.status(HttpStatus.OK).body(adminProductService.getProductById(id));
	}

	@GetMapping("/count-total")
	public ResponseEntity<ProductCountResponse> getTotalProductCount() {
		return ResponseEntity.status(HttpStatus.OK).body(adminProductService.getTotalProductCount());
	}

	@GetMapping("/count-active")
	public ResponseEntity<ProductCountResponse> getActiveProductCount() {
		return ResponseEntity.status(HttpStatus.OK).body(adminProductService.getActiveProductCount());
	}

	@PostMapping("")
	public ResponseEntity<String> addProduct(@RequestBody AdminProductRequest productRequest) {

		return ResponseEntity.status(HttpStatus.CREATED).body(adminProductService.addProduct(productRequest));
	}

	@PutMapping("/{id}")
	public ResponseEntity<String> updateProduct(@PathVariable Integer id,
			@RequestBody AdminProductRequest productRequest) {

		return ResponseEntity.status(HttpStatus.OK).body(adminProductService.updateProduct(id, productRequest));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable Integer id) {
		return ResponseEntity.status(HttpStatus.OK).body(adminProductService.deleteProduct(id));
	}

}
