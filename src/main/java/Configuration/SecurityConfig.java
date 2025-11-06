package Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	 //Password encoder
	 @Bean
     public PasswordEncoder passwordEncoder() {
         return new BCryptPasswordEncoder();
     }
	 
	 //filter by roles given to accounts
	 @Bean
	 public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		 http.csrf(AbstractHttpConfigurer::disable)
		 .authorizeHttpRequests(auth->auth
			//public endpoints for everyone
		   .requestMatchers("/auth/**").permitAll()
		   .requestMatchers("/rooms/all-rooms").permitAll()
		   
		   //my bookings list,book room,cancel,
		   //default role will be user
		   .requestMatchers("/rooms/bookings/my-bookings").hasRole("USER")
		   .requestMatchers("/rooms/browse-rooms/booking/**").hasRole("USER")
		   
		   //here will be admin endpoints 
		   
		   //here will be owner endpoints
		   
		   .anyRequest().authenticated());
		return http.build();
	 }
}
