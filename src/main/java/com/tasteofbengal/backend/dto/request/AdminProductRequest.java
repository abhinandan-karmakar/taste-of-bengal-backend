package com.tasteofbengal.backend.dto.request;

public class AdminProductRequest {
	private String name;

	private int price;

	private String category;

	private String description;

	private String imageUrl;

	private int availableStock;

	private boolean isActive;

	public AdminProductRequest() {
		super();
	}

	public AdminProductRequest(String name, int price, String category, String description, String image_url,
			int availableStock, boolean isActive) {
		super();
		this.name = name;
		this.price = price;
		this.category = category;
		this.description = description;
		this.imageUrl = image_url;
		this.availableStock = availableStock;
		this.isActive = isActive;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String image_url) {
		this.imageUrl = image_url;
	}

	public int getAvailableStock() {
		return availableStock;
	}

	public void setAvailableStock(int availableStock) {
		this.availableStock = availableStock;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

}
