// Add this method to your UserRepository
package com.consulting.repository;

import com.consulting.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    @Query(value = "SELECT MAX(u.userId) FROM User u")
    String findLastUserId();

    // Add this method for JWT authentication
    Optional<User> findByUserEmailId(String email);

    boolean existsByUserEmailId(String email);

}