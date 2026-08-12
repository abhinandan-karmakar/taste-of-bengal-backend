package com.tasteofbengal.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tasteofbengal.backend.dto.response.CategoryResponse;
import com.tasteofbengal.backend.model.Category;
import com.tasteofbengal.backend.repo.CategoryRepo;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepo categoryRepo;

	private CategoryResponse mapToCategoryResponse(Category category) {
		return new CategoryResponse(category.getId(), category.getName(), category.getDescription());
	}

	public List<CategoryResponse> getAllCategories() {

		List<Category> categories = categoryRepo.findByIsActiveTrue();

		List<CategoryResponse> categoryResponses = new ArrayList<>();

		for (Category category : categories) {
			categoryResponses.add(mapToCategoryResponse(category));
		}

		return categoryResponses;
	}

}
