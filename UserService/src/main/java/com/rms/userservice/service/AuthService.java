package com.rms.userservice.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rms.userservice.entity.User;
import com.rms.userservice.exception.UserNotFoundException;
import com.rms.userservice.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.repo = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repo.save(user);
        System.out.println("Encrypted password stored in DB: " + user.getPassword());
        return "User registered successfully";
    }

    public String login(String email, String password) throws UserNotFoundException {
        User user = repo.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtService.generateToken(user);
    }
}
