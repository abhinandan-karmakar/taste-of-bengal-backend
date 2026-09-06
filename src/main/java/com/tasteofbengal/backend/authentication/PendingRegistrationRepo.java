package com.tasteofbengal.backend.authentication;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PendingRegistrationRepo extends JpaRepository<PendingRegistration, Integer> {

	Optional<PendingRegistration> findByEmail(String email);

	void deleteByEmail(String email);

}
