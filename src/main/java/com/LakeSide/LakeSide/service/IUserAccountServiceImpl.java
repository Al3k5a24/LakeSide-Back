package com.LakeSide.LakeSide.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.LakeSide.LakeSide.Exception.InvalidPasswordException;
import com.LakeSide.LakeSide.Exception.UserAccountNotFoundException;
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
		user.setIsLoggedIn(false);
		userRepository.save(user);
		return user;
	}

	@Override
	public UserAccount SignInExistingAccount(String email, String password) {
		if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        
        UserAccount potentialUser = userRepository.findUserByEmail(email.trim())
                .orElseThrow(() -> new UserAccountNotFoundException(
                    "User with email " + email + " not found"));
        potentialUser.setIsLoggedIn(true);
        if (!passwordEncoder.matches(password, potentialUser.getPassword())) {
            throw new InvalidPasswordException("Password is incorrect,try again");
        }
        userRepository.save(potentialUser);
        return potentialUser;
	}
}
