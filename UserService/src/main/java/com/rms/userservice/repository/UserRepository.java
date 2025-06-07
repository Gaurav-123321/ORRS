package com.rms.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rms.userservice.entity.User;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long>{
	public User findByEmail(String email);
}
