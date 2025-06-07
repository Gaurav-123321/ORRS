package com.gateway.config;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.gateway.util.JwtUtil;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter implements GlobalFilter {

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		String path = request.getURI().getPath();
		HttpMethod method = exchange.getRequest().getMethod(); 


// Public APIs - No Token Required
		if (path.contains("/auth/login") 
        	|| path.contains("/auth/register")
        	|| path.contains("/swagger-ui/")
        	|| path.contains("/v3/api-docs/")
        	|| path.contains("/webjars/")
        	|| path.contains("/favicon.ico")
        	|| path.contains("/user/v3/api-docs")
        	|| path.contains("/train/v3/api-docs")
        	|| path.contains("/booking/v3/api-docs")
        	|| path.contains("/payment/v3/api-docs")
    	) 
{
    return chain.filter(exchange);
	
}

// ...existing code...

		// Token Required
		if (!request.getHeaders().containsKey("Authorization")) {
			return onError(exchange, "Missing Authorization Header", HttpStatus.UNAUTHORIZED);
		}

		String authHeader = request.getHeaders().getFirst("Authorization");
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return onError(exchange, "Invalid Authorization Header", HttpStatus.UNAUTHORIZED);
		}

		String token = authHeader.substring(7);
		if (!jwtUtil.validateToken(token)) {
			return onError(exchange, "JWT validation failed", HttpStatus.UNAUTHORIZED);
		}

		Claims claims = jwtUtil.getClaims(token);
		String role = (String) claims.get("role");

//		 Role-based Access Control
		 if (path.startsWith("/user")) {
		 	if (!"ADMIN".equals(role )) {
		 		return onError(exchange, "Only ADMIN allowed", HttpStatus.FORBIDDEN);
		 	}
		 }

		if (path.startsWith("/train")) {
			if (method == HttpMethod.GET) {
				if (!"ADMIN".equals(role) && !"USER".equals(role)) {
					return onError(exchange, "Only ADMIN or USER allowed", HttpStatus.FORBIDDEN);
				}
			} else { // POST, PUT, DELETE
				if (!"ADMIN".equals(role)) {
					return onError(exchange, "Only ADMIN allowed", HttpStatus.FORBIDDEN);
				}
			}
		}

		if (path.startsWith("/booking")) {
			if (method == HttpMethod.GET) {
				if (!"ADMIN".equals(role) && !"USER".equals(role)) {
					return onError(exchange, "Only ADMIN or USER allowed", HttpStatus.FORBIDDEN);
				}
			} else { // POST, DELETE
				if (!"USER".equals(role)) {
					return onError(exchange, "Only USER allowed", HttpStatus.FORBIDDEN);
				}
			}
		}

		// if (path.startsWith("/payment")) {
		// 	if (method == HttpMethod.GET) {
		// 		if (!"ADMIN".equals(role)) {
		// 			return onError(exchange, "Only ADMIN allowed", HttpStatus.FORBIDDEN);
		// 		}
		// 	} else if (method == HttpMethod.POST) {
		// 		if (!"USER".equals(role)) {
		// 			return onError(exchange, "Only USER allowed", HttpStatus.FORBIDDEN);
		// 		}
		// 	}
		// }

		
		


		return chain.filter(exchange);
	}

	private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus status) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(status);
		return response.setComplete();
	}
}