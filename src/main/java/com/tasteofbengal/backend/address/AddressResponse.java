package com.tasteofbengal.backend.address;

public class AddressResponse {
	private Integer id;
	private Integer userId;
	private String addressName;
	private String houseNo;
	private String addressMain;
	private String landmark;
	private String city;
	private String state;
	private String pincode;
	private String addressType;

	public AddressResponse() {
		super();
	}

	public AddressResponse(Integer id, Integer userId, String addressName, String houseNo, String addressMain,
			String landmark,
			String city, String state, String pincode, String addressType) {
		super();
		this.id = id;
		this.userId = userId;
		this.addressName = addressName;
		this.houseNo = houseNo;
		this.addressMain = addressMain;
		this.landmark = landmark;
		this.city = city;
		this.state = state;
		this.pincode = pincode;
		this.addressType = addressType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getAddressName() {
		return addressName;
	}

	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public String getAddressMain() {
		return addressMain;
	}

	public void setAddressMain(String addressMain) {
		this.addressMain = addressMain;
	}

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

}
