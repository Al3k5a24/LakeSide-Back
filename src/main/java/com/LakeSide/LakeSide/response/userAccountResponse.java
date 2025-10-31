package com.LakeSide.LakeSide.response;

public class userAccountResponse {

	private Long id;
	
	private String fullName;
	
	private String email;
	
	private String password;
	
	private Boolean isActive=false;
	
	public userAccountResponse() {
		super();
	}

	public userAccountResponse(Long id, String fullName, String email, String password, Boolean isActive) {
		super();
		this.id = id;
		this.fullName = fullName;
		this.email = email;
		this.password = password;
		this.isActive = isActive;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

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
	
	
	
}
