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
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;

@RequestMapping("/auth")
//CORS policy override for diffrent paths 
@CrossOrigin(origins="*")
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
			
			// ✅✅✅ DODAJ LOGOVE OVDE:
	        System.out.println("========== COOKIE DEBUG ==========");
	        System.out.println("User token: " + user.getToken());
	        System.out.println("Properties cookie: " + properties);
	        System.out.println("Cookie config: " + properties.getCookie());
	        
	        if (properties.getCookie() == null) {
	            System.out.println("❌ ERROR: Cookie configuration is NULL!");
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body("Server configuration error");
	        }
	        
			// Token and logged in status are already set by the service
			userAccountLogInResponse userResponse = getUserLoginResponse(user);
			ResponseCookie cookie = ResponseCookie.from(properties.getCookie().getName(), user.getToken())
                    .httpOnly(true)
                    .secure(false) //disable httos in order to set cookie
                    .path("/")
                    .maxAge(properties.getCookie().getExpiresIn())
                    .sameSite("Lax") //to set cookie
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            
            System.out.println("Cookie added to response header");

         // ✅ DODAJ OVE LOGOVE:
         System.out.println("Response headers: " + response.getHeaderNames());
         System.out.println("Set-Cookie header value: " + response.getHeader(HttpHeaders.SET_COOKIE));

         System.out.println("==================================");
            
			return ResponseEntity.ok(userResponse);
		} catch (Exception e) {
			return ResponseEntity.status(401).body("Authentication failed: " + e.getMessage());
		}
	}
	
	//get user profile after successfully logged in by taking claims from cookie
	@GetMapping("/profile")
	public ResponseEntity<Object> returnUserAfterLog(
			@AuthenticationPrincipal UserAccount userDetails,
			@CookieValue(name="AUTH_TOKEN", required = false)
			String token) {
		//bcs of jwt filter, we can implement reading like this
		//otherwise cookie would need to be read manually
		if (userDetails == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body("Not authenticated");
	    }
		
		 // Provera 2: Da li postoji cookie
	    if (token == null || token.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body("Authentication token missing");
	    }
	    
	    try {
			if(!authService.isTokenValid(token, userDetails)) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body("Invalid token");
		}
	    
	    String email = authService.extractEmail(token);
	    UserAccount potentialUser = userService.loadUserbyEmail(email);
	    
	    userAccountLogInResponse userResponse = getUserLoginResponse(potentialUser);
	    return ResponseEntity.ok(userResponse);
	}
}
