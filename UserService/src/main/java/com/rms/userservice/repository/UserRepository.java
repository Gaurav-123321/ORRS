package com.rms.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.rms.userservice.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);
    
    List<User> findByRole(String role);
}