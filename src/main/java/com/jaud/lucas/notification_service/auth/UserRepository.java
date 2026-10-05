package com.jaud.lucas.notification_service.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jaud.lucas.notification_service.auth.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{

    @Query("SELECT u from User u JOIN FETCH u.roles where u.username = ?1")
    Optional<User> findByUsername(String username);
   
}
