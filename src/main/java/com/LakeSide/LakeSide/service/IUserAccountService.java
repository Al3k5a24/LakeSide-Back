package com.LakeSide.LakeSide.service;

import java.util.Optional;

import com.LakeSide.LakeSide.model.UserAccount;

public interface IUserAccountService {

	UserAccount createAccount(String fullName,String email,String Password);
	
	UserAccount SignInExistingAccount(String email,String password);

}
