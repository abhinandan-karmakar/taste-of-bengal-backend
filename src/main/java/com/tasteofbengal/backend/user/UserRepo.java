package com.tasteofbengal.backend.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

	User findByEmail(String email);

	User findByPhone(String phone);
}
