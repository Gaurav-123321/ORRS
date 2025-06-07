package com.gateway.config;



import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
	@Value("${cors.allowed-origins}")
	private String allowedOrigins;

	@Bean
	public CorsFilter corsFilter() {
//		CorsConfiguration config = new CorsConfiguration();
//		List<String> origins = Arrays.asList(allowedOrigins.split(","));
//		config.setAllowedOrigins(origins);
//		config.addAllowedMethod("*");
//		config.addAllowedHeader("*");
//		config.setAllowCredentials(true);
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", config);
//		return new CorsFilter(source);
		 CorsConfiguration config = new CorsConfiguration();
	        config.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
	        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
	        config.setAllowedHeaders(Arrays.asList("*"));
	        config.setExposedHeaders(Arrays.asList("Authorization"));
	        config.setMaxAge(3600L);
	        
	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        source.registerCorsConfiguration("/**", config);
	        return new CorsFilter(source);
	}
}
