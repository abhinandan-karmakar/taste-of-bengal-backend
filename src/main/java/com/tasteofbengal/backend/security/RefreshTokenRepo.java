package com.tasteofbengal.backend.security;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tasteofbengal.backend.user.User;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Integer> {
	Optional<RefreshToken> findByTokenHash(String tokenHash);

	List<RefreshToken> findByUser(User user);

	void deleteByUser(User user);
}
