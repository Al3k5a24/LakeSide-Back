package JWT;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.LakeSide.LakeSide.service.IUserAccountService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {
	//JSON web token components:
	//Header->contains type of token(JWT predominantly) and sign in type used
	//Payload -> all informations that are transfered between 2 parties
	//Signature -> checks origin of the token and if it was not modified along the way

	//key is used to verify and sign JWT token
	private static final String secret_key="790abb2d3c85881fa129ef731c4258ee3af55bbe8488c5fc763f2d00f31bc3ea";
	
	public String extractEmail(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	//jwt token generated from claims, with creation and expiration date
	public String generateToken(
			UserDetails userDetails
			) {
		return Jwts.builder()
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
				.signWith(getSignInKey(),SignatureAlgorithm.HS256)
				.compact();
	}
	
	//method to validate jwt token
	public Boolean isTokenValid(String token,UserDetails userDetails) {
		final String email=extractEmail(token);
		return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	//check if date is past expiration date
	private boolean isTokenExpired(String token) {
		return extractExparation(token).before(new Date());
	}
	
	private Date extractExparation(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	//!!! claims represent information from user sent in request which will be used to create JWT by server
	public <T> T extractClaim(String token, Function<Claims, T> claimResolver){
		final Claims claims=extractAllClaims(token);
		return claimResolver.apply(claims);
	}
	
	//singing key is used to digitally sign jwt key to verify sender is who he said he is and check if JWT was modified
	@SuppressWarnings("deprecation")
	private Claims extractAllClaims(String token) {
		return Jwts.parser()
			.setSigningKey(getSignInKey())
			.build()
			.parseClaimsJws(token)
		    .getBody();
	}

	//key that will be used as "signature stamp" to check every JWT token
	private Key getSignInKey() {
		byte[] hexButes=Decoders.BASE64.decode(secret_key);
		return Keys.hmacShaKeyFor(hexButes);
	}

	public static String getSecretKey() {
		return secret_key;
	}

}
