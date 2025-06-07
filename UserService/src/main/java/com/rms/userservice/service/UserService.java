package com.rms.userservice.service;

import java.util.List;

import com.rms.userservice.entity.User;

public interface UserService {
	
	User add(User user);
	List<User> get();
	User get(Long id);
	User getByEmail(String email);
	
	void delete(Long id);
	
}