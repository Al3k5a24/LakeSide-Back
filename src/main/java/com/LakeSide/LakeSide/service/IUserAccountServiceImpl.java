package com.LakeSide.LakeSide.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.LakeSide.LakeSide.Exception.InvalidPasswordException;
import com.LakeSide.LakeSide.Exception.UserAccountNotFoundException;
import com.LakeSide.LakeSide.model.UserAccount;
import com.LakeSide.LakeSide.model.UserAccount.Role;
import com.LakeSide.LakeSide.repository.UserAccountRepository;
import com.LakeSide.LakeSide.response.userAccountResponse;

import JWT.JWTService;

@Service
public class IUserAccountServiceImpl implements IUserAccountService{
	
	private static final String EMAIL_PATTERN = 
			"^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
	private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
	
	@Autowired
	private UserAccountRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JWTService jwtService;
	
	private AuthenticationManager authManager;
	
	@Override
	public userAccountResponse createAccount(String fullName, String email, String password) {
		// Validate input
		if (fullName == null || fullName.trim().isEmpty()) {
			throw new IllegalArgumentException("Full name cannot be empty");
		}
		if (email == null || email.trim().isEmpty()) {
			throw new IllegalArgumentException("Email cannot be empty");
		}
		if (password == null || password.trim().isEmpty()) {
			throw new IllegalArgumentException("Password cannot be empty");
		}
		if (password.length() < 6) {
			throw new IllegalArgumentException("Password must be at least 6 characters long");
		}
		
		// Validate email format
		if (!pattern.matcher(email.trim()).matches()) {
			throw new IllegalArgumentException("Invalid email format");
		}
		
		// Check if user already exists
		Optional<UserAccount> existingUser = userRepository.findUserByEmail(email.trim());
		if (existingUser.isPresent()) {
			throw new IllegalArgumentException("User with email " + email + " already exists");
		}
		
		// Create new user
		UserAccount user = new UserAccount();
		user.setFullName(fullName.trim());
		user.setEmail(email.trim().toLowerCase());
		user.setPassword(passwordEncoder.encode(password));
		user.setIsLoggedIn(false);
		user.setRole(Role.USER);
		
		// Save user
		UserAccount savedUser = userRepository.save(user);
		
		// Create and return response (without password)
		return new userAccountResponse(
				savedUser.getId(),
				savedUser.getFullName(),
				savedUser.getEmail(),
				null, // Never return password
				savedUser.getIsLoggedIn(),
				savedUser.getRole().name()
		);
	}

	@Override
	public UserAccount SignInExistingAccount(String email, String password) {
		if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        
        // Find user by email
        UserAccount potentialUser = userRepository.findUserByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new UserAccountNotFoundException(
                    "User with email " + email + " not found"));
        
        // Verify password BEFORE setting logged in status
        if (!passwordEncoder.matches(password, potentialUser.getPassword())) {
            throw new InvalidPasswordException("Password is incorrect, try again");
        }
        
        //add role as claim to payload
        Map<String, Object> claim = new HashMap();
        claim.put("role", potentialUser.getRole().name());
        claim.put("fullName", potentialUser.getFullName());
        
        // Set logged in status and generate token only after successful authentication
		// Generate JWT token
		String jwtToken = jwtService.generateToken(claim,potentialUser);
		potentialUser.setToken(jwtToken);
        potentialUser.setIsLoggedIn(true);
        userRepository.save(potentialUser);
        return potentialUser;
	}

	@Override
	public UserAccount loadUserbyEmail(String email) {
		if (email == null || email.trim().isEmpty()) {
			throw new IllegalArgumentException("Email cannot be empty");
		}
		UserAccount user = userRepository.findUserByEmail(email.trim().toLowerCase())
				.orElseThrow(() -> new UserAccountNotFoundException("Account has not been found!"));
		return user;
	}
}
