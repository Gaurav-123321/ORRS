 package com.tms.trainservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

//	private final JwtAuthFilter jwtAuthFilter;

//    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
//        this.jwtAuthFilter = jwtAuthFilter;
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable());
//            .authorizeHttpRequests(auth -> auth
//            	.requestMatchers("/suppliers/drug/code/**").hasAnyAuthority("ROLE_DOCTOR", "ROLE-ADMIN")
//                .requestMatchers(HttpMethod.POST, "/drugs").hasAuthority("ROLE_ADMIN") // ✅ Only Admin can POST
//                .requestMatchers(HttpMethod.PUT, "/drugs").hasAuthority("ROLE_ADMIN")
//                .requestMatchers(HttpMethod.PATCH, "/drugs/**").hasAuthority("ROLE_ADMIN")
//                .requestMatchers(HttpMethod.GET, "/drugs/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_DOCTOR") // ✅ Both Admin & Doctor can GET
//                .anyRequest().authenticated()
//            )
//            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}