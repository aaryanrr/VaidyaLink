package com.mustard.vaidyalink.repositories;

import com.mustard.vaidyalink.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<Token, UUID> {
    Optional<Token> findByToken(String token);

    boolean existsByTokenAndUserIsNotNullAndInstitutionIsNull(String token);

    boolean existsByTokenAndInstitutionIsNotNullAndUserIsNull(String token);
}
