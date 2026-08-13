package com.tasteofbengal.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.tasteofbengal.backend.exception.ResourceNotFoundException;
import com.tasteofbengal.backend.user.User;
import com.tasteofbengal.backend.user.UserRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) {

		User user = userRepo.findByEmail(username);

		if (user == null) {
			throw new ResourceNotFoundException("No user exist the email : " + username);
		}

		return new CustomUserDetails(user);
	}

}
