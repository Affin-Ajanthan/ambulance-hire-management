package com.stjohn.backend.service;

import com.stjohn.backend.dto.CreateUserDto;
import com.stjohn.backend.dto.UpdateUserDto;
import com.stjohn.backend.dto.UserDto;
import com.stjohn.backend.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDto> getAllUsers();

    Optional<UserDto> getUserById(String userId);

    UserDto createUser(CreateUserDto createUserDto);

    UserDto updateUser(String id, UpdateUserDto updateUserDto);

    void deleteUser(String id);

    Optional<UserDto> getUserByEmail(String email);

    List<UserDto> searchUsers(String searchTerm);

    List<UserDto> getUsersCreatedBetween(String startDate, String endDate);

    int getUserCountByDomain(String domain);
}