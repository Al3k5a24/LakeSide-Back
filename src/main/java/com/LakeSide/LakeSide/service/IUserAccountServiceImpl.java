package com.LakeSide.LakeSide.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.LakeSide.LakeSide.model.UserAccount;
import com.LakeSide.LakeSide.repository.UserAccountRepository;

@Service
public class IUserAccountServiceImpl implements IUserAccountService{
	
	@Autowired
	private UserAccountRepository userRepository;
	
	public UserAccountRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(UserAccountRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	private BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
	
	
	@Override
	public UserAccount createAccount(String fullName, String email, String Password) {
		UserAccount user=new UserAccount();
		if(fullName!=null) user.setFullName(fullName);
		if(email!=null) user.setEmail(email);
		if(Password!=null) {
			String encryptedPassword=passwordEncoder.encode(Password);
			user.setPassword(encryptedPassword);
		}
		user.setIsActive(false);
		userRepository.save(user);
		return user;
	}

}
