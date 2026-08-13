package com.tasteofbengal.backend.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tasteofbengal.backend.enums.Role;
import com.tasteofbengal.backend.exception.ConflictException;
import com.tasteofbengal.backend.security.CustomUserDetails;
import com.tasteofbengal.backend.security.JwtService;
import com.tasteofbengal.backend.user.User;
import com.tasteofbengal.backend.user.UserRepo;

@Service
public class AuthenticationService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtService jwtService;

	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

	private User mapToUser(RegisterUserRequest userRequest) {
		User user = new User();

		if (userRepo.findByEmail(userRequest.getEmail()) == null) {
			user.setEmail(userRequest.getEmail());
		} else {
			throw new ConflictException("Email already exists");
		}

		if (userRepo.findByPhone(userRequest.getPhone()) == null) {
			user.setPhone(userRequest.getPhone());
		} else {
			throw new ConflictException("Phone number already exists");
		}

		user.setName(userRequest.getName());
		user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
		user.setRole(Role.USER);

		return user;

	}

	public String registerUser(RegisterUserRequest userRequest) {

		User user = mapToUser(userRequest);
		userRepo.save(user);

		return "Registration successful";
	}

	public String loginUser(LoginUserRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		
		return jwtService.generateToken(userDetails);

	}

}
