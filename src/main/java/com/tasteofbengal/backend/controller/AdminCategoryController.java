package com.tasteofbengal.backend.controller;

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

import com.tasteofbengal.backend.dto.request.AdminCategoryRequest;
import com.tasteofbengal.backend.dto.response.AdminCategoryResponse;
import com.tasteofbengal.backend.service.AdminCategoryService;



@RestController
@RequestMapping("/api/v1/admin/categories")
public class AdminCategoryController {

	@Autowired
	private AdminCategoryService adminCategoryService;

	@GetMapping("")
	public ResponseEntity<List<AdminCategoryResponse>> getAllCategories() {
		return ResponseEntity.status(HttpStatus.OK).body(adminCategoryService.getAllCategories());
	}

	@PostMapping("")
	public ResponseEntity<String> addCategory(@RequestBody AdminCategoryRequest categoryRequest) {

		return ResponseEntity.status(HttpStatus.CREATED).body(adminCategoryService.addCategory(categoryRequest));
	}

	@PutMapping("/{id}")
	public ResponseEntity<String> updateCategory(@PathVariable Integer id,
			@RequestBody AdminCategoryRequest categoryRequest) {

		return ResponseEntity.status(HttpStatus.OK).body(adminCategoryService.updateCategory(id, categoryRequest));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCategory(@PathVariable Integer id) {
		return ResponseEntity.status(HttpStatus.OK).body(adminCategoryService.deleteCategory(id));
	}

}
