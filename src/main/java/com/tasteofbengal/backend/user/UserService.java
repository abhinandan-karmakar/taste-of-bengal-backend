package com.tasteofbengal.backend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tasteofbengal.backend.exception.ResourceNotFoundException;

@Service
public class UserService {

	@Autowired
	private UserRepo userRepo;

	private UserResponse mapToUserResponse(User user) {
		return new UserResponse(user.getId(), user.getName(), user.getRole().name());
	}

	public UserResponse getUserById(Integer id) {

		User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("No User Found"));
		return mapToUserResponse(user);
	}

}
