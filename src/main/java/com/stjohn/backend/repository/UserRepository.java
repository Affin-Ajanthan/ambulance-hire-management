package com.stjohn.backend.repository;

import com.stjohn.backend.dto.UserDto;
import com.stjohn.backend.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Custom query methods
    Optional<User> findByEmail(String email);

    List<User> findByNameContainingIgnoreCase(String name);

    @Query("SELECT u FROM User u WHERE u.name = :name")
    List<User> findUsersByName(@Param("name") String name);

    @Query("SELECT u FROM User u WHERE LOWER(u.userId) = LOWER(:userId)")
    Optional<User> findUsersById(@Param("userId") String userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.userId = :userId")
    int deleteUserById(@Param("userId") String userId);
    boolean existsByEmail(String email);
}