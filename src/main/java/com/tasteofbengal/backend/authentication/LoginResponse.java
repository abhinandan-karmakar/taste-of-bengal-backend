package com.tasteofbengal.backend.authentication;

public class LoginResponse {

	private String accessToken;
	private String refreshToken;

	public LoginResponse(String accessToken, String refreshToken) {
		super();
		this.refreshToken = refreshToken;
		this.accessToken = accessToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshtoken) {
		this.refreshToken = refreshtoken;
	}

}
