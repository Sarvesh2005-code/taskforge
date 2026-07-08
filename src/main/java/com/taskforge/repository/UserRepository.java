package com.taskforge.repository;

import com.taskforge.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by username (for login in Module 6).
     * Returns Optional because the user might not exist.
     */
    Optional<User> findByUsername(String username);

    /** Check if a username is already taken (for registration). */
    boolean existsByUsername(String username);

    /** Check if an email is already taken. */
    boolean existsByEmail(String email);
}
