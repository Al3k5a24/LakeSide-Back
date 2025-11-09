package com.LakeSide.LakeSide.service;

import java.util.Optional;

import com.LakeSide.LakeSide.model.UserAccount;
import com.LakeSide.LakeSide.response.userAccountResponse;

public interface IUserAccountService {

	userAccountResponse createAccount(String fullName,String email,String Password);
	
	UserAccount SignInExistingAccount(String email,String password);
	
	UserAccount loadUserbyEmail(String email);
}
