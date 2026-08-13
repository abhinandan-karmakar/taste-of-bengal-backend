package com.tasteofbengal.backend.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/auth/")
public class AuthenticationController {

	@Autowired
	private AuthenticationService authenticationService;

	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody RegisterUserRequest userRequest) {

		return ResponseEntity.status(HttpStatus.OK).body(authenticationService.registerUser(userRequest));
	}

	@PostMapping("/login")
	public ResponseEntity<String> loginUser(@RequestBody LoginUserRequest loginRequest) {

		return ResponseEntity.status(HttpStatus.OK).body(authenticationService.loginUser(loginRequest));
	}

}
