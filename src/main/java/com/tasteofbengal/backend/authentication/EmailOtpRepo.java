package com.tasteofbengal.backend.authentication;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailOtpRepo extends JpaRepository<EmailOtp, Integer> {

	Optional<EmailOtp> findByEmail(String email);

	void deleteByEmail(String email);

}
