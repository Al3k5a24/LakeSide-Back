package com.LakeSide.LakeSide.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import com.LakeSide.LakeSide.model.RefreshToken;
import com.LakeSide.LakeSide.repository.RefreshTokenRepository;
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

import com.LakeSide.LakeSide.JWT.JWTService;

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

    @Autowired
	private RefreshTokenRepository refreshTokenRepository;
	
	@Override
	public userAccountResponse createAccount(String fullName, String email, String password) {
		if (fullName == null || fullName.trim().isEmpty()) {
			throw new IllegalArgumentException("Full name cannot be empty");
		}
		if (email == null || email.trim().isEmpty()) {
			throw new IllegalArgumentException("Email cannot be empty");
		}
		if (password == null || password.trim().isEmpty()) {
			throw new IllegalArgumentException("Password cannot be empty");
		}
		if (password.length() < 8) {
			throw new IllegalArgumentException("Password must be at least 8 characters long");
		}
		if (!pattern.matcher(email.trim()).matches()) {
			throw new IllegalArgumentException("Invalid email format");
		}

		Optional<UserAccount> existingUser = userRepository.findUserByEmail(email.trim());
		if (existingUser.isPresent()) {
			throw new IllegalArgumentException("User with email " + email + " already exists");
		}

		UserAccount user = new UserAccount();
		user.setFullName(fullName.trim());
		user.setEmail(email.trim().toLowerCase());
		user.setPassword(passwordEncoder.encode(password));
		user.setRole(Role.USER);

        String RtokenString = jwtService.generateRefreshToken(user.getEmail());

        RefreshToken refreshToken = new RefreshToken(
                LocalDateTime.now().plusDays(7),
                RtokenString,
                user.getEmail());

        refreshTokenRepository.save(refreshToken);

		UserAccount savedUser = userRepository.save(user);

		return new userAccountResponse(
				savedUser.getId(),
				savedUser.getFullName(),
				savedUser.getEmail(),// Never return password
				savedUser.getRole().name(),
                savedUser.getToken()
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

        UserAccount potentialUser = userRepository.findUserByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new UserAccountNotFoundException(
                    "User with email " + email + " not found"));

        if (!passwordEncoder.matches(password, potentialUser.getPassword())) {
            throw new InvalidPasswordException("Password is incorrect, try again");
        }

        //add role and full name as claim to payload
        Map<String, Object> claim = new HashMap();
        claim.put("role", potentialUser.getRole().name());
        claim.put("fullName", potentialUser.getFullName());

		String jwtToken = jwtService.generateAccessToken(claim,potentialUser);
		potentialUser.setToken(jwtToken);
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
