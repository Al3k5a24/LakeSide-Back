package com.LakeSide.LakeSide.controller;

import java.util.Optional;


import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
import io.jsonwebtoken.Jwt;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.val;
import lombok.extern.java.Log;

@RequestMapping("/auth")
//CORS policy override for diffrent paths 
@CrossOrigin(origins="http://localhost:5173",allowCredentials = "true") //allows us to send cookies
@RestController
public class UserAccountController {
	
	//added for http-only cookie(jwt storage)
	@Autowired
	private AppConfig properties;
	
	@Autowired
    private JWTService authService;

	@Autowired
	private IUserAccountService userService;

	public IUserAccountService getUserService() {
		return userService;
	}

	public void setUserService(IUserAccountService userService) {
		this.userService = userService;
	}
	
	public AppConfig getProperties() {
		return properties;
	}

	public void setProperties(AppConfig properties) {
		this.properties = properties;
	}

	public JWTService getAuthService() {
		return authService;
	}

	public void setAuthService(JWTService authService) {
		this.authService = authService;
	}
	
	//for login
	private userAccountLogInResponse getUserLoginResponse(UserAccount user) {
		return new userAccountLogInResponse(
				user.getFullName(),
				user.getEmail(),
				null, // Never return password
				user.getIsLoggedIn(),
				user.getToken());
	}
	
	@PostMapping("/create-account")
	private ResponseEntity<userAccountResponse> createAccount(
			@RequestParam String fullName,
			@RequestParam String email,
			@RequestParam String password){
		// Service already returns userAccountResponse with token
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
	        
	        if (properties.getCookie() == null) {
	            System.out.println("❌ ERROR: Cookie configuration is NULL!");
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body("Server configuration error");
	        }
	        
			// Token and logged in status are already set by the service
			userAccountLogInResponse userResponse = getUserLoginResponse(user);
			Cookie cookie = new Cookie("AUTH_TOKEN", user.getToken());
					cookie.setHttpOnly(true);
					cookie.setSecure(true); 
					cookie.setPath("/");
					cookie.setMaxAge(properties.getCookie().getExpiresIn());
			response.addCookie(cookie);
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            
			return ResponseEntity.ok(userResponse);
		} catch (Exception e) {
			return ResponseEntity.status(401).body("Authentication failed: " + e.getMessage());
		}
	}
	
	//get user profile after successfully logged in by taking claims from cookie
	@GetMapping("/profile")
	public ResponseEntity<Object> returnUserAfterLog(
			@CookieValue(name="AUTH_TOKEN", required = false)
			String token) {
		//bcs of jwt filter, we can implement reading like this
		//otherwise cookie would need to be read manually
		
		 //Does cookie exist
	    if (token == null || token.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body("Authentication missing");
	    }
	    
	    String email = authService.extractEmail(token);
	    UserAccount potentialUser = userService.loadUserbyEmail(email);
	    
	    userAccountLogInResponse userResponse = getUserLoginResponse(potentialUser);
	    return ResponseEntity.ok(userResponse);
	}
	
	//check for cookies in session, if not, user needs to be logged in
	@GetMapping("/session-cookie")
	public ResponseEntity<Object> checkSessionCookie (@CookieValue(name="AUTH_TOKEN", required = false)
	String token){
		 //Does cookie exist
	    if (token == null || token.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body("Authentication missing");
	    }
	    return ResponseEntity.ok(token);
	}
	
}
