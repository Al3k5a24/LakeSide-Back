package Configuration;

import java.security.AuthProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.LakeSide.LakeSide.Exception.UserAccountNotFoundException;
import com.LakeSide.LakeSide.repository.UserAccountRepository;
import com.LakeSide.LakeSide.service.IUserAccountService;

@Configuration
public class AppConfig {
	
	@Autowired
	private UserAccountRepository userRepo;
	
	@Bean
	public UserDetailsService userDetailsService() {
		return email -> userRepo.findUserByEmail(email)
				.orElseThrow(() -> new UserAccountNotFoundException("Account has not been found!"));
	}
	
	
	//used to fetch user details
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
		daoProvider.setUserDetailsService(userDetailsService());
		return daoProvider;
	}
	
	@Bean
	public AuthenticationManager authenticationMenager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}
