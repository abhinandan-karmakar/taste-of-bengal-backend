package com.tasteofbengal.backend.address;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepo extends JpaRepository<Address, Integer> {

	List<Address> findByUserIdAndIsActiveTrue(Integer userId);

}
