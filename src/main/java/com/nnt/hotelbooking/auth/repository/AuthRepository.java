package com.nnt.hotelbooking.auth.repository;

import com.nnt.hotelbooking.auth.model.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, Long> {
    @Query("""
        SELECT a
        FROM Auth a
        WHERE a.username = :username
          AND a.accountStatus = 'ACTIVE'
    """)
    Optional<Auth> findActiveByUsername(String username);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
