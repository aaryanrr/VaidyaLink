package com.mustard.vaidyalink.repositories;

import com.mustard.vaidyalink.entities.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, UUID> {
    Optional<Institution> findByEmail(String email);

    Optional<Institution> findByRegistrationNumber(String registrationNumber);
}
