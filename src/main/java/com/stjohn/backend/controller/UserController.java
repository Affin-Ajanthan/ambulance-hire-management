package com.stjohn.backend.controller;

import com.stjohn.backend.dto.CreateUserDto;
import com.stjohn.backend.dto.UpdateUserDto;
import com.stjohn.backend.dto.UserDto;
import com.stjohn.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String userId) {
        return userService.getUserById(userId)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserDto createUserDto) {
        try {
            UserDto createdUser = userService.createUser(createUserDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable String userId,
                                              @Valid @RequestBody UpdateUserDto updateUserDto) {
        try {
            UserDto updatedUser = userService.updateUser(userId, updateUserDto);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> searchUsers(@RequestParam String term) {
        List<UserDto> users = userService.searchUsers(term);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/created-between")
    public ResponseEntity<List<UserDto>> getUsersCreatedBetween(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        List<UserDto> users = userService.getUsersCreatedBetween(startDate, endDate);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/count/domain/{domain}")
    public ResponseEntity<Integer> getUserCountByDomain(@PathVariable String domain) {
        int count = userService.getUserCountByDomain(domain);
        return ResponseEntity.ok(count);
    }
}
