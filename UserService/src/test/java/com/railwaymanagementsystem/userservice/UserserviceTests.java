package com.railwaymanagementsystem.userservice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rms.userservice.entity.User;
import com.rms.userservice.exception.UserNotFoundException;
import com.rms.userservice.repository.UserRepository;
import com.rms.userservice.service.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

//    @BeforeEach
//    void setUp() {
//        user = new User(1L, "Nitesh", "yadav", "nitesh.yadav@gmail.com", "USER");
//    }

    @Test
    void testAddUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        User savedUser = userService.add(user);
        assertNotNull(savedUser);
        assertEquals("John", savedUser.getUserFirstName());
    }

    @Test
    void testGetUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User foundUser = userService.get(1L);
        assertNotNull(foundUser);
        assertEquals("nitesh.yadav@gmail.com", foundUser.getEmail());
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.get(2L));
    }

    @Test
    void testGetAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(user);
        when(userRepository.findAll()).thenReturn(users);
        List<User> retrievedUsers = userService.get();
        assertFalse(retrievedUsers.isEmpty());
    }

    @Test
    void testDeleteUser() {
        userService.delete(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }
}

