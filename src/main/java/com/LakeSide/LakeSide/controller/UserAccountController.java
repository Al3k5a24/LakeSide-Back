package com.LakeSide.LakeSide.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.LakeSide.LakeSide.model.UserAccount;
import com.LakeSide.LakeSide.response.userAccountLogInResponse;
import com.LakeSide.LakeSide.response.userAccountResponse;
import com.LakeSide.LakeSide.service.IUserAccountService;

import Configuration.AppConfig;
import JWT.JWTService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("/auth")
//CORS policy override for diffrent paths 
@CrossOrigin(origins="http://localhost:5173",allowCredentials = "true") //allowCredentials allows us to send cookies
@RestController
public class UserAccountController {
	
	//added for http-only cookie(jwt storage)
	@Autowired
	private AppConfig properties;
	
	@Autowired
    private JWTService jwtService;

	@Autowired
	private IUserAccountService userService;

	private userAccountLogInResponse getUserLoginResponse(UserAccount user) {
		return new userAccountLogInResponse(
				user.getFullName(),
				user.getEmail(),
				user.getToken());
	}
	
	@PostMapping("/create-account")
	private ResponseEntity<userAccountResponse> createAccount(
			@RequestParam String fullName,
			@RequestParam String email,
			@RequestParam String password){
		userAccountResponse userResponse = userService.createAccount(fullName, email, password);
		return ResponseEntity.ok(userResponse);
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/sign-in")
	private ResponseEntity<Object> LogInExistingAccount(
			@RequestParam String email,
			@RequestParam String password,
			HttpServletResponse response) {
		UserAccount user = userService.SignInExistingAccount(email, password);
		try {
			// Token and logged in status are already set by the service
			userAccountLogInResponse userResponse = getUserLoginResponse(user);
			jwtService.generateCookie(response,userResponse.getToken());
			return ResponseEntity.ok(userResponse);
		} catch (Exception e) {
			return ResponseEntity.status(401).body("Authentication failed: " + e.getMessage());
		}
	}

	@GetMapping("/profile")
	public ResponseEntity<Object> returnUserProfileAfterLog(
			@CookieValue(name="AUTH_TOKEN", required = false)
			String token) {
		//bcs of jwt filter, we can implement reading like this
		//otherwise cookie would need to be read manually
	    if (token == null || token.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body("Authentication missing");
	    }
	    String email = jwtService.extractEmail(token);
	    UserAccount potentialUser = userService.loadUserbyEmail(email);
	    userAccountLogInResponse userResponse = getUserLoginResponse(potentialUser);
	    return ResponseEntity.ok(userResponse);
	}
}
