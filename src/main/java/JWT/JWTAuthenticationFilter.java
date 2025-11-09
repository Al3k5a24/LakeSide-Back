package JWT;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

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
	private IUserAccountService userService;
	
	@Override
	protected void doFilterInternal(
			@NotNull HttpServletRequest request,
			@NotNull HttpServletResponse response,
			@NotNull FilterChain filterChain)
			throws ServletException, IOException {
		//JWT token header
		final String authHeader = request.getHeader("Athorization"); 
		final String jwt;
		//data from user request that will be extracted from fields in order to check if user is in our database
		final String userEmail;
		
		//early check JWT token
		if(authHeader==null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		//start from position 7 bcs Bearer with 1 space has 7 positions(letters)
		//that is not important for us, only what comes next
		jwt = authHeader.substring(7);
		userEmail=jwtService.extractEmail(jwt);
		
		//check if userEmail is not null and the user has not been authenticated already(not connected)
		if(userEmail != null && SecurityContextHolder.getContext().getAuthentication()==null) {
			UserDetails userDetails = this.userService.loadUserbyEmail(userEmail);
			//check if token is vald
			if(jwtService.isTokenValid(jwt,userDetails)) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
						userDetails,null,userDetails.getAuthorities());
				//use info from request to create token
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		//always call!
		filterChain.doFilter(request, response);
	}
}
