package Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.LakeSide.LakeSide.Exception.UserAccountNotFoundException;
import com.LakeSide.LakeSide.repository.UserAccountRepository;

@Configuration
@ConfigurationProperties(prefix = "app") //for cookie connection in app properties
public class AppConfig {
	
	@Autowired
	private UserAccountRepository userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private CookieConfiguration cookie;
	
	public static class CookieConfiguration {
        private String name;
        private int expiresIn;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getExpiresIn() {
			return expiresIn;
		}
		public void setExpiresIn(int expiresIn) {
			this.expiresIn = expiresIn;
		}
    }

	public CookieConfiguration getCookie() {
        return cookie;
	}

	public void setCookie(CookieConfiguration cookie) {
        this.cookie = cookie;
	}
}
