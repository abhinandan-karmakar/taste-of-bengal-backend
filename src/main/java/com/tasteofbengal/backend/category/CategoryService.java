package com.tasteofbengal.backend.category;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
