package com.tasteofbengal.backend.address;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tasteofbengal.backend.exception.ResourceNotFoundException;
import com.tasteofbengal.backend.security.SecurityUtil;
import com.tasteofbengal.backend.user.User;
import com.tasteofbengal.backend.user.UserRepo;

@Service
public class AddressService {

	@Autowired
	private AddressRepo addressRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private SecurityUtil securityUtil;

	private AddressResponse mapToAddressResponse(Address address) {
		return new AddressResponse(address.getId(), address.getUser().getId(), address.getAddressName(),
				address.getHouseNo(),
				address.getAddressMain(), address.getLandmark(), address.getCity(), address.getState(),
				address.getPincode(), address.getAddressType().toString());
	}

	private Address mapFromAddressRequest(AddressRequest addressRequest, Address address) {

		User user = userRepo.findById(securityUtil.getCurrentUserId())
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		address.setUser(user);
		address.setAddressName(addressRequest.getAddressName());
		address.setHouseNo(addressRequest.getHouseNo());
		address.setAddressMain(addressRequest.getAddressMain());
		address.setLandmark(addressRequest.getLandmark());
		address.setCity(addressRequest.getCity());
		address.setState(addressRequest.getState());
		address.setPincode(addressRequest.getPincode());

		return address;
	}

	public List<AddressResponse> getAllAddressOfTheUser() {

		Integer userId = securityUtil.getCurrentUserId();

		List<Address> addresses = addressRepo.findByUserIdAndIsActiveTrue(userId);

		List<AddressResponse> responses = new ArrayList<>();

		for (Address address : addresses) {
			responses.add(mapToAddressResponse(address));
		}

		return responses;
	}

	public String addAddress(AddressRequest addressRequest) {

		Address address = mapFromAddressRequest(addressRequest, new Address());

		addressRepo.save(address);

		return "Address Saved Successfully";
	}

	public String deleteAddress(Integer id) {

		Address address = addressRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No address found with the id" + id));

		if (address.getUser().getId() != securityUtil.getCurrentUserId()) {
			throw new ResourceNotFoundException("No address found with the id " + id);
		}

		address.setActive(false);

		addressRepo.save(address);

		return "Address deleted successfully";
	}

}
