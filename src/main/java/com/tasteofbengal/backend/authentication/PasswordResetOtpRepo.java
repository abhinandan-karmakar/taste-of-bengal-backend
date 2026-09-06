package com.tasteofbengal.backend.authentication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetOtpRepo extends JpaRepository<PasswordResetOtp, Integer> {

	PasswordResetOtp findByEmail(String email);

}
