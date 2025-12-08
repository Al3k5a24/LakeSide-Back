package com.LakeSide.LakeSide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.LakeSide.LakeSide.Configuration.AppConfig;
import com.LakeSide.LakeSide.JWT.JWTService;

@SpringBootApplication
public class LakeSideApplication {

	public static void main(String[] args) {
		SpringApplication.run(LakeSideApplication.class, args);
	}
}
