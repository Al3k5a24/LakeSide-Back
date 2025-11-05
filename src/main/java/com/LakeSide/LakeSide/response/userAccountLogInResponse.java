package com.LakeSide.LakeSide.response;

public class userAccountLogInResponse {

	private String email;
	
	private String password;
	
	private Boolean isActive=false;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public userAccountLogInResponse(String email, String password, Boolean isActive) {
		super();
		this.email = email;
		this.password = password;
		this.isActive = isActive;
	}
	
	public userAccountLogInResponse() {
		super();
	}
	
	
}
