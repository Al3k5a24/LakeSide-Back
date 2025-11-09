package com.LakeSide.LakeSide.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

@RequestMapping("/auth")
//CORS policy override for diffrent paths 
@CrossOrigin(origins="*")
@RestController
public class UserAccountController {

	@Autowired
	private IUserAccountService userService;

	public IUserAccountService getUserService() {
		return userService;
	}

	public void setUserService(IUserAccountService userService) {
		this.userService = userService;
	}
	
	//general use
	private userAccountResponse getUserResponse(UserAccount user) {
		return new userAccountResponse(user.getId(),
				user.getFullName(),
				user.getEmail(),
				user.getPassword(),
				user.getIsLoggedIn(),
				user.getRole().name(),
				user.getToken());
	}
	
	//for login
	private userAccountLogInResponse getUserLoginResponse(UserAccount user) {
		return new userAccountLogInResponse(
				user.getEmail(),
				user.getPassword(),
				user.getIsLoggedIn(),
				user.getToken());
	}
	
	@PostMapping("/create-account")
	private ResponseEntity<userAccountResponse> createAccount(
			@RequestParam String fullName,
			@RequestParam String email,
			@RequestParam String password){
		UserAccount user=userService.createAccount(fullName, email, password);
		userAccountResponse userResponse=getUserResponse(user);
		return ResponseEntity.ok(userResponse);
	}
	
	@PostMapping("/sign-in")
	private ResponseEntity<userAccountLogInResponse> LogInExistingAccount(
			@RequestParam String email,
			@RequestParam String password) {
		UserAccount user=userService.SignInExistingAccount(email, password);
		user.setIsLoggedIn(true);
		userAccountLogInResponse userResponse=getUserLoginResponse(user);
		return ResponseEntity.ok(userResponse);
	}
}
