package com.tasteofbengal.backend.authentication;

public class ForgetPasswordTokenResponse {
	private String token;

	public ForgetPasswordTokenResponse() {
		super();
	}

	public ForgetPasswordTokenResponse(String token) {
		super();
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
