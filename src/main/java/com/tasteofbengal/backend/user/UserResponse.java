package com.tasteofbengal.backend.user;

public class UserResponse {
	private Integer userId;
	private String userName;
	private String role;

	public UserResponse(Integer userId, String userName, String role) {
		this.userId = userId;
		this.userName = userName;
		this.role = role;
	}

	public UserResponse() {
		super();
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
