package com.tasteofbengal.backend.authentication;

public class ForgetPasswordRequest {
	private String email;

	public ForgetPasswordRequest() {
		super();
	}

	public ForgetPasswordRequest(String email) {
		super();
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
