package com.rms.userservice.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rms.userservice.entity.User;
import com.rms.userservice.exception.UserNotFoundException;
import com.rms.userservice.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepo;

	@Override
	public User add(User user) {
		logger.info("Adding new user: {}", user);
		User savedUser = userRepo.save(user);
		logger.info("User added successfully: {}", savedUser);
		return savedUser;
	}

	@Override
	public List<User> get() {
		logger.info("Fetching all users");
		List<User> users = userRepo.findAll();
		logger.info("Total users found: {}", users.size());
		return users;
	}

	@Override
	public User get(Long id) {
		logger.info("Fetching user with ID: {}", id);
		User user = userRepo.findById(id).orElseThrow(() -> {
			logger.error("User with ID {} not found", id);
			return new RuntimeException("User not found...");
		});
		logger.info("User found: {}", user);
		return user;
	}

	@Override
	public User getByEmail(String email) {
		logger.info("Fetching user with email: {}", email);
		User user = userRepo.findByEmail(email);
		if (user == null) {
			logger.error("User with email {} not found", email);
			throw new UserNotFoundException("User not found");
		}
		logger.info("User found: {}", user);
		return user;
	}

	@Override
	public void delete(Long id) {
		logger.info("Deleting user with ID: {}", id);
		userRepo.deleteById(id);
		logger.info("User deleted successfully");
	}
}