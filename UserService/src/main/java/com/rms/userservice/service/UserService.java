package com.rms.userservice.service;

import com.rms.userservice.dto.UserDTO;
import com.rms.userservice.entity.User;
import java.util.List;

public interface UserService {
    UserDTO add(User user);
    List<UserDTO> get();
    UserDTO get(Long id);
    UserDTO getByEmail(String email);
    void delete(Long id); 
    boolean bookTicket(Long userId, String trainNumber, int seatCount);
    boolean cancelTicket(Long bookingId, Long userId);
	List<UserDTO> getAdmins();
}
