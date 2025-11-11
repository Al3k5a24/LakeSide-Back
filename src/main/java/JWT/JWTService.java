package JWT;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

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

	//key is used to verify and sign JWT token - loaded from application.properties
	@Value("${jwt.secret.key}")
	private String secretKey;
	
	@Value("${jwt.expiration}")
	private long jwtExpiration;
	
	public String extractEmail(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	//basic jwt token, just for user creation
	public String generateToken(UserDetails userDetails) {
	    return generateToken(new HashMap<>(), userDetails);
	}
	
	//jwt token generated from claims, with creation and expiration date
	public String generateToken(
			Map<String, Object> extraClaims,
			UserDetails userDetails) {
	    return Jwts.builder()
	            .setClaims(extraClaims)  // Prvo postavi extra claims
	            .setSubject(userDetails.getUsername())
	            .setIssuedAt(new Date(System.currentTimeMillis()))
	            .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
	            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
	            .compact();
	}
	
	//method to validate jwt token
	public Boolean isTokenValid(String token, UserDetails userDetails) {
		final String email = extractEmail(token);
		return (email != null && email.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	//check if date is past expiration date
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	//!!! claims represent information from user sent in request which will be used to create JWT by server
	public <T> T extractClaim(String token, Function<Claims, T> claimResolver){
		final Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}
	
	//signing key is used to digitally sign jwt key to verify sender is who he said he is and check if JWT was modified
	private Claims extractAllClaims(String token) {
		return Jwts.parser()
			.setSigningKey(getSignInKey())
			.build()
			.parseSignedClaims(token)
		    .getPayload();
	}

	//key that will be used as "signature stamp" to check every JWT token
	//Note: The secret key should be a Base64-encoded string. If you have a hex string, 
	//you need to convert it to Base64 first or decode it properly
	private Key getSignInKey() {
		// Assuming the secret key is a hex string, we convert it to bytes
		// For better security, use a proper Base64-encoded key (256-bit = 32 bytes = 44 Base64 chars)
		byte[] keyBytes;
		keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
