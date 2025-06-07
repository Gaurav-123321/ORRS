package com.rms.bookingservice.util;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtUtil {

	@Value("${jwt.secret}")
	private String SECRET;

	public boolean validateToken(String token) {
		try {
			return !isTokenExpired(token);
		} catch (Exception e) {
			return false;
		}
	}

	public String extractUsername(String token) {
		return getClaims(token).getSubject();
	}

	public Claims getClaims(String token) {
	    return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
	}

	private boolean isTokenExpired(String token) {
		return getClaims(token).getExpiration().before(new Date());
	}
	
}