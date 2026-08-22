package com.tasteofbengal.backend.authentication;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tasteofbengal.backend.security.RefreshTokenService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/api/v1/auth/")
public class AuthenticationController {

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private RefreshTokenService refreshTokenService;

	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody RegisterUserRequest userRequest) {

		return ResponseEntity.status(HttpStatus.OK).body(authenticationService.registerUser(userRequest));
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {

		LoginResponse response = authenticationService.loginUser(loginRequest, request);

		// .secure(true) -> For production.
		ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", response.getRefreshToken()).httpOnly(true)
				.secure(false).sameSite("Strict").path("/api/v1/auth").maxAge(Duration.ofDays(7)).build();

		response.setRefreshToken(null);

		return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
				.body(response);
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout(@CookieValue("refreshToken") String refreshToken,
			HttpServletResponse response) {
		refreshTokenService.revokeRefreshToken(refreshToken);

		ResponseCookie cookie = ResponseCookie.from("refreshToken", "").httpOnly(true).secure(false) // true in
																										// production
				.sameSite("Strict").path("/api/v1/auth").maxAge(0).build();

		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body("Logout successful");
	}

	@PostMapping("/refresh")
	public ResponseEntity<LoginResponse> refreshToken(@CookieValue("refreshToken") String refreshToken,
			HttpServletRequest request) {

		LoginResponse response = refreshTokenService.refreshAccessToken(refreshToken, request);

		ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", response.getRefreshToken()).httpOnly(true)
				.secure(false) // true in production
				.sameSite("Strict").path("/api/v1/auth").maxAge(Duration.ofDays(7)).build();

		response.setRefreshToken(null);

		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, refreshCookie.toString()).body(response);
	}

}
