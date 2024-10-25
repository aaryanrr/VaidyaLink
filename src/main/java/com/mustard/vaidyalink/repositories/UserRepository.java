package com.mustard.vaidyalink.repositories;

import com.mustard.vaidyalink.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByAadhaarNumberHash(String aadhaarNumberHash);

    Optional<User> findByEmail(String email);
}
