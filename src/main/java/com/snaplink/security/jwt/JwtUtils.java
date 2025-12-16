package com.snaplink.security.jwt;

import java.security.Key;

import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.snaplink.service.UserDetailsImpl;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtils {
	
	@Value("${jwt.secret}")
	private String jwtSecret;
	
	@Value("${jwt.expiration}")
	private int JwtExpirationMs;
	 
	//Extracting Bearer token from Request
	public String geJwtFromHeader(HttpServletRequest request) {
		
		String bearerToken = request.getHeader("Authorization");
		
		 if(bearerToken!=null &&  bearerToken.startsWith("Bearer ")){
			  return bearerToken.substring(7);
		 }
		 
		 return null;
	}
	
	//Generating Token
	public String generateToken(UserDetailsImpl userDetails) {
		
		String username = userDetails.getUsername(); 
		String roles  = userDetails.getAuthorities().stream()
					.map(authority -> authority.getAuthority())
					.collect(Collectors.joining(","));
		return Jwts.builder()
				.subject(username)
				.claim("roles", roles)
				 .issuedAt(new Date())
				.expiration(new Date((new Date().getTime()+JwtExpirationMs)))
				.signWith(key())
				.compact();
		  
	}
	
	//Getting username from the token
	public String getUsernameFromJwtToken(String token) {
		return Jwts.parser()
				.verifyWith((SecretKey) key())
				.build().parseSignedClaims(token)
				.getPayload().getSubject();
	}
	
	//To check either token is valid or not
	public boolean validateToken(String authToken) {
		try {
			
			 Jwts.parser().verifyWith((SecretKey)key())
				.build().parseSignedClaims(authToken);

			 return true;
			
		} 
		catch (JwtException e) {
			throw new RuntimeException(e);
	 	}
		catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
	 	}
		catch (Exception e) {
			throw new RuntimeException(e);
	 	}
	}
	
	//Helper method for Key Generation
	private Key key() {
		 return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}
	
	
}
