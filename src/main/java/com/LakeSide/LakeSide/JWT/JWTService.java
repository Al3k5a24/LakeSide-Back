package com.LakeSide.LakeSide.JWT;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

//JSON web token components:
//Header->contains type of token(JWT predominantly) and sign in type used
//Payload -> all informations that are transfered between 2 parties
//Signature -> checks origin of the token and if it was not modified along the way

@Service
public class JWTService {

	//key is used to verify and sign JWT token - loaded from application.properties
	@Value("${jwt.secret.key}")
	private String secretKey;

    //period in which token is valid - loaded from application.properties
	@Value("${jwt.expiration}")
	private long jwtExpiration;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;
	
	public String extractEmail(String token) {

        return extractClaim(token, Claims::getSubject);
	}

    private int cookieExpiration = 604800;
    private String accessTokenCookieName = "ACCESS_TOKEN";
    private String refreshTokenCookieName = "REFRESH_TOKEN";
	
	//basic jwt token, just for user creation
	public String generateAccessToken(UserDetails userDetails) {
        return generateAccessToken(new HashMap<>(), userDetails);
	}

	public String generateAccessToken(
			Map<String, Object> extraClaims,
			UserDetails userDetails) {
	    return Jwts.builder()
	            .setClaims(extraClaims)
                .claim("type","access")
                //getUsername has been modified in UserAccount class and returns email
	            .setSubject(userDetails.getUsername())
	            .setIssuedAt(new Date(System.currentTimeMillis()))
	            .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
	            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
	            .compact();
	}

    public String generateRefreshToken(
            String email){
        return Jwts.builder()
                .subject(email)
                .claim("type", "refresh")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+refreshTokenExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractTokenType(String token) {
        return extractAllClaims(token).get("type", String.class);
    }


    public void generateAccessTokenCookie(HttpServletResponse response, String token){
        Cookie cookie = new Cookie(accessTokenCookieName, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // false za localhost development (HTTP), true za production (HTTPS)
        cookie.setPath("/");
        cookie.setMaxAge(cookieExpiration); //7 days
        cookie.setAttribute("SameSite", "Lax"); // Potrebno za moderne browsere u cross-origin scenarijima
        response.addCookie(cookie);
    }

    public void generateRefreshTokenCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(refreshTokenCookieName, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(cookieExpiration);
        cookie.setAttribute("SameSite", "Strict"); // Strict za refresh token
        response.addCookie(cookie);
    }

    public void deleteCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        response.addCookie(cookie);
    }

	public Boolean isTokenValid(String token, UserDetails userDetails) {
		final String email = extractEmail(token);
		return (email != null && email.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

    public boolean validateRefreshToken(String token) {
        try {
            extractAllClaims(token);
            isTokenExpired(token);
            String tokenType = extractTokenType(token);

            if(!"refresh".equals(tokenType) || isTokenExpired(token)){
                return false;
            }
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

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
