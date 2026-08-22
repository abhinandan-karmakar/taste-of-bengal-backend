package com.tasteofbengal.backend.cart;

public class CartItemResponse {
	private Integer productId;

	private String productName;

	private int productPrice;

	private String ProductCategory;

	private String productDescription;

	private String productImageUrl;

	private int productQuantity;

	public CartItemResponse() {
		super();
	}

	public CartItemResponse(Integer productId, String productName, int productPrice, String productCategory,
			String productDescription, String productImageUrl, int productQuantity) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.productPrice = productPrice;
		ProductCategory = productCategory;
		this.productDescription = productDescription;
		this.productImageUrl = productImageUrl;
		this.productQuantity = productQuantity;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(int productPrice) {
		this.productPrice = productPrice;
	}

	public String getProductCategory() {
		return ProductCategory;
	}

	public void setProductCategory(String productCategory) {
		ProductCategory = productCategory;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getProductImageUrl() {
		return productImageUrl;
	}

	public void setProductImageUrl(String productImageUrl) {
		this.productImageUrl = productImageUrl;
	}

	public int getProductQuantity() {
		return productQuantity;
	}

	public void setProductQuantity(int productQuantity) {
		this.productQuantity = productQuantity;
	}

}
