package com.tasteofbengal.backend.category;

public class AdminCategoryRequest {
	private String name;

	private String description;

	private boolean isActive;

	public AdminCategoryRequest() {
		super();
	}

	public AdminCategoryRequest(String name, String description, boolean isActive) {
		super();
		this.name = name;
		this.description = description;
		this.isActive = isActive;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

}
