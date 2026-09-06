package com.tasteofbengal.backend.authentication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepo extends JpaRepository<PasswordResetToken, Integer> {
	PasswordResetToken findByEmail(String email);

	PasswordResetToken findByToken(String token);
}
