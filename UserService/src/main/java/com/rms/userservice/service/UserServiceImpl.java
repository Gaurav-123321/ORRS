package com.rms.userservice.service;

import com.rms.bookingservice.dto.BookingDto;
import com.rms.userservice.dto.UserDTO;
import com.rms.userservice.entity.User;
import com.rms.userservice.feign.BookingServiceClient;
import com.rms.userservice.feign.SearchServiceClient;
import com.rms.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingServiceClient bookingServiceClient;

    @Autowired
    private SearchServiceClient searchServiceClient;

    @Override
    public UserDTO add(User user) {
        System.out.println("Received request to add user: " + user.getEmail());
        User savedUser = userRepository.save(user);
        System.out.println("User saved successfully with ID: " + savedUser.getUserId());
        return new UserDTO(savedUser.getUserId(), savedUser.getUserFirstName(), savedUser.getUserLastName(), savedUser.getEmail(), savedUser.getRole());
    }

    @Override
    public List<UserDTO> get() {
        List<User> users = userRepository.findByRole("user");
        return users.stream()
                .map(user -> new UserDTO(user.getUserId(), user.getUserFirstName(), user.getUserLastName(), user.getEmail(), user.getRole()))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getAdmins() {
        List<User> admins = userRepository.findByRole("admin");
        return admins.stream()
                .map(user -> new UserDTO(user.getUserId(), user.getUserFirstName(), user.getUserLastName(), user.getEmail(), user.getRole()))
                .collect(Collectors.toList());
    }
    
    @Override
    public UserDTO get(Long id) { 
        return userRepository.findById(id)
                .map(user -> new UserDTO(user.getUserId(), user.getUserFirstName(), user.getUserLastName(), user.getEmail(), user.getRole()))
                .orElse(null);
    }
    
    @Override
    public UserDTO getByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> new UserDTO(user.getUserId(), user.getUserFirstName(), user.getUserLastName(), user.getEmail(), user.getRole()))
                .orElse(null);
    }

    @Override
    public void delete(Long id) {  // Change Long to int here
        userRepository.deleteById(id);
    }

    @Override
    public boolean bookTicket(Long userId, String trainNumber, int seatCount) {  // Change Long to int here
        
        if (seatCount < 1 || seatCount > 7) {
            throw new IllegalArgumentException("You can only book between 1 and 7 tickets per transaction.");
        }

        BookingDto bookingDto = new BookingDto();
        bookingDto.setUserId(userId);

        try {
            Long trainId = Long.parseLong(trainNumber);
            bookingDto.setTrainId(trainId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid train number format. It should be a numeric value.");
        }

        bookingDto.setSeatCount(seatCount);

        try {
            bookingServiceClient.addBooking(bookingDto);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean cancelTicket(Long bookingId, Long userId) {
        try {
            bookingServiceClient.cancelBooking(bookingId, userId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
