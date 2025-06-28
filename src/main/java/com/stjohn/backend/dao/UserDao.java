package com.stjohn.backend.dao;

import com.stjohn.backend.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    List<User> findActiveUsers();

    List<User> findUsersByNamePattern(String pattern);

    Optional<User> findUserWithDetails(Long id);

    List<User> findUsersCreatedBetween(String startDate, String endDate);

    int countUsersByDomain(String domain);

    List<User> findUsersWithPagination(int page, int size);

    boolean softDeleteUser(Long id);

    List<User> searchUsers(String searchTerm);
}