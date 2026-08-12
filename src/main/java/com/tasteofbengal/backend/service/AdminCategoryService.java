package com.tasteofbengal.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tasteofbengal.backend.dto.request.AdminCategoryRequest;
import com.tasteofbengal.backend.dto.response.AdminCategoryResponse;
import com.tasteofbengal.backend.exception.ConflictException;
import com.tasteofbengal.backend.exception.ResourceNotFoundException;
import com.tasteofbengal.backend.model.Category;
import com.tasteofbengal.backend.repo.CategoryRepo;
import com.tasteofbengal.backend.repo.ProductRepo;

@Service
public class AdminCategoryService {

	@Autowired
	private ProductRepo productRepo;

	@Autowired
	private CategoryRepo categoryRepo;

	AdminCategoryService(ProductRepo productRepo) {
		this.productRepo = productRepo;
	}

	private AdminCategoryResponse mapToCategoryResponse(Category category) {
		return new AdminCategoryResponse(category.getId(), category.getName(), category.getDescription(),
				category.isActive(), category.getCreatedAt(), category.getUpdatedAt());
	}

	private Category mapFromAdminCategoryRequest(AdminCategoryRequest categoryRequest, Category category) {

		category.setName(categoryRequest.getName());
		category.setDescription(categoryRequest.getDescription());
		category.setActive(categoryRequest.isActive());

		return category;
	}

	public List<AdminCategoryResponse> getAllCategories() {

		List<Category> categories = categoryRepo.findAll();
		List<AdminCategoryResponse> adminCategoryResponses = new ArrayList<>();

		for (Category category : categories) {
			adminCategoryResponses.add(mapToCategoryResponse(category));
		}

		return adminCategoryResponses;
	}


	public String addCategory(AdminCategoryRequest categoryRequest) {

		if (categoryRepo.existsByNameIgnoreCase(categoryRequest.getName())) {
			throw new ConflictException("Category exist with name : " + categoryRequest.getName());
		}

		Category category = new Category();

		category = mapFromAdminCategoryRequest(categoryRequest, category);

		categoryRepo.save(category);

		return "Category created successfully";
	}

	public String updateCategory(Integer id, AdminCategoryRequest categoryRequest) {

		Category category = categoryRepo.findById(id).orElseThrow(() -> {
			throw new ResourceNotFoundException("Category not found by id : " + id);
		});

		if (!category.getName().equalsIgnoreCase(categoryRequest.getName())) {
			if (categoryRepo.existsByNameIgnoreCase(categoryRequest.getName())) {
				throw new ConflictException("Category already exists with the name : " + categoryRequest.getName());
			}
		}

		if (categoryRequest.isActive() == false && category.isActive() == true) {
			if (productRepo.existsByCategoryIdAndIsActiveTrue(category.getId())) {
				throw new ConflictException(
						"Can not make the category in-active because active product exsist associated with the category");
			}
		}

		category = mapFromAdminCategoryRequest(categoryRequest, category);

		categoryRepo.save(category);

		return "Updated the category successfully";
	}

	public String deleteCategory(Integer id) {

		Category category = categoryRepo.findById(id).orElseThrow(() -> {
			throw new ResourceNotFoundException("Category not found with id : " + id);
		});
		if (productRepo.existsByCategoryId(category.getId())) {
			throw new ConflictException("Can not be deleted because product is associated with the category");
		}

		categoryRepo.delete(category);

		return "Category deleted successfully";
	}

}
