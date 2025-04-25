package com.rms.userservice.controller;

import com.rms.userservice.dto.UserDTO;
import com.rms.userservice.entity.User;
import com.rms.userservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.get(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getByEmail(email));
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.get());
    }
    

    @PostMapping("/add")
    public ResponseEntity<UserDTO> addUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.add(user));
    }
    
    @GetMapping("/admins")
    public ResponseEntity<List<UserDTO>> getAdmins() {
        return ResponseEntity.ok(userService.getAdmins());
    }

    @DeleteMapping("/cancel/{bookingId}/{userId}")
    public ResponseEntity<String> cancelBooking(@PathVariable Long bookingId, @PathVariable Long userId) {
        boolean success = userService.cancelTicket(bookingId, userId);
        return success ? ResponseEntity.ok("Booking canceled successfully") 
                       : ResponseEntity.badRequest().body("Cancellation failed");
    }

    @PostMapping("/book-ticket/{userId}")
    public ResponseEntity<String> bookTicket(@PathVariable Long userId, @RequestParam String trainNumber, @RequestParam int seatCount) {
        boolean success = userService.bookTicket(userId, trainNumber, seatCount);
        return success ? ResponseEntity.ok("Ticket booked successfully.") 
                       : ResponseEntity.badRequest().body("User not found.");
    }
}
