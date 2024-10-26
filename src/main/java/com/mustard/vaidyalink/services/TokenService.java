package com.mustard.vaidyalink.services;

import com.mustard.vaidyalink.entities.Token;
import com.mustard.vaidyalink.entities.User;
import com.mustard.vaidyalink.repositories.TokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public void saveToken(User user, String jwtToken) {
        Token token = new Token();
        token.setToken(jwtToken);
        token.setUser(user);
        token.setExpiryDate(LocalDateTime.now().plusHours(6));
        token.setId(UUID.randomUUID());
        tokenRepository.save(token);
    }
}
