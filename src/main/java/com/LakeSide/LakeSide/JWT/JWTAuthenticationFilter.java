package com.LakeSide.LakeSide.JWT;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.LakeSide.LakeSide.model.UserAccount;
import com.LakeSide.LakeSide.service.IUserAccountService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter{
    //name is self-explainatory, once per user request this class should start
	//class that will extract user info from token

	@Autowired
	private JWTService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(
			@NotNull HttpServletRequest request,
			@NotNull HttpServletResponse response,
			@NotNull FilterChain filterChain)
			throws ServletException, IOException {
		try {
			//JWT token can come from Authorization header OR from AUTH_TOKEN cookie
			final String authHeader = request.getHeader("Authorization"); 
			String jwt = null;

			//data from user request that will be extracted from fields in order to check if user is in our database
			final String userEmail;
			
			// First, try to get token from Authorization header
			if(authHeader != null && authHeader.startsWith("Bearer ")) {
				//start from position 7 because Bearer with 1 space has 7 positions(letters)
				jwt = authHeader.substring(7);
			} else {
				// If no Authorization header, try to get token from cookie
				jakarta.servlet.http.Cookie[] cookies = request.getCookies();
				if(cookies != null) {
					for(jakarta.servlet.http.Cookie cookie : cookies) {
						if("AUTH_TOKEN".equals(cookie.getName())) {
							jwt = cookie.getValue();
							break;
						}
					}
				}
			}
			
			// If no token found in header or cookie, continue filter chain
			if(jwt == null || jwt.isEmpty()) {
				filterChain.doFilter(request, response);
				return;
			}
			
			userEmail = jwtService.extractEmail(jwt);
			
			//check if userEmail is not null and the user has not been authenticated already(not connected)
			if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
				
				//check if userDetails exists and token is valid
				if(userDetails != null && jwtService.isTokenValid(jwt, userDetails)) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					//use info from request to create token
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}
		} catch (Exception e) {
			// Log the exception and continue with the filter chain
			// Invalid tokens will simply not authenticate the user
			// In production, you might want to log this for monitoring
			logger.error("JWT Authentication failed: " + e.getMessage());
		}
		
		//always call!
		filterChain.doFilter(request, response);
	}
}
