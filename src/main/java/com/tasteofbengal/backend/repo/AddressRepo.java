package com.tasteofbengal.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tasteofbengal.backend.model.Address;

@Repository
public interface AddressRepo extends JpaRepository<Address, Integer> {

}
