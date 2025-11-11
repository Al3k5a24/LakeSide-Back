package com.LakeSide.LakeSide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import JWT.JWTService;

@SpringBootApplication
public class LakeSideApplication {

	public static void main(String[] args) {
		SpringApplication.run(LakeSideApplication.class, args);
	}
	
	//had error where it "could not" find this 2, now works when entered here
	 @Bean
	 public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	 }

	 @Bean
	    public JWTService jwtService() {
	        return new JWTService();
	    }
}
