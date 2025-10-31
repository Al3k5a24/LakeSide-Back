package com.LakeSide.LakeSide.service;

import com.LakeSide.LakeSide.model.UserAccount;

public interface IUserAccountService {

	UserAccount createAccount(String fullName,String email,String Password);
}
