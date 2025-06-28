package com.stjohn.backend.service.impl;

import com.stjohn.backend.dao.UserDao;
import com.stjohn.backend.dto.CreateUserDto;
import com.stjohn.backend.dto.UpdateUserDto;
import com.stjohn.backend.dto.UserDto;
import com.stjohn.backend.model.User;
import com.stjohn.backend.repository.UserRepository;
import com.stjohn.backend.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDao userDao;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDto> getUserById(String userId) {
        return userRepository.findUsersById(userId)
                .map(this::convertToDto);
    }

    @Override
    public UserDto createUser(CreateUserDto createUserDto) {
        // Check if email already exists
        if (userRepository.existsByEmail(createUserDto.getEmail())) {
            throw new RuntimeException("Email already exists: " + createUserDto.getEmail());
        }

        User user = new User();
        user.setUserId(createUserDto.getUserId());
        user.setName(createUserDto.getName());
        user.setEmail(createUserDto.getEmail());
        user.setPassword(createUserDto.getPassword()); // In real app, hash the password

        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    @Override
    public UserDto updateUser(String userId, UpdateUserDto updateUserDto) {
        return userRepository.findUsersById(userId)
                .map(user -> {
                    if (updateUserDto.getName() != null) {
                        user.setName(updateUserDto.getName());
                    }
                    if (updateUserDto.getEmail() != null) {
                        // Check if new email already exists for another user
                        if (userRepository.existsByEmail(updateUserDto.getEmail()) &&
                                !user.getEmail().equals(updateUserDto.getEmail())) {
                            throw new RuntimeException("Email already exists: " + updateUserDto.getEmail());
                        }
                        user.setEmail(updateUserDto.getEmail());
                    }
                    if (updateUserDto.getPassword() != null) {
                        user.setPassword(updateUserDto.getPassword());
                    }
                    return convertToDto(userRepository.save(user));
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    @Override
    public void deleteUser(String userId) {
        userRepository.deleteUserById(userId);
    }

    @Override
    public Optional<UserDto> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::convertToDto);
    }

    @Override
    public List<UserDto> searchUsers(String searchTerm) {
        // Using custom DAO for complex search
        return userDao.searchUsers(searchTerm)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getUsersCreatedBetween(String startDate, String endDate) {
        // Using custom DAO for date range queries
        return userDao.findUsersCreatedBetween(startDate, endDate)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public int getUserCountByDomain(String domain) {
        // Using custom DAO for business logic
        return userDao.countUsersByDomain(domain);
    }

    private UserDto convertToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    private User convertToEntity(CreateUserDto createUserDto) {
        User user = new User();
        user.setUserId(createUserDto.getUserId());
        user.setName(createUserDto.getName());
        user.setEmail(createUserDto.getEmail());
        user.setPassword(createUserDto.getPassword());
        return user;
    }
}