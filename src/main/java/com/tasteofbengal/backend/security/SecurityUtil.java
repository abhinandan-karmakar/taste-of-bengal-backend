package com.tasteofbengal.backend.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {
	public Integer getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		return Integer.parseInt(authentication.getName());
	}
}
