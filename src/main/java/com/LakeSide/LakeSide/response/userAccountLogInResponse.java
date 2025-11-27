package com.LakeSide.LakeSide.response;

public class userAccountLogInResponse {
	
	private String fullName;

	private String email;
	
	private String password;
	
	private String token;

    public userAccountLogInResponse(String fullName, String email, String token) {
        super();
        this.fullName=fullName;
        this.email=email;
        this.token=token;
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

	public userAccountLogInResponse() {
		super();
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	
}
